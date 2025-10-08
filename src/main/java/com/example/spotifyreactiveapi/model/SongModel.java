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
    private String title;
    private Long artistId;
    private Long albumId;
    private LocalDate releaseDate;
    private Integer releaseYear;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SongModel create(
            String title,
            Long artistId,
            Long albumId,
            LocalDate releaseDate,
            Integer releaseYear) {
        return SongModel.builder()
                .title(title)
                .artistId(artistId)
                .albumId(albumId)
                .releaseDate(releaseDate)
                .releaseYear(releaseYear)
                .likeCount(0L)
                .build();
    }
}
