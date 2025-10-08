package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.model.ArtistModel;
import reactor.core.publisher.Mono;

public interface ArtistService {

    Mono<ArtistModel> save(ArtistModel artist);
}