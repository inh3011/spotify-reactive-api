package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.domain.Song;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends ReactiveCrudRepository<Song, Long> {
}
