package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Song;
import com.example.spotifyreactiveapi.exception.EntityNotFoundException;
import com.example.spotifyreactiveapi.mapper.SongMapper;
import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.repository.SongRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SongServiceImpl 검증")
class SongServiceImplTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private SongMapper songMapper;

    @InjectMocks
    private SongServiceImpl songServiceImpl;

    private SongModel songModel() {
        return SongModel.builder()
                .id(1L)
                .artistName("Test Artist")
                .albumName("Test Album")
                .songTitle("Test Song")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .releaseYear(2023)
                .likeCount(0L)
                .build();
    }

    private Song song() {
        return Song.builder()
                .id(1L)
                .artistName("Test Artist")
                .albumName("Test Album")
                .songTitle("Test Song")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .releaseYear(2023)
                .likeCount(0L)
                .build();
    }

    @Nested
    @DisplayName("Song 조회 검증")
    class GetByIdValidation {

        @Test
        @DisplayName("유효한 ID로 Song을 조회하면 SongModel을 반환한다.")
        void shouldReturnSongModelWhenSongExists() {
            when(songRepository.findById(1L)).thenReturn(Mono.just(song()));
            when(songMapper.toModel(any(Song.class))).thenReturn(songModel());

            StepVerifier.create(songServiceImpl.getById(1L))
                    .expectNextMatches(result -> result.getId().equals(1L) &&
                            result.getArtistName().equals("Test Artist") &&
                            result.getAlbumName().equals("Test Album") &&
                            result.getSongTitle().equals("Test Song"))
                    .verifyComplete();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 Song을 조회하면 EntityNotFoundException이 발생한다.")
        void shouldThrowEntityNotFoundExceptionWhenSongNotExists() {
            when(songRepository.findById(999L)).thenReturn(Mono.empty());

            StepVerifier.create(songServiceImpl.getById(999L))
                    .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException &&
                            throwable.getMessage().contains("Song with id 999 not found"))
                    .verify();
        }
    }

    @Nested
    @DisplayName("Song 저장 검증")
    class SaveValidation {

        @Test
        @DisplayName("유효한 SongModel로 Song을 저장하면 저장된 SongModel을 반환한다.")
        void shouldSaveSongSuccessfully() {
            when(songMapper.toEntity(any(SongModel.class))).thenReturn(song());
            when(songRepository.save(any(Song.class))).thenReturn(Mono.just(song()));
            when(songMapper.toModel(any(Song.class))).thenReturn(songModel());

            StepVerifier.create(songServiceImpl.save(songModel()))
                    .expectNextMatches(result -> result.getId().equals(1L))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Song 저장 또는 업데이트 검증")
    class SaveOrUpdateValidation {

        @Test
        @DisplayName("유효한 SongModel로 Song을 저장 또는 업데이트하면 저장된 SongModel을 반환한다.")
        void shouldSaveOrUpdateSongSuccessfully() {
            when(songMapper.toEntity(any(SongModel.class))).thenReturn(song());
            when(songRepository.saveOrUpdate(any(Song.class))).thenReturn(Mono.just(song()));
            when(songMapper.toModel(any(Song.class))).thenReturn(songModel());

            StepVerifier.create(songServiceImpl.saveOrUpdate(songModel()))
                    .expectNextMatches(result -> result.getId().equals(1L))
                    .verifyComplete();
        }
    }
}
