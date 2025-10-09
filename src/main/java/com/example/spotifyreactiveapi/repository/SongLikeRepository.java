package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.domain.SongLike;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongLikeRepository extends ReactiveCrudRepository<SongLike, Long> {
}
