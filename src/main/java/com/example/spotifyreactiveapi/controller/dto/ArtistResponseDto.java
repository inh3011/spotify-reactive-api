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
@Schema(description = "아티스트 응답")
public class ArtistResponseDto {

    @Schema(description = "아티스트 ID")
    private Long id;

    @Schema(description = "아티스트 이름")
    private String name;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}
