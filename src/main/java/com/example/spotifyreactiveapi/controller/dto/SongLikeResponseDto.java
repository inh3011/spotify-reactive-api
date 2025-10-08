package com.example.spotifyreactiveapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "노래 좋아요 응답")
public class SongLikeResponseDto {

    @Schema(description = "좋아요 ID")
    private Long id;

    @Schema(description = "노래 ID")
    private Long songId;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}
