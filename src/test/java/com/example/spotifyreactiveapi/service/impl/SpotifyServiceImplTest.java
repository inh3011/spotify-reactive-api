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
    @DisplayName("readFile 메서드 테스트")
    class ReadFileTests {

        @Test
        @DisplayName("json 파일 읽기 - 빈 파일 처리 테스트")
        void shouldHandleEmptyFile() throws IOException {
            // Given
            when(multipartFile.getInputStream())
                    .thenReturn(new ByteArrayInputStream(new byte[0]));

            // When
            Mono<String> result = spotifyService.readFile(multipartFile);

            // Then
            StepVerifier.create(result)
                    .expectNext("")
                    .verifyComplete();
        }

        @Test
        @DisplayName("json 파일 읽기 - IOException 발생시 exception 반환")
        void shouldHandleFileReadError() throws IOException {
            // Given
            when(multipartFile.getInputStream())
                    .thenThrow(new IOException("File read error"));

            // When & Then
            StepVerifier.create(spotifyService.readFile(multipartFile))
                    .expectError(RuntimeException.class)
                    .verify();
        }

        @Test
        @DisplayName("json 파일 읽기 - string 변환을 성공")
        void shouldReadFileSuccessfully() throws IOException {
            // Given
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
    @DisplayName("parseJsonData 메서드 테스트")
    class ParseJsonDataTests {

        @Test
        @DisplayName("json 데이터 파싱 - 빈 문자열 처리 테스트")
        void shouldHandleEmptyJsonData() {
            // Given
            String emptyJson = "[]";

            // When
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(emptyJson);

            // Then
            StepVerifier.create(result)
                    .expectNext(List.of())
                    .verifyComplete();
        }

        @Test
        @DisplayName("json 데이터 파싱 - 잘못된 문자열 처리 테스트")
        void shouldHandleInvalidJsonData() {
            // Given
            String invalidJson = "{ invalid json }";

            // When & Then
            StepVerifier.create(spotifyService.parseJsonData(invalidJson))
                    .expectError()
                    .verify();
        }

        @Test
        @DisplayName("json 데이터 파싱 - null 문자열 처리 테스트")
        void shouldHandleNullJsonData() {
            // When & Then
            StepVerifier.create(spotifyService.parseJsonData(null))
                    .expectError()
                    .verify();
        }

        @Test
        @DisplayName("json 데이터 파싱 - SpotifyData 반환 성공")
        void shouldParseJsonDataSuccessfully() {
            // When
            Mono<List<SpotifyData>> result = spotifyService.parseJsonData(jsonData);

            // Then
            StepVerifier.create(result)
                    .expectNextMatches(dataList -> dataList.size() == 1 &&
                            dataList.getFirst().getArtistName().equals("!!!") &&
                            dataList.getFirst().getSongTitle().equals("Even When the Waters Cold") &&
                            dataList.getFirst().getAlbumName().equals("Thr!!!er"))
                    .verifyComplete();
        }
    }
}