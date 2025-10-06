package com.example.spotifyreactiveapi.service;

import java.io.InputStream;
import java.util.List;

import com.example.spotifyreactiveapi.controller.dto.SpotifyData;

import reactor.core.publisher.Mono;

public interface SpotifyService {

    Mono<InputStream> read();

    Mono<List<SpotifyData>> parse(InputStream inputStream);

    Mono<Void> save(List<SpotifyData> dataList);

    Mono<Void> process();
}
