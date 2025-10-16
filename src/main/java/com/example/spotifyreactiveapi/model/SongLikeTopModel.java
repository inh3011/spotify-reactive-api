package com.example.spotifyreactiveapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongLikeTopModel {

    private Long songId;
    private Long likeCount;
    private Integer rank;
    private String songTitle;
    private String albumName;
    private String artistName;
}
