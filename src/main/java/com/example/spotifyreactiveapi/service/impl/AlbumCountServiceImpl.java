package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.controller.dto.AlbumCountResponse;
import com.example.spotifyreactiveapi.mapper.AlbumCountMapper;
import com.example.spotifyreactiveapi.repository.AlbumCountRetrieveRepository;
import com.example.spotifyreactiveapi.service.AlbumCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumCountServiceImpl implements AlbumCountService {

    private final AlbumCountRetrieveRepository albumCountRetrieveRepository;
    private final AlbumCountMapper albumCountMapper;

    @Override
    public Flux<AlbumCountResponse> getAlbumCountByReleaseYearAndArtist(
            Pageable pageable,
            String artistKeyword,
            Integer yearKeyword
    ) {
        return albumCountRetrieveRepository.findAlbumCountByReleaseYearAndArtist(pageable, artistKeyword, yearKeyword)
                .map(albumCountMapper::toResponse);
    }

    @Override
    public Mono<Long> countAlbumCounts(String artistKeyword, Integer yearKeyword) {
        return albumCountRetrieveRepository.countAlbumCounts(artistKeyword, yearKeyword);
    }
}
