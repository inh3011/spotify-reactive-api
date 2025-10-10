package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.service.SongService;
import com.example.spotifyreactiveapi.service.SpotifySaveService;
import com.example.spotifyreactiveapi.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifySaveServiceImpl implements SpotifySaveService {

    private final SpotifyService spotifyService;
    private final SongService songService;

    @Override
    public Mono<Void> saveSpotifyData() {
        return spotifyService.processFile()
                .flatMap(this::saveSong)
                .then();
    }

    private Mono<Void> saveSong(SpotifyData data) {
        LocalDate releaseDate = data.getReleaseDate() != null ? LocalDate.parse(data.getReleaseDate()) : null;
        Integer releaseYear = data.getReleaseDate() != null ? LocalDate.parse(data.getReleaseDate()).getYear()
                : null;

        SongModel songModel = SongModel.create(
                data.getArtistName(),
                data.getAlbumName(),
                data.getSongTitle(),
                releaseDate,
                releaseYear);

        log.debug("Saving song: artist={}, album={}, title={}, releaseDate={}, releaseYear={}",
                data.getArtistName(), data.getAlbumName(), data.getSongTitle(), releaseDate,
                releaseYear);

        return songService.saveOrUpdate(songModel)
                .doOnSuccess(savedSong -> log.debug("Successfully saved song with ID: {}", savedSong.getId()))
                .doOnError(error -> log.error("Failed to save/update song: {}", error.getMessage()))
                .then();
    }
}
