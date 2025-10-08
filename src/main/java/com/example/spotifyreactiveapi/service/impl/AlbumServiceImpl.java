package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Album;
import com.example.spotifyreactiveapi.model.AlbumModel;
import com.example.spotifyreactiveapi.repository.AlbumRepository;
import com.example.spotifyreactiveapi.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    @Override
    public Mono<AlbumModel> save(AlbumModel album) {
        return Mono.just(album)
                .flatMap(this::convertToEntity)
                .flatMap(albumRepository::save)
                .map(this::convertToModel);
    }

    private Mono<Album> convertToEntity(AlbumModel album) {
        Album createAlbum = Album.builder()
                .name(album.getName())
                .artistId(album.getArtistId())
                .build();

        return Mono.just(createAlbum);
    }

    private AlbumModel convertToModel(Album album) {
        return AlbumModel.builder()
                .id(album.getId())
                .name(album.getName())
                .artistId(album.getArtistId())
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .build();
    }
}
