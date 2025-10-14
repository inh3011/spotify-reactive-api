package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponse;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SongLikeService {

    Mono<SongLikeModel> save(Long songId);

    Flux<SongLikeTopResponse> getTopLikes(Integer hour, Integer limit);
}
