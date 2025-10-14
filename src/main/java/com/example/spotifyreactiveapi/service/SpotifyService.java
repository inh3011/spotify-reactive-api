package com.example.spotifyreactiveapi.service;

import java.io.BufferedInputStream;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpotifyService {

    Mono<BufferedInputStream> read();

    Flux<SpotifyData> parse(BufferedInputStream bufferedInputStream);

    Flux<SpotifyData> processFile();
}
