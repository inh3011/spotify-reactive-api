package com.example.spotifyreactiveapi.service;

import reactor.core.publisher.Mono;

public interface SpotifySaveService {

    Mono<Void> saveSpotifyData();
}
