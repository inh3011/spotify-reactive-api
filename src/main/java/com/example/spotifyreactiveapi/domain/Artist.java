package com.example.spotifyreactiveapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("artist")
public class Artist extends BaseEntity {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @MappedCollection(idColumn = "artist_id")
    private Set<Album> albums;

    @MappedCollection(idColumn = "artist_id")
    private Set<Song> songs;
}