package com.example.spotifyreactiveapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongModel {

    private Long id;
    private String artistName;
    private String albumName;
    private String songTitle;
    private LocalDate releaseDate;
    private Integer releaseYear;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SongModel create(
            String artistName,
            String albumName,
            String songTitle,
            LocalDate releaseDate,
            Integer releaseYear) {
        LocalDateTime now = LocalDateTime.now();
        return SongModel.builder()
                .artistName(artistName)
                .albumName(albumName)
                .songTitle(songTitle)
                .releaseDate(releaseDate)
                .releaseYear(releaseYear)
                .likeCount(0L)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
