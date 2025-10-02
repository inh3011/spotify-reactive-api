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
@Table("album")
public class Album {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("release_date")
    private LocalDate releaseDate;

    @Column("release_year")
    private Integer releaseYear;

    @Column("artist_id")
    private Long artistId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "album_id")
    private Set<Song> songs;
}
