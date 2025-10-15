package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.service.SongLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
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
@DisplayName("SongLikeController 통합 테스트")
class SongLikeControllerIntegrationTest {

    private static final String BASE_URL = "/api/spotify/song";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SongLikeService songLikeService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        SongLikeService songLikeService() {
            return Mockito.mock(SongLikeService.class);
        }
    }

    @Nested
    @DisplayName("파라미터 유효성 검증")
    class ParameterValidation {

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -5})
        @DisplayName("hour 파라미터가 1 미만이면 400 에러를 반환한다.")
        void shouldReturn400WhenHourIsInvalid(int invalidHour) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + "/likes/top")
                            .queryParam("hour", invalidHour)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(songLikeService);
        }

        @ParameterizedTest
        @ValueSource(ints = {25, 30, 100})
        @DisplayName("hour 파라미터가 24 초과하면 400 에러를 반환한다.")
        void shouldReturn400WhenHourExceeds24(int invalidHour) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + "/likes/top")
                            .queryParam("hour", invalidHour)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(songLikeService);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10})
        @DisplayName("top 파라미터가 1 미만이면 400 에러를 반환한다.")
        void shouldReturn400WhenTopIsInvalid(int invalidTop) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + "/likes/top")
                            .queryParam("top", invalidTop)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(songLikeService);
        }

        @ParameterizedTest
        @ValueSource(ints = {101, 200, 1000})
        @DisplayName("top 파라미터가 100 초과하면 400 에러를 반환한다.")
        void shouldReturn400WhenTopExceeds100(int invalidTop) {
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + "/likes/top")
                            .queryParam("top", invalidTop)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();

            verifyNoInteractions(songLikeService);
        }
    }
}
