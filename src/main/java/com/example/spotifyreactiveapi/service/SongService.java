package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.model.SongModel;
import reactor.core.publisher.Mono;

public interface SongService {

    Mono<SongModel> save(SongModel song);
}