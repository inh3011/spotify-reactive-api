package com.example.spotifyreactiveapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyData {

    // JSON 필드명 그대로 사용
    @JsonProperty("Artist(s)")
    private String artistName;

    @JsonProperty("song")
    private String songTitle;

    @JsonProperty("Album")
    private String albumName;

    @JsonProperty("Release Date")
    private String releaseDate;

    @JsonProperty("text")
    private String lyrics;

    @JsonProperty("Length")
    private String length;

    @JsonProperty("emotion")
    private String emotion;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("Tempo")
    private Double tempo;

    @JsonProperty("Loudness (db)")
    private Double loudness;

    @JsonProperty("Time signature")
    private String timeSignature;

    @JsonProperty("Explicit")
    private String explicit;

    @JsonProperty("Popularity")
    private String popularity;

    @JsonProperty("Energy")
    private String energy;

    @JsonProperty("Danceability")
    private String danceability;

    @JsonProperty("Positiveness")
    private String positiveness;

    @JsonProperty("Speechiness")
    private String speechiness;

    @JsonProperty("Liveness")
    private String liveness;

    @JsonProperty("Acousticness")
    private String acousticness;

    @JsonProperty("Instrumentalness")
    private String instrumentalness;
}