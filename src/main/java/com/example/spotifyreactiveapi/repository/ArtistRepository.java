package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.domain.Artist;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends ReactiveCrudRepository<Artist, Long> {
}
