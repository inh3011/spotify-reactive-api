package com.example.spotifyreactiveapi.service;

import com.example.spotifyreactiveapi.controller.dto.AlbumCountResponseDto;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumCountService {

    Flux<AlbumCountResponseDto> getAlbumCountByReleaseYearAndArtist(
            Pageable pageable,
            String artistKeyword,
            Integer yearKeyword);

    Mono<Long> countAlbumCounts(String artistKeyword, Integer yearKeyword);
}
