package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.model.SongLikeTopModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SongLikeService {

    Mono<SongLikeModel> save(Long songId);

    Flux<SongLikeTopModel> getTopLikes(Integer hour, Integer limit);
}
