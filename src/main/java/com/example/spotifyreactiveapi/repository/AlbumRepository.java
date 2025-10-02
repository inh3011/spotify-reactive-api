package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.domain.Album;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends ReactiveCrudRepository<Album, Long> {
}
