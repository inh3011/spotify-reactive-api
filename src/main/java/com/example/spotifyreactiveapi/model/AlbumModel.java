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
public class AlbumModel {

    private Long id;
    private String name;
    private Long artistId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AlbumModel create(String name, Long artistId) {
        return AlbumModel.builder()
                .name(name)
                .artistId(artistId)
                .build();
    }
}
