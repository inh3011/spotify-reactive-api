package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.model.SongLikeModel;
import reactor.core.publisher.Mono;

public interface SongLikeService {

    Mono<SongLikeModel> save(SongLikeModel songLike);
}
