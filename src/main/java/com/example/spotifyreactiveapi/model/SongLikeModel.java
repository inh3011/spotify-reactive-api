package com.example.spotifyreactiveapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongLikeModel {

    private Long id;
    private Long songId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SongLikeModel create(Long songId) {
        return SongLikeModel.builder()
                .songId(songId)
                .build();
    }
}
