package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponse;
import com.example.spotifyreactiveapi.domain.SongLike;
import com.example.spotifyreactiveapi.mapper.SongLikeMapper;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.model.SongLikeTopModel;
import com.example.spotifyreactiveapi.repository.SongLikeRepository;
import com.example.spotifyreactiveapi.repository.SongLikeTopRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongLikeServiceImplTest {

    @Mock
    private SongLikeRepository songLikeRepository;
    @Mock
    private SongLikeMapper songLikeMapper;
    @Mock
    private SongLikeTopRepository songLikeTopRepository;

    @InjectMocks
    private SongLikeServiceImpl songLikeServiceImpl;

    private SongLikeModel songLikeModel() {
        return SongLikeModel.builder()
                .id(100L)
                .songId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private SongLike songLike() {
        return SongLike.builder()
                .id(100L)
                .songId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private SongLikeTopModel topModel() {
        return SongLikeTopModel.builder()
                .songId(1L)
                .likeCount(100L)
                .rank(1)
                .build();
    }

    private SongLikeTopResponse topResponse() {
        return SongLikeTopResponse.builder()
                .songId(1L)
                .likeCount(100L)
                .rank(1)
                .build();
    }

    @Nested
    @DisplayName("좋아요 저장 검증")
    class SaveValidation {

        @Test
        @DisplayName("유효한 songId로 좋아요가 저장된다.")
        void shouldSaveSongLikeWithValidSongId() {
            when(songLikeMapper.toEntity(any(SongLikeModel.class))).thenReturn(songLike());
            when(songLikeRepository.save(any(SongLike.class))).thenReturn(Mono.just(songLike()));
            when(songLikeMapper.toModel(any(SongLike.class))).thenReturn(songLikeModel());

            StepVerifier.create(songLikeServiceImpl.save(1L))
                    .expectNextMatches(result -> result.getSongId().equals(1L) &&
                            result.getId().equals(100L) &&
                            result.getCreatedAt() != null &&
                            result.getUpdatedAt() != null)
                    .verifyComplete();
        }

    }

    @Nested
    @DisplayName("저장 예외 처리")
    class SaveExceptionHandling {

        @Test
        @DisplayName("저장 중 예외가 발생하면 RuntimeException을 반환한다.")
        void shouldReturnRuntimeExceptionWhenSaveFails() {
            when(songLikeMapper.toEntity(any(SongLikeModel.class))).thenReturn(songLike());
            when(songLikeRepository.save(any(SongLike.class)))
                    .thenReturn(Mono.error(new RuntimeException("Database error")));

            StepVerifier.create(songLikeServiceImpl.save(1L))
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                            throwable.getMessage().contains("Failed to save song like"))
                    .verify();
        }
    }

    @Nested
    @DisplayName("Top 좋아요 조회 검증")
    class GetTopLikesValidation {

        @Test
        @DisplayName("유효한 파라미터로 Top 좋아요가 조회된다.")
        void shouldGetTopLikesWithValidParameters() {
            when(songLikeTopRepository.findTopLikes(anyInt(), anyInt())).thenReturn(Flux.just(topModel()));
            when(songLikeMapper.toTopResponse(any(SongLikeTopModel.class))).thenReturn(topResponse());

            StepVerifier.create(songLikeServiceImpl.getTopLikes(3, 5))
                    .expectNextMatches(result -> result.getSongId().equals(1L) &&
                            result.getLikeCount().equals(100L) &&
                            result.getRank().equals(1))
                    .verifyComplete();
        }

        @Test
        @DisplayName("빈 결과가 반환된다.")
        void shouldReturnEmptyResultWhenNoData() {
            when(songLikeTopRepository.findTopLikes(anyInt(), anyInt())).thenReturn(Flux.empty());

            StepVerifier.create(songLikeServiceImpl.getTopLikes(1, 10))
                    .verifyComplete();

            verify(songLikeTopRepository).findTopLikes(anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Top 좋아요 조회 예외 처리")
    class GetTopLikesExceptionHandling {

        @Test
        @DisplayName("조회 중 예외가 발생하면 예외가 전파된다.")
        void shouldPropagateExceptionWhenQueryFails() {
            when(songLikeTopRepository.findTopLikes(anyInt(), anyInt()))
                    .thenReturn(Flux.error(new RuntimeException("Query failed")));

            StepVerifier.create(songLikeServiceImpl.getTopLikes(1, 10))
                    .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                            throwable.getMessage().equals("Query failed"))
                    .verify();

            verify(songLikeTopRepository).findTopLikes(anyInt(), anyInt());
        }
    }

}