package com.example.spotifyreactiveapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "최근 1시간 좋아요 Top10 응답")
public class SongLikeTopResponse {

    @Schema(description = "노래 ID")
    private Long songId;

    @Schema(description = "좋아요 수")
    private Long likeCount;

    @Schema(description = "등수 (1부터 시작)")
    private Integer rank;
}
