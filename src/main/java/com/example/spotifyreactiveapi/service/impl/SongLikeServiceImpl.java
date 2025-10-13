package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.mapper.SongLikeMapper;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.repository.SongLikeRepository;
import com.example.spotifyreactiveapi.service.SongLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SongLikeServiceImpl implements SongLikeService {

    private final SongLikeRepository songLikeRepository;
    private final SongLikeMapper songLikeMapper;

    @Override
    public Mono<SongLikeModel> save(Long songId) {
        return Mono.just(SongLikeModel.create(songId))
                .map(songLikeMapper::toEntity)
                .flatMap(songLikeRepository::save)
                .map(songLikeMapper::toModel);
    }
}
