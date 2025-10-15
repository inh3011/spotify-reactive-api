package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.service.AlbumCountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@DisplayName("AlbumCountController 통합 테스트")
class AlbumCountControllerIntegrationTest {

    private static final String BASE_URL = "/api/spotify/album-count";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AlbumCountService albumCountService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        AlbumCountService albumCountService() {
            return Mockito.mock(AlbumCountService.class);
        }
    }

    @Nested
    @DisplayName("파라미터 유효성 검증")
    class ParameterValidation {

        @ParameterizedTest
        @ValueSource(ints = { -1, -10 })
        @DisplayName("페이지 번호가 0 미만이면 400 에러를 반환한다.")
        void shouldReturn400WhenPageIs(int invalidPage) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("page", invalidPage)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(albumCountService);
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, -1 })
        @DisplayName("페이지 크기가 1 미만이면 400 에러를 반환한다.")
        void shouldReturn400WhenSizeIsInvalid(int invalidSize) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("size", invalidSize)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(albumCountService);
        }

        @ParameterizedTest
        @ValueSource(ints = { 1001 })
        @DisplayName("페이지 크기가 1000 초과하면 400 에러를 반환한다.")
        void shouldReturn400WhenSizeExceeds1000(int invalidSize) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("size", invalidSize)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(albumCountService);
        }

        @ParameterizedTest
        @ValueSource(strings = { "invalid_column", "album_count", "releaseYear" })
        @DisplayName("허용되지 않은 정렬 컬럼이면 400 에러를 반환한다.")
        void shouldReturn400WhenSortColumnIsInvalid(String invalidColumn) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("sortColumn", invalidColumn)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(albumCountService);
        }

        @ParameterizedTest
        @ValueSource(strings = { "asc", "desc", "ASCENDING", "invalid" })
        @DisplayName("허용되지 않은 정렬 방향이면 400 에러를 반환한다.")
        void shouldReturn400WhenSortDirIsInvalid(String invalidDir) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("sortDir", invalidDir)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(albumCountService);
        }

        @ParameterizedTest
        @CsvSource({
                "1899",
                "2031"
        })
        @DisplayName("년도 범위를 벗어나면 400 에러를 반환한다.")
        void shouldReturn400WhenYearIsOutOfRange(int invalidYear) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("yearKeyword", invalidYear)
                            .build())
                    .exchange()
                    .expectStatus().isBadRequest();

            verifyNoInteractions(albumCountService);
        }
    }
}
