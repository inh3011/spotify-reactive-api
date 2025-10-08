package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Song;
import com.example.spotifyreactiveapi.mapper.SongMapper;
import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.repository.SongRepository;
import com.example.spotifyreactiveapi.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final SongMapper songMapper;

    @Override
    public Mono<SongModel> save(SongModel song) {
        return Mono.just(song)
                .map(songMapper::toEntity)
                .flatMap(songRepository::save)
                .map(songMapper::toModel);
    }
}