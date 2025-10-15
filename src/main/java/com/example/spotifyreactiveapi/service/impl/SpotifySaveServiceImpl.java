package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.mapper.SpotifyDataMapper;
import com.example.spotifyreactiveapi.service.SongService;
import com.example.spotifyreactiveapi.service.SpotifySaveService;
import com.example.spotifyreactiveapi.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifySaveServiceImpl implements SpotifySaveService {

    private final SpotifyService spotifyService;
    private final SongService songService;
    private final SpotifyDataMapper spotifyDataMapper;

    @Override
    public Mono<Void> saveSpotifyData() {
        return spotifyService.processFile()
                .flatMap(data -> saveSong(data)
                        .onErrorResume(error -> {
                            log.warn("Skipping invalid data. Reason: {}, Data: artist={}, album={}, title={}",
                                    error.getMessage(),
                                    data.getArtistName(),
                                    data.getAlbumName(),
                                    data.getSongTitle());
                            return Mono.empty();
                        }))
                .then();
    }

    private Mono<Void> saveSong(SpotifyData data) {
        return Mono.just(data)
                .doOnNext(this::validateSpotifyData)
                .map(spotifyDataMapper::toSongModel)
                .flatMap(songService::saveOrUpdate)
                .doOnSuccess(savedSong -> log.debug("Successfully saved song with ID: {}",
                        savedSong.getId()))
                .then();
    }

    private void validateSpotifyData(SpotifyData data) {
        if (data.getArtistName() == null && data.getAlbumName() == null && data.getSongTitle() == null) {
            throw new IllegalArgumentException(
                    "All required fields (artist_name, album_name, song_title) cannot be null");
        }
    }

}
