package com.example.spotifyreactiveapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

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

    @Column("length_ms")
    private Integer lengthMs;

    @Column("explicit")
    private Boolean explicit;

    @Column("popularity")
    private Integer popularity;

    @Column("like_count")
    private Long likeCount;

    @Column("key_text")
    private String keyText;

    @Column("tempo")
    private Double tempo;

    @Column("loudness_db")
    private Double loudnessDb;

    @Column("time_signature")
    private String timeSignature;

    @Column("genre")
    private String genre;

    @Column("emotion")
    private String emotion;

    @Column("lyrics_text")
    private String lyricsText;

    @Column("energy")
    private Integer energy;

    @Column("danceability")
    private Integer danceability;

    @Column("positiveness")
    private Integer positiveness;

    @Column("speechiness")
    private Integer speechiness;

    @Column("liveness")
    private Integer liveness;

    @Column("acousticness")
    private Integer acousticness;

    @Column("instrumentalness")
    private Integer instrumentalness;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "song_id")
    private Set<SongLike> songLikes;
}
