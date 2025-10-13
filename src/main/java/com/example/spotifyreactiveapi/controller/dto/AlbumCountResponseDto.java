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
@Schema(description = "연도 & 가수별 앨범 수 집계 응답")
public class AlbumCountResponseDto {

    @Schema(description = "발매년도")
    private Integer releaseYear;

    @Schema(description = "아티스트명")
    private String artistName;

    @Schema(description = "앨범 수")
    private Long albumCount;
}
