package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.SongLike;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.repository.SongLikeRepository;
import com.example.spotifyreactiveapi.service.SongLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SongLikeServiceImpl implements SongLikeService {

    private final SongLikeRepository songLikeRepository;

    @Override
    public Mono<SongLikeModel> save(SongLikeModel songLike) {
        return Mono.just(songLike)
                .flatMap(this::createSongLike)
                .flatMap(songLikeRepository::save)
                .map(this::convertToModel);
    }

    private Mono<SongLike> createSongLike(SongLikeModel songLike) {
        SongLike songLikes = SongLike.builder()
                .songId(songLike.getSongId())
                .build();

        return Mono.just(songLikes);
    }

    private SongLikeModel convertToModel(SongLike songLike) {
        return SongLikeModel.builder()
                .id(songLike.getId())
                .songId(songLike.getSongId())
                .createdAt(songLike.getCreatedAt())
                .updatedAt(songLike.getUpdatedAt())
                .build();
    }
}
