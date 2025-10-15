package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.AlbumCountResponse;
import com.example.spotifyreactiveapi.mapper.AlbumCountMapper;
import com.example.spotifyreactiveapi.model.AlbumCountModel;
import com.example.spotifyreactiveapi.repository.AlbumCountRetrieveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlbumCountServiceImpl 검증")
class AlbumCountServiceImplTest {

    @Mock
    private AlbumCountRetrieveRepository albumCountRetrieveRepository;
    @Mock
    private AlbumCountMapper albumCountMapper;

    @InjectMocks
    private AlbumCountServiceImpl albumCountServiceImpl;

    private AlbumCountModel albumCountModel() {
        return AlbumCountModel.builder()
                .releaseYear(2023)
                .artistName("Test Artist")
                .albumCount(5L)
                .build();
    }

    private AlbumCountResponse albumCountResponse() {
        return AlbumCountResponse.builder()
                .releaseYear(2023)
                .artistName("Test Artist")
                .albumCount(5L)
                .build();
    }

    @Nested
    @DisplayName("앨범 카운트 조회 검증")
    class GetAlbumCountValidation {

        @Test
        @DisplayName("조회한 데이터가 Mapper를 통해 변환된다.")
        void shouldTransformRepositoryDataThroughMapper() {
            Pageable pageable = PageRequest.of(0, 10);
            when(albumCountRetrieveRepository.findAlbumCountByReleaseYearAndArtist(any(Pageable.class), anyString(), anyInt()))
                    .thenReturn(Flux.just(albumCountModel()));
            when(albumCountMapper.toResponse(any(AlbumCountModel.class)))
                    .thenReturn(albumCountResponse());

            StepVerifier.create(albumCountServiceImpl.getAlbumCountByReleaseYearAndArtist(pageable, "Artist", 2023))
                    .expectNextMatches(result -> result.getReleaseYear().equals(2023) &&
                            result.getArtistName().equals("Test Artist") &&
                            result.getAlbumCount().equals(5L))
                    .verifyComplete();
        }

        @Test
        @DisplayName("빈 결과가 반환되면 빈 Flux가 반환된다.")
        void shouldReturnEmptyFluxWhenRepositoryReturnsEmpty() {
            Pageable pageable = PageRequest.of(0, 10);
            when(albumCountRetrieveRepository.findAlbumCountByReleaseYearAndArtist(any(Pageable.class), anyString(), anyInt()))
                    .thenReturn(Flux.empty());

            StepVerifier.create(albumCountServiceImpl.getAlbumCountByReleaseYearAndArtist(pageable, "Artist", 2023))
                    .verifyComplete();
        }

        @Test
        @DisplayName("여러 데이터가 반환되면 모든 데이터가 변환된다.")
        void shouldTransformAllRepositoryData() {
            Pageable pageable = PageRequest.of(0, 10);
            AlbumCountModel model1 = albumCountModel();
            AlbumCountModel model2 = AlbumCountModel.builder()
                    .releaseYear(2022)
                    .artistName("Another Artist")
                    .albumCount(3L)
                    .build();

            AlbumCountResponse response1 = albumCountResponse();
            AlbumCountResponse response2 = AlbumCountResponse.builder()
                    .releaseYear(2022)
                    .artistName("Another Artist")
                    .albumCount(3L)
                    .build();

            when(albumCountRetrieveRepository.findAlbumCountByReleaseYearAndArtist(any(Pageable.class), anyString(), anyInt()))
                    .thenReturn(Flux.just(model1, model2));
            when(albumCountMapper.toResponse(model1)).thenReturn(response1);
            when(albumCountMapper.toResponse(model2)).thenReturn(response2);

            StepVerifier.create(albumCountServiceImpl.getAlbumCountByReleaseYearAndArtist(pageable, "Artist", 2023))
                    .expectNextMatches(result -> result.getReleaseYear().equals(2023))
                    .expectNextMatches(result -> result.getReleaseYear().equals(2022))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("예외 처리 검증")
    class ExceptionHandlingValidation {

        @Test
        @DisplayName("조회 중 예외가 발생하면 예외를 반환한다.")
        void shouldPropagateRepositoryException() {
            Pageable pageable = PageRequest.of(0, 10);
            when(albumCountRetrieveRepository.findAlbumCountByReleaseYearAndArtist(any(Pageable.class), anyString(), anyInt()))
                    .thenReturn(Flux.error(new RuntimeException("Database error")));

            StepVerifier.create(albumCountServiceImpl.getAlbumCountByReleaseYearAndArtist(pageable, "Artist", 2023))
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                            throwable.getMessage().equals("Database error"))
                    .verify();
        }

        @Test
        @DisplayName("조회 중 예외가 발생하면 예외를 반환한다.")
        void shouldPropagateCountRepositoryException() {
            when(albumCountRetrieveRepository.countAlbumCounts(anyString(), anyInt()))
                    .thenReturn(Mono.error(new RuntimeException("Count query failed")));

            StepVerifier.create(albumCountServiceImpl.countAlbumCounts("Artist", 2023))
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                            throwable.getMessage().equals("Count query failed"))
                    .verify();
        }
    }

    @Nested
    @DisplayName("앨범 카운트 개수 조회 검증")
    class CountAlbumCountsValidation {

        @Test
        @DisplayName("카운트를 조회하여 그대로 반환한다.")
        void shouldReturnCountFromRepository() {
            when(albumCountRetrieveRepository.countAlbumCounts(anyString(), anyInt()))
                    .thenReturn(Mono.just(100L));

            StepVerifier.create(albumCountServiceImpl.countAlbumCounts("Artist", 2023))
                    .expectNextMatches(count -> count.equals(100L))
                    .verifyComplete();
        }

        @Test
        @DisplayName("0이 반환되면 0을 반환한다.")
        void shouldReturnZeroWhenRepositoryReturnsZero() {
            when(albumCountRetrieveRepository.countAlbumCounts(anyString(), anyInt()))
                    .thenReturn(Mono.just(0L));

            StepVerifier.create(albumCountServiceImpl.countAlbumCounts("Artist", 2023))
                    .expectNextMatches(count -> count.equals(0L))
                    .verifyComplete();
        }
    }

}
