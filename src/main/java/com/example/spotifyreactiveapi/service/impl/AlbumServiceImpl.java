package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Album;
import com.example.spotifyreactiveapi.mapper.AlbumMapper;
import com.example.spotifyreactiveapi.model.AlbumModel;
import com.example.spotifyreactiveapi.repository.AlbumRepository;
import com.example.spotifyreactiveapi.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public Mono<AlbumModel> save(AlbumModel album) {
        return Mono.just(album)
                .map(albumMapper::toEntity)
                .flatMap(albumRepository::save)
                .map(albumMapper::toModel);
    }
}
