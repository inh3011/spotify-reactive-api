package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.InputStream;

public interface SpotifyService {

    Mono<BufferedInputStream> read();

    Flux<SpotifyData> parse(InputStream inputStream);

    Flux<SpotifyData> processFile();
}
