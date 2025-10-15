package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.controller.dto.AlbumCountResponse;
import com.example.spotifyreactiveapi.controller.dto.CommonPageResponse;
import com.example.spotifyreactiveapi.service.AlbumCountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlbumCountController 단위 테스트")
class AlbumCountControllerTest {

    private static final String BASE_URL = "/api/spotify/album-count";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT_COLUMN = "release_year";
    private static final String DEFAULT_SORT_DIR = "DESC";

    private WebTestClient webTestClient;

    @Mock
    private AlbumCountService albumCountService;

    @BeforeEach
    void setUp() {
        AlbumCountController controller = new AlbumCountController(albumCountService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        webTestClient = WebTestClient.bindToController(controller)
                .validator(validator)
                .build();
    }

    private List<AlbumCountResponse> createTestData(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> AlbumCountResponse.builder()
                        .releaseYear(2020 + i)
                        .artistName("Artist " + (i + 1))
                        .albumCount(5L - i)
                        .build())
                .toList();
    }

    @Nested
    @DisplayName("앨범 카운트 조회 검증")
    class GetAlbumCountValidation {

        @Test
        @DisplayName("기본 파라미터로 앨범 카운트가 성공적으로 조회된다.")
        void shouldGetAlbumCountWithDefaultParameters() {
            // given
            List<AlbumCountResponse> expectedData = createTestData(2);
            long expectedTotal = 2L;

            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), isNull(), isNull()))
                    .thenReturn(Flux.fromIterable(expectedData));
            when(albumCountService.countAlbumCounts(isNull(), isNull()))
                    .thenReturn(Mono.just(expectedTotal));

            // when & then
            webTestClient.get()
                    .uri(BASE_URL)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(CommonPageResponse.class)
                    .value(response -> {
                        assertThat(response).isNotNull();
                        assertThat(response.getContent()).hasSize(2);
                        assertThat(response.getPage()).isEqualTo(DEFAULT_PAGE);
                        assertThat(response.getSize()).isEqualTo(DEFAULT_SIZE);
                        assertThat(response.getTotalElements()).isEqualTo(expectedTotal);
                    });

            // verify
            verify(albumCountService).getAlbumCountByReleaseYearAndArtist(
                    argThat(pageable -> pageable.getPageNumber() == DEFAULT_PAGE &&
                            pageable.getPageSize() == DEFAULT_SIZE),
                    isNull(),
                    isNull());
        }

        @Test
        @DisplayName("커스텀 파라미터로 앨범 카운트가 성공적으로 조회된다.")
        void shouldGetAlbumCountWithCustomParameters() {
            // given
            int page = 2;
            int size = 10;
            String sortColumn = "artist_name";
            String sortDir = "ASC";
            String artistKeyword = "BTS";
            int yearKeyword = 2020;

            List<AlbumCountResponse> expectedData = createTestData(50);
            long expectedTotal = 50L;

            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), eq(artistKeyword), eq(yearKeyword)))
                    .thenReturn(Flux.fromIterable(expectedData));
            when(albumCountService.countAlbumCounts(eq(artistKeyword), eq(yearKeyword)))
                    .thenReturn(Mono.just(expectedTotal));

            // when & then
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .queryParam("sortColumn", sortColumn)
                            .queryParam("sortDir", sortDir)
                            .queryParam("artistKeyword", artistKeyword)
                            .queryParam("yearKeyword", yearKeyword)
                            .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(CommonPageResponse.class)
                    .value(response -> {
                        assertThat(response).isNotNull();
                        assertThat(response.getContent()).hasSize(50);
                        assertThat(response.getPage()).isEqualTo(page);
                        assertThat(response.getSize()).isEqualTo(size);
                        assertThat(response.getTotalElements()).isEqualTo(expectedTotal);
                    });

            // verify
            verify(albumCountService).getAlbumCountByReleaseYearAndArtist(
                    argThat(pageable -> pageable.getPageNumber() == page &&
                            pageable.getPageSize() == size &&
                            pageable.getSort().equals(Sort.by(Sort.Direction.ASC, sortColumn))),
                    eq(artistKeyword),
                    eq(yearKeyword));
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 응답이 반환된다.")
        void shouldReturnEmptyResponseWhenNoResults() {
            // given
            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), any(), any()))
                    .thenReturn(Flux.empty());
            when(albumCountService.countAlbumCounts(any(), any()))
                    .thenReturn(Mono.just(0L));

            // when & then
            webTestClient.get()
                    .uri(BASE_URL)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(CommonPageResponse.class)
                    .value(response -> {
                        assertThat(response).isNotNull();
                        assertThat(response.getContent()).isEmpty();
                        assertThat(response.getTotalElements()).isZero();
                    });
        }

        @Test
        @DisplayName("가수 키워드만 제공한 경우 정상 조회")
        void shouldGetAlbumCountWithOnlyArtistKeyword() {
            // given
            String artistKeyword = "IU";
            List<AlbumCountResponse> expectedData = createTestData(3);

            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), eq(artistKeyword), isNull()))
                    .thenReturn(Flux.fromIterable(expectedData));
            when(albumCountService.countAlbumCounts(eq(artistKeyword), isNull()))
                    .thenReturn(Mono.just(3L));

            // when & then
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("artistKeyword", artistKeyword)
                            .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(CommonPageResponse.class)
                    .value(response -> {
                        assertThat(response).isNotNull();
                        assertThat(response.getContent()).hasSize(3);
                    });

            verify(albumCountService).getAlbumCountByReleaseYearAndArtist(any(Pageable.class), eq(artistKeyword),
                    isNull());
        }

        @Test
        @DisplayName("년도 키워드만 제공한 경우 정상 조회")
        void shouldGetAlbumCountWithOnlyYearKeyword() {
            // given
            int yearKeyword = 2021;
            List<AlbumCountResponse> expectedData = createTestData(5);

            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), isNull(), eq(yearKeyword)))
                    .thenReturn(Flux.fromIterable(expectedData));
            when(albumCountService.countAlbumCounts(isNull(), eq(yearKeyword)))
                    .thenReturn(Mono.just(5L));

            // when & then
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("yearKeyword", yearKeyword)
                            .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(CommonPageResponse.class)
                    .value(response -> {
                        assertThat(response).isNotNull();
                        assertThat(response.getContent()).hasSize(5);
                    });
        }
    }

    @Nested
    @DisplayName("예외 처리 검증")
    class ErrorHandlingValidation {

        @Test
        @DisplayName("서비스에서 예외가 발생하면 500 에러를 반환한다.")
        void shouldReturn500WhenServiceThrowsException() {
            // given
            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), any(), any()))
                    .thenReturn(Flux.error(new RuntimeException("Database connection failed")));

            // when & then
            webTestClient.get()
                    .uri(BASE_URL)
                    .exchange()
                    .expectStatus().is5xxServerError();
        }

        @Test
        @DisplayName("카운트 조회 중 예외 발생 시 500 에러 반환")
        void shouldReturn500WhenCountServiceThrowsException() {
            // given
            when(albumCountService.getAlbumCountByReleaseYearAndArtist(any(Pageable.class), any(), any()))
                    .thenReturn(Flux.fromIterable(createTestData(1)));
            when(albumCountService.countAlbumCounts(any(), any()))
                    .thenReturn(Mono.error(new RuntimeException("Count query failed")));

            // when & then
            webTestClient.get()
                    .uri(BASE_URL)
                    .exchange()
                    .expectStatus().is5xxServerError();
        }
    }
}