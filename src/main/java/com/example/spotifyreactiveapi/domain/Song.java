package com.example.spotifyreactiveapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("song")
public class Song {

    @Id
    private Long id;

    @Column("title")
    private String title;

    @Column("artist_id")
    private Long artistId;

    @Column("album_id")
    private Long albumId;

    @Column("release_date")
    private LocalDate releaseDate;

    @Column("release_year")
    private Integer releaseYear;

    @Column("like_count")
    private Long likeCount;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "song_id")
    private Set<SongLike> songLikes;
}
