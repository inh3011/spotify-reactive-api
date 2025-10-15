package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponse;
import com.example.spotifyreactiveapi.mapper.SongLikeMapper;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.repository.SongLikeRepository;
import com.example.spotifyreactiveapi.repository.SongLikeTopRepository;
import com.example.spotifyreactiveapi.service.SongLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SongLikeServiceImpl implements SongLikeService {

    private final SongLikeRepository songLikeRepository;
    private final SongLikeMapper songLikeMapper;
    private final SongLikeTopRepository songLikeTopRepository;

    @Override
    public Mono<SongLikeModel> save(Long songId) {
        return Mono.just(songId)
                .doOnNext(this::validateSongId)
                .map(SongLikeModel::create)
                .map(songLikeMapper::toEntity)
                .flatMap(songLikeRepository::save)
                .map(songLikeMapper::toModel)
                .onErrorMap(IllegalArgumentException.class, exception -> exception)
                .onErrorMap(Exception.class,
                        exception -> new RuntimeException("Failed to save song like: " + exception.getMessage(), exception));
    }

    @Override
    public Flux<SongLikeTopResponse> getTopLikes(Integer hour, Integer limit) {
        return Mono.just(hour)
                .then(Mono.just(limit))
                .thenMany(songLikeTopRepository.findTopLikes(hour, limit))
                .map(songLikeMapper::toTopResponse);
    }

    private void validateSongId(Long songId) {
        if (songId == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }
        if (songId < 0L) {
            throw new IllegalArgumentException("Song ID must be non-negative");
        }
    }
}
