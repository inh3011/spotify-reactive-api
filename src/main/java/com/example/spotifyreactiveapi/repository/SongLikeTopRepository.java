package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.model.SongLikeTopModel;
import reactor.core.publisher.Flux;

public interface SongLikeTopRepository {

    Flux<SongLikeTopModel> findTopLikes(Integer hour, Integer top);
}
