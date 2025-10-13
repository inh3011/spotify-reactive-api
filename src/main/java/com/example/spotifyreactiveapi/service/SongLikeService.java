package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.model.SongModel;
import reactor.core.publisher.Mono;

public interface SongLikeService {

    Mono<SongLikeModel> save(Long songId);
}
