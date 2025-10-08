package com.example.spotifyreactiveapi.service.impl;

import com.example.spotifyreactiveapi.domain.Song;
import com.example.spotifyreactiveapi.model.SongModel;
import com.example.spotifyreactiveapi.repository.SongRepository;
import com.example.spotifyreactiveapi.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    public Mono<SongModel> save(SongModel song) {
        return Mono.just(song)
                .flatMap(this::convertToEntity)
                .flatMap(songRepository::save)
                .map(this::convertToModel);
    }

    private Mono<Song> convertToEntity(SongModel song) {
        Song createSong = Song.builder()
                .title(song.getTitle())
                .artistId(song.getArtistId())
                .albumId(song.getAlbumId())
                .releaseDate(song.getReleaseDate())
                .releaseYear(song.getReleaseYear())
                .likeCount(song.getLikeCount() != null ? song.getLikeCount() : 0L)
                .build();

        return Mono.just(createSong);
    }

    private SongModel convertToModel(Song song) {
        return SongModel.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artistId(song.getArtistId())
                .albumId(song.getAlbumId())
                .releaseDate(song.getReleaseDate())
                .releaseYear(song.getReleaseYear())
                .likeCount(song.getLikeCount())
                .createdAt(song.getCreatedAt())
                .updatedAt(song.getUpdatedAt())
                .build();
    }
}