package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.domain.Artist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistService {

    Mono<Artist> save(Artist artist);

    Mono<Void> saveAll(Flux<Artist> artists);
}