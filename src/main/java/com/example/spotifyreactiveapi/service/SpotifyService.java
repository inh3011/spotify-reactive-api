package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

public interface SpotifyService {

    Mono<InputStream> read();

    Mono<List<SpotifyData>> parse(InputStream inputStream);

    Mono<List<SpotifyData>> processFile();
}
