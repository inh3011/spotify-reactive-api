package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.mapper.SpotifyDataMapper;
import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.service.SongService;
import com.example.spotifyreactiveapi.service.SpotifyService;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpotifySaveServiceImplTest {

    @Mock
    private SpotifyService spotifyService;
    @Mock
    private SongService songService;
    @Mock
    private SpotifyDataMapper spotifyDataMapper;

    @InjectMocks
    private SpotifySaveServiceImpl spotifySaveServiceImpl;

    private SpotifyData data() {
        return SpotifyData.builder()
                .artistName("Artist")
                .albumName("Album")
                .songTitle("Song")
                .releaseDate("2023-01-01")
                .build();
    }

    private SongModel song() {
        return SongModel.builder()
                .id(1L)
                .artistName("Artist")
                .albumName("Album")
                .songTitle("Song")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .releaseYear(2023)
                .likeCount(0L)
                .build();
    }

    @Nested
    @DisplayName("Spotify 데이터 저장 검증")
    class SaveSpotifyDataValidation {

        @Test
        @DisplayName("유효한 데이터로 Song이 성공적으로 저장된다.")
        void shouldSaveSongWithValidData() {
            when(spotifyService.processFile()).thenReturn(Flux.just(data()));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();
        }

        @Test
        @DisplayName("Song 저장 시 올바른 데이터가 전달된다.")
        void shouldPassCorrectDataWhenSaving() {
            when(spotifyService.processFile()).thenReturn(Flux.just(data()));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();

            verify(spotifyDataMapper).toSongModel(argThat(spotifyData -> spotifyData.getArtistName().equals("Artist") &&
                    spotifyData.getAlbumName().equals("Album") &&
                    spotifyData.getSongTitle().equals("Song") &&
                    spotifyData.getReleaseDate().equals("2023-01-01")));
            verify(songService).saveOrUpdate(argThat(songModel -> songModel.getArtistName().equals("Artist") &&
                    songModel.getAlbumName().equals("Album") &&
                    songModel.getSongTitle().equals("Song") &&
                    songModel.getReleaseDate().equals(LocalDate.of(2023, 1, 1)) &&
                    songModel.getReleaseYear().equals(2023)));
        }

    }

    @Nested
    @DisplayName("null 필드 처리 검증")
    class NullFieldHandling {
        @Test
        @DisplayName("artist_name이 null이어도 Song이 저장된다.")
        void shouldSaveSongWithNullArtistName() {
            SpotifyData spotifyData = data();
            spotifyData.setArtistName(null);

            when(spotifyService.processFile()).thenReturn(Flux.just(spotifyData));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();

            verify(spotifyDataMapper).toSongModel(argThat(data -> data.getArtistName() == null));
        }

        @Test
        @DisplayName("song_title이 null이어도 Song이 저장된다.")
        void shouldSaveSongWithNullSongTitle() {
            SpotifyData spotifyData = data();
            spotifyData.setSongTitle(null);

            when(spotifyService.processFile()).thenReturn(Flux.just(spotifyData));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();

            verify(spotifyDataMapper).toSongModel(argThat(data -> data.getSongTitle() == null));
        }

        @Test
        @DisplayName("모든 필드가 null이면 예외가 발생한다.")
        void shouldThrowExceptionWhenAllFieldsAreNull() {
            SpotifyData spotifyData = data();
            spotifyData.setArtistName(null);
            spotifyData.setAlbumName(null);
            spotifyData.setSongTitle(null);

            when(spotifyService.processFile()).thenReturn(Flux.just(spotifyData));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .expectError(IllegalArgumentException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("발매일 파싱 검증")
    class DateParsingValidation {
        @Test
        @DisplayName("유효한 날짜(YYYY-MM-DD)면 releaseDate/Year가 정상 전달된다.")
        void shouldPropagateValidDate() {
            SpotifyData spotifyData = data();
            spotifyData.setReleaseDate("2023-01-01");
            when(spotifyService.processFile()).thenReturn(Flux.just(spotifyData));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();

            verify(songService)
                    .saveOrUpdate(argThat(songModel -> songModel.getReleaseDate().equals(LocalDate.of(2023, 1, 1)) &&
                            songModel.getReleaseYear().equals(2023)));
        }

        @Test
        @DisplayName("잘못된 날짜 형식이면 예외가 발생한다.")
        void shouldPropagateErrorWithInvalidDate() {
            SpotifyData spotifyData = data();
            spotifyData.setReleaseDate("2023/01/01");
            when(spotifyService.processFile()).thenReturn(Flux.just(spotifyData));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class)))
                    .thenThrow(new DateTimeParseException("Invalid date format", "2023/01/01", 0));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .expectError(DateTimeParseException.class)
                    .verify();
        }

        @Test
        @DisplayName("날짜가 null이면 releaseDate와 releaseYear가 null로 저장된다.")
        void shouldSaveAsNullWhenDateIsNull() {
            SpotifyData spotifyData = data();
            spotifyData.setReleaseDate(null);
            when(spotifyService.processFile()).thenReturn(Flux.just(spotifyData));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();
        }

    }

    @Nested
    @DisplayName("서비스 호출 검증")
    class ServiceInteractionValidation {
        @Test
        @DisplayName("파일 처리 서비스는 한 번만 호출된다.")
        void shouldCallProcessFileOnce() {
            when(spotifyService.processFile()).thenReturn(Flux.just(data()));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();
        }

        @Test
        @DisplayName("Song 서비스는 데이터 개수만큼 호출된다.")
        void shouldCallSongServiceForEachData() {
            SpotifyData data1 = data();
            SpotifyData data2 = data();
            data2.setSongTitle("Song2");

            when(spotifyService.processFile()).thenReturn(Flux.just(data1, data2));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class))).thenReturn(Mono.just(song()));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .verifyComplete();
        }

        @Test
        @DisplayName("Song 서비스 에러가 발생하면 예외가 전파된다.")
        void shouldPropagateExceptionWhenSongServiceFails() {
            when(spotifyService.processFile()).thenReturn(Flux.just(data()));
            when(spotifyDataMapper.toSongModel(any(SpotifyData.class))).thenReturn(song());
            when(songService.saveOrUpdate(any(SongModel.class)))
                    .thenReturn(Mono.error(new RuntimeException("DB error")));

            StepVerifier.create(spotifySaveServiceImpl.saveSpotifyData())
                    .expectError(RuntimeException.class)
                    .verify();
        }
    }
}