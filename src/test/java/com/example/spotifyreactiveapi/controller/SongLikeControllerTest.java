package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.controller.dto.SongLikeResponse;
import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponse;
import com.example.spotifyreactiveapi.mapper.SongLikeMapper;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.service.SongLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SongLikeController 단위 테스트")
class SongLikeControllerTest {

    private static final String BASE_URL = "/api/spotify/song";
    private static final Long SONG_ID = 1L;
    private static final Long LIKE_ID = 100L;

    private WebTestClient webTestClient;

    @Mock
    private SongLikeService songLikeService;

    @Mock
    private SongLikeMapper songLikeMapper;

    @BeforeEach
    void setUp() {
        SongLikeController controller = new SongLikeController(songLikeService, songLikeMapper);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        webTestClient = WebTestClient.bindToController(controller)
                .validator(validator)
                .build();
    }

    private SongLikeModel createSongLikeModel() {
        return SongLikeModel.builder()
                .id(LIKE_ID)
                .songId(SONG_ID)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private SongLikeResponse createSongLikeResponse() {
        return SongLikeResponse.builder()
                .id(LIKE_ID)
                .songId(SONG_ID)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private List<SongLikeTopResponse> createTopLikesData(int count) {
        return java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> SongLikeTopResponse.builder()
                        .songId(i + 1L)
                        .likeCount(100 - i * 10L)
                        .rank(i + 1)
                        .build())
                .toList();
    }

    @Nested
    @DisplayName("좋아요 생성 검증")
    class LikeSongValidation {

        @Test
        @DisplayName("노래 좋아요가 성공적으로 생성된다.")
        void shouldCreateSongLikeSuccessfully() {
            // given
            SongLikeModel model = createSongLikeModel();
            SongLikeResponse response = createSongLikeResponse();

            when(songLikeService.save(SONG_ID)).thenReturn(Mono.just(model));
            when(songLikeMapper.toResponse(model)).thenReturn(response);

            // when & then
            webTestClient.post()
                    .uri(BASE_URL + "/{songId}/like", SONG_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(SongLikeResponse.class)
                    .value(result -> {
                        assertThat(result).isNotNull();
                        assertThat(result.getId()).isEqualTo(LIKE_ID);
                        assertThat(result.getSongId()).isEqualTo(SONG_ID);
                        assertThat(result.getCreatedAt()).isNotNull();
                    });

            verify(songLikeService).save(SONG_ID);
            verify(songLikeMapper).toResponse(model);
        }

        @Test
        @DisplayName("서비스에서 예외가 발생하면 500 에러를 반환한다.")
        void shouldReturn500WhenServiceThrowsException() {
            // given
            when(songLikeService.save(SONG_ID))
                    .thenReturn(Mono.error(new RuntimeException("Database error")));

            // when & then
            webTestClient.post()
                    .uri(BASE_URL + "/{songId}/like", SONG_ID)
                    .exchange()
                    .expectStatus().is5xxServerError();

            verify(songLikeService).save(SONG_ID);
            verifyNoInteractions(songLikeMapper);
        }

        @Test
        @DisplayName("잘못된 songId로 요청하면 500 에러를 반환한다.")
        void shouldReturn500WhenInvalidSongId() {
            // given
            Long invalidSongId = -1L;
            when(songLikeService.save(invalidSongId))
                    .thenReturn(Mono.error(new IllegalArgumentException("Song ID must be positive")));

            // when & then
            webTestClient.post()
                    .uri(BASE_URL + "/{songId}/like", invalidSongId)
                    .exchange()
                    .expectStatus().is5xxServerError();

            verify(songLikeService).save(invalidSongId);
            verifyNoInteractions(songLikeMapper);
        }
    }

    @Nested
    @DisplayName("Top 좋아요 조회 검증")
    class GetTopLikesValidation {

        @Test
        @DisplayName("기본 파라미터로 Top 좋아요가 성공적으로 조회된다.")
        void shouldGetTopLikesWithDefaultParameters() {
            // given
            List<SongLikeTopResponse> expectedData = createTopLikesData(3);

            when(songLikeService.getTopLikes(1, 10))
                    .thenReturn(Flux.fromIterable(expectedData));

            // when & then
            webTestClient.get()
                    .uri(BASE_URL + "/likes/top")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(SongLikeTopResponse.class)
                    .value(result -> {
                        assertThat(result).hasSize(3);
                        assertThat(result.getFirst().getSongId()).isEqualTo(1L);
                        assertThat(result.getFirst().getLikeCount()).isEqualTo(100L);
                        assertThat(result.getFirst().getRank()).isEqualTo(1);
                    });

            verify(songLikeService).getTopLikes(1, 10);
        }

        @Test
        @DisplayName("커스텀 파라미터로 Top 좋아요가 성공적으로 조회된다.")
        void shouldGetTopLikesWithCustomParameters() {
            // given
            int hour = 3;
            int top = 5;
            List<SongLikeTopResponse> expectedData = createTopLikesData(2);

            when(songLikeService.getTopLikes(hour, top))
                    .thenReturn(Flux.fromIterable(expectedData));

            // when & then
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + "/likes/top")
                            .queryParam("hour", hour)
                            .queryParam("top", top)
                            .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(SongLikeTopResponse.class)
                    .value(result -> {
                        assertThat(result).hasSize(2);
                    });

            verify(songLikeService).getTopLikes(hour, top);
        }

        @Test
        @DisplayName("빈 결과가 반환된다.")
        void shouldReturnEmptyListWhenNoResults() {
            // given
            when(songLikeService.getTopLikes(anyInt(), anyInt()))
                    .thenReturn(Flux.empty());

            // when & then
            webTestClient.get()
                    .uri(BASE_URL + "/likes/top")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(SongLikeTopResponse.class)
                    .value(result -> {
                        assertThat(result).isEmpty();
                    });

            verify(songLikeService).getTopLikes(1, 10);
        }

        @Test
        @DisplayName("서비스에서 예외가 발생하면 500 에러를 반환한다.")
        void shouldReturn500WhenServiceThrowsException() {
            // given
            when(songLikeService.getTopLikes(anyInt(), anyInt()))
                    .thenReturn(Flux.error(new RuntimeException("Query failed")));

            // when & then
            webTestClient.get()
                    .uri(BASE_URL + "/likes/top")
                    .exchange()
                    .expectStatus().is5xxServerError();

            verify(songLikeService).getTopLikes(1, 10);
        }

    }
}
