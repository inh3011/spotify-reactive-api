package com.example.spotifyreactiveapi.repository;

import com.example.spotifyreactiveapi.model.AlbumCountModel;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AlbumCountRetrieveRepository {

    Flux<AlbumCountModel> findAlbumCountByReleaseYearAndArtist(
            Pageable pageable,
            String artistKeyword,
            Integer yearKeyword);

    Mono<Long> countAlbumCounts(String artistKeyword, Integer yearKeyword);

}