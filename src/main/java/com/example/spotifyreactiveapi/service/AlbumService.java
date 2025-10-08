package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.model.AlbumModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumService {

    Mono<AlbumModel> save(AlbumModel album);
}