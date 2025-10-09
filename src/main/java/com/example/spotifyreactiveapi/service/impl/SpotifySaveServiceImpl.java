package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.service.SongService;
import com.example.spotifyreactiveapi.service.SpotifySaveService;
import com.example.spotifyreactiveapi.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SpotifySaveServiceImpl implements SpotifySaveService {

    private final SpotifyService spotifyService;
    private final SongService songService;

    @Override
    public Mono<Void> saveSpotifyData() {
        return spotifyService.processFile()
                .flatMap(dataList -> Flux.fromIterable(dataList)
                        .flatMap(this::saveSong)
                        .then());
    }

    private Mono<Void> saveSong(SpotifyData data) {
        SongModel songModel = SongModel.create(
                data.getArtistName(),
                data.getAlbumName(),
                data.getSongTitle(),
                LocalDate.parse(data.getReleaseDate()),
                LocalDate.parse(data.getReleaseDate()).getYear());

        return songService.save(songModel)
                .then();
    }
}
