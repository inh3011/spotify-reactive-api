package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.service.SpotifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotifyServiceImplTest {

    @Mock
    private MultipartFile multipartFile;

    private SpotifyService spotifyService;

    private String jsonData;

    @BeforeEach
    void setUp() {
        spotifyService = new SpotifyServiceImpl();

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
    @DisplayName("파일 업로드 검증")
    class FileUploadValidation {

        @Test
        @DisplayName("파일이 Null 이면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenFileIsNull() {
            // When & Then
            StepVerifier.create(spotifyService.readFile(null))
                    .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
                    .verify();
        }

        @Test
        @DisplayName("파일이 비어있으면 예외를 발생시킨다.")
        void shouldThrowExceptionWhenFileIsEmpty() throws IOException {
            // Given
            when(multipartFile.isEmpty()).thenReturn(true);

            // When & Then
            StepVerifier.create(spotifyService.readFile(multipartFile))
                    .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
                    .verify();
        }

        @Test
        @DisplayName("올바른 파일 확장자가 아니면 예외를 발생시킨다.")
        void shouldThrowExceptionWithNonJsonFile() throws IOException {
            // Given
            when(multipartFile.isEmpty()).thenReturn(false);
            when(multipartFile.getOriginalFilename()).thenReturn("test.txt");

            // When & Then
            StepVerifier.create(spotifyService.readFile(multipartFile))
                    .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
                    .verify();
        }

        @Test
        @DisplayName("파일이 유요할 경우 예외가 발생하지 않는다.")
        void shouldPassValidationWithJsonFile() throws IOException {
            // Given
            when(multipartFile.isEmpty()).thenReturn(false);
            when(multipartFile.getOriginalFilename()).thenReturn("sample.json");
            when(multipartFile.getInputStream())
                    .thenReturn(new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8)));

            // When
            Mono<String> result = spotifyService.readFile(multipartFile);

            // Then
            StepVerifier.create(result)
                    .expectNext(jsonData)
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

            // When & Then
            StepVerifier.create(spotifyService.parseJsonData(invalidJson))
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("JSON 데이터가 빈 경우 예외를 발생시킨다.")
        void shouldThrowExceptionWhenJsonDataIsEmpty() {
            String emptyJson = "";

            StepVerifier.create(spotifyService.parseJsonData(emptyJson))
                    .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
                    .verify();
        }

        @Test
        @DisplayName("JSON 데이터가 빈 배열일 경우 빈 리스트를 반환하고 예외가 발생하지 않는다.")
        void shouldPassValidationWithEmptyJson() {
            // Given
            String emptyJson = "[]";

            // When
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(emptyJson);

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
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(jsonData);

            // Then
            // When & Then
            StepVerifier.create(result)
                    .expectNextMatches(dataList -> dataList.size() == 1 &&
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
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(jsonData);

            // Then
            StepVerifier.create(result)
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
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
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(jsonData);

            // Then
            StepVerifier.create(result)
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
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
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(jsonData);

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
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(jsonData);

            // When & Then
            StepVerifier.create(result)
                    .expectNextMatches(dataList -> dataList.size() == 1 &&
                            dataList.getFirst().getArtistName().equals("!!!") &&
                            dataList.getFirst().getSongTitle().equals("Even When the Waters Cold") &&
                            dataList.getFirst().getAlbumName().equals("Thr!!!er"))
                    .verifyComplete();
        }

    }
}