package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Artist;
import com.example.spotifyreactiveapi.model.ArtistModel;
import com.example.spotifyreactiveapi.repository.ArtistRepository;
import com.example.spotifyreactiveapi.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public Mono<ArtistModel> save(ArtistModel artist) {
        return Mono.just(artist)
                .flatMap(this::convertToEntity)
                .flatMap(artistRepository::save)
                .map(this::convertToModel);
    }

    private Mono<Artist> convertToEntity(ArtistModel artist) {
        Artist createArtist = Artist.builder()
                .name(artist.getName())
                .build();

        return Mono.just(createArtist);
    }

    private ArtistModel convertToModel(Artist artist) {
        return ArtistModel.builder()
                .id(artist.getId())
                .name(artist.getName())
                .createdAt(artist.getCreatedAt())
                .updatedAt(artist.getUpdatedAt())
                .build();
    }
}
