package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Artist;
import com.example.spotifyreactiveapi.mapper.ArtistMapper;
import com.example.spotifyreactiveapi.model.ArtistModel;
import com.example.spotifyreactiveapi.repository.ArtistRepository;
import com.example.spotifyreactiveapi.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public Mono<ArtistModel> save(ArtistModel artist) {
        return Mono.just(artist)
                .map(artistMapper::toEntity)
                .flatMap(artistRepository::save)
                .map(artistMapper::toModel);
    }
}
