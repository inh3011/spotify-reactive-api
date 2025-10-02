package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.domain.Song;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SongService {

    Mono<Song> save(Song song);

    Mono<Void> saveAll(Flux<Song> songs);
}