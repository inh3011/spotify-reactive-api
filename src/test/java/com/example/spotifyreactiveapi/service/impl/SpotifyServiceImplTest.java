package com.example.spotifyreactiveapi.service.impl;

import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.spotifyreactiveapi.config.SpotifyProperties;
import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.service.SpotifyService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SpotifyServiceImplTest {

    @Mock
    private SpotifyProperties spotifyProperties;

    private SpotifyService spotifyService;

    private String jsonData;

    @BeforeEach
    void setUp() {
        spotifyService = new SpotifyServiceImpl(spotifyProperties);

        jsonData = """
                [
                  {
                    "Artist(s)": "!!!",
                    "song": "Even When the Waters Cold",
                    "text": "Friends told her she was better off at the bottom of a river...",
                    "Length": "03:47",
                    "emotion": "sadness",
                    "Genre": "hip hop",
                    "Album": "Thr!!!er",
                    "Release Date": "2013-04-29",
                    "Key": "D min",
                    "Tempo": 0.4378698225,
                    "Loudness (db)": 0.785065407,
                    "Time signature": "4/4",
                    "Explicit": "No",
                    "Popularity": "40",
                    "Energy": "83",
                    "Danceability": "71",
                    "Positiveness": "87",
                    "Speechiness": "4",
                    "Liveness": "16",
                    "Acousticness": "11",
                    "Instrumentalness": "0"
                  }
                ]
                """;
    }

    @Nested
    @DisplayName("로컬 파일 경로 검증")
    class LocalFilePathValidation {

        @Test
        @DisplayName("파일 경로가 null이면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenFilePathIsNull() {
            // Given
            when(spotifyProperties.getFilePath()).thenReturn(null);

            // When & Then
            StepVerifier.create(spotifyService.read())
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("파일 경로가 비어있으면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenFilePathIsEmpty() {
            // Given
            when(spotifyProperties.getFilePath()).thenReturn("");

            // When & Then
            StepVerifier.create(spotifyService.read())
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("존재하지 않는 파일 경로면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenFileDoesNotExist() {
            // Given
            when(spotifyProperties.getFilePath()).thenReturn("nonexistent.json");

            // When & Then
            StepVerifier.create(spotifyService.read())
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("올바른 파일 확장자가 아니면 예외를 발생시킨다.")
        void shouldThrowExceptionWithNonJsonFile() {
            // Given
            when(spotifyProperties.getFilePath()).thenReturn("test.txt");

            // When & Then
            StepVerifier.create(spotifyService.read())
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("유효한 JSON 파일 경로일 경우 파싱까지 성공한다.")
        void shouldReadAndParseValidJsonFile() throws Exception {
            // Given
            Path tempFile = Files.createTempFile("test", ".json");
            Files.writeString(tempFile, jsonData);
            when(spotifyProperties.getFilePath()).thenReturn(tempFile.toString());

            // When
            Mono<List<SpotifyData>> result = spotifyService.read().flatMap(spotifyService::parse);

            // Then
            StepVerifier.create(result)
                    .expectNextMatches(dataList ->
                            dataList.size() == 1 &&
                                    dataList.getFirst().getArtistName().equals("!!!") &&
                                    dataList.getFirst().getSongTitle().equals("Even When the Waters Cold") &&
                                    dataList.getFirst().getAlbumName().equals("Thr!!!er"))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("데이터 파싱 검증")
    class DataParsingValidation {

        @Test
        @DisplayName("잘못된 JSON 구조를 파싱하면 예외를 발생시킨다.")
        void shouldThrowExceptionWithInvalidJsonStructure() {
            // Given
            String invalidJson = "{ invalid json }";
            InputStream inputStream = new ByteArrayInputStream(
                    invalidJson.getBytes(StandardCharsets.UTF_8));

            // When & Then
            StepVerifier.create(spotifyService.parse(inputStream))
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("JSON 데이터가 빈 경우 예외를 발생시킨다.")
        void shouldThrowExceptionWhenJsonDataIsEmpty() {
            String emptyJson = "";
            InputStream inputStream = new ByteArrayInputStream(emptyJson.getBytes(StandardCharsets.UTF_8));

            StepVerifier.create(spotifyService.parse(inputStream))
                    .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
                    .verify();
        }

        @Test
        @DisplayName("JSON 데이터가 빈 배열일 경우 빈 리스트를 반환하고 예외가 발생하지 않는다.")
        void shouldPassValidationWithEmptyJson() {
            // Given
            String emptyJson = "[]";

            // When
            InputStream inputStream = new ByteArrayInputStream(emptyJson.getBytes(StandardCharsets.UTF_8));
            Mono<List<SpotifyData>> result = spotifyService.parse(inputStream);

            // Then
            StepVerifier.create(result)
                    .expectNextMatches(List::isEmpty)
                    .verifyComplete();
        }

        @Test
        @DisplayName("JSON 데이터가 유효할 경우 예외가 발생하지 않는다.")
        void shouldPassValidationWithArrayJson() {
            // Given
            String jsonData = """
                    [
                      {
                        "Artist(s)": "!!!",
                        "song": "Even When the Waters Cold",
                        "Album": "Thr!!!er"
                      }
                    ]
                    """;

            // When
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
            Mono<List<SpotifyData>> result = spotifyService.parse(inputStream);

            // Then
            // When & Then
            StepVerifier.create(result)
                    .expectNextMatches(dataList ->
                            dataList.size() == 1 &&
                                    dataList.getFirst().getArtistName().equals("!!!") &&
                                    dataList.getFirst().getSongTitle().equals("Even When the Waters Cold") &&
                                    dataList.getFirst().getAlbumName().equals("Thr!!!er"))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("유효한 데이터 검증")
    class DataValidation {

        @Test
        @DisplayName("필수값 Artist(s) 가 없으면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenArtistNameIsNull() {
            // Given
            String jsonData = """
                    [
                      {
                        "Artist(s)": "",
                        "song": "Even When the Waters Cold",
                        "Album": "Thr!!!er"
                      }
                    ]
                    """;

            // When
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
            Mono<List<SpotifyData>> result = spotifyService.parse(inputStream);

            // Then
            StepVerifier.create(result)
                    .expectErrorMatches(throwable ->
                            throwable instanceof RuntimeException &&
                                    throwable.getMessage().contains("artist is required"))
                    .verify();
        }

        @Test
        @DisplayName("필수값 song 가 없으면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenSongTitleIsNull() {
            // Given
            String jsonData = """
                    [
                      {
                        "Artist(s)": "!!!",
                        "song": "",
                        "Album": "Thr!!!er"
                      }
                    ]
                    """;

            // When
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
            Mono<List<SpotifyData>> result = spotifyService.parse(inputStream);

            // Then
            StepVerifier.create(result)
                    .expectErrorMatches(throwable ->
                            throwable instanceof RuntimeException &&
                                    throwable.getMessage().contains("song is required"))
                    .verify();
        }

        @Test
        @DisplayName("필수값 Album 가 없으면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenAlbumNameIsNull() {
            // Given
            String jsonData = """
                    [
                      {
                        "Artist(s)": "!!!",
                        "song": "Even When the Waters Cold",
                        "Album": ""
                      }
                    ]
                    """;

            // When
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
            Mono<List<SpotifyData>> result = spotifyService.parse(inputStream);

            // Then
            StepVerifier.create(result)
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                            throwable.getMessage().contains("album is required"))
                    .verify();
        }

        @Test
        @DisplayName("모든 필수값 필드가 유효할 경우 예외가 발생하지 않는다.")
        void shouldPassValidationWithAllRequiredFields() {
            // Given
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
            Mono<List<SpotifyData>> result = spotifyService.parse(inputStream);

            // When & Then
            StepVerifier.create(result)
                    .expectNextMatches(dataList ->
                            dataList.size() == 1 &&
                                    dataList.getFirst().getArtistName().equals("!!!") &&
                                    dataList.getFirst().getSongTitle().equals("Even When the Waters Cold") &&
                                    dataList.getFirst().getAlbumName().equals("Thr!!!er"))
                    .verifyComplete();
        }

    }
}