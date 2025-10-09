package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.domain.Song;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SongRepository extends ReactiveCrudRepository<Song, Long> {
}
