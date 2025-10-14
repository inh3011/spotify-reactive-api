package com.example.spotifyreactiveapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "노래 응답")
public class SongResponse {

    @Schema(description = "노래 ID")
    private Long id;

    @Schema(description = "곡 제목")
    private String title;

    @Schema(description = "아티스트 ID")
    private Long artistId;

    @Schema(description = "앨범 ID")
    private Long albumId;

    @Schema(description = "발매일")
    private LocalDate releaseDate;

    @Schema(description = "발매년도")
    private Integer releaseYear;

    @Schema(description = "좋아요 수")
    private Long likeCount;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}
