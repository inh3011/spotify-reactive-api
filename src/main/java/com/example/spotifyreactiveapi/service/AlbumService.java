package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.domain.Album;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumService {

    Mono<Album> save(Album album);

    Mono<Void> saveAll(Flux<Album> albums);
}