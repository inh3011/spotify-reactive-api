package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.controller.dto.AlbumCountResponseDto;
import com.example.spotifyreactiveapi.controller.dto.CommonPageResponse;
import com.example.spotifyreactiveapi.controller.dto.ErrorResponse;
import com.example.spotifyreactiveapi.service.AlbumCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Album API", description = "Album 조회 API")
@RestController
@RequestMapping("/api/spotify/album-count")
@RequiredArgsConstructor
@Validated
public class AlbumCountController {

    private final AlbumCountService albumCountService;

    @Operation(summary = "연도 & 가수별 발매 앨범 수 조회", description = "연도/가수별 고유 앨범 수를 페이징하여 반환합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공적인 응답"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 파라미터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public Mono<CommonPageResponse<AlbumCountResponseDto>> getAlbumCount(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
            int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
            @Max(value = 1000, message = "페이지 크기는 1000 이하여야 합니다")
            int size,

            @Parameter(description = "정렬 컬럼 (release_year, artist_name )", example = "release_year")
            @RequestParam(defaultValue = "release_year")
            @Pattern(regexp = "^(release_year|artist_name)$", message = "정렬 컬럼은 release_year, artist_name 중 하나여야 합니다")
            String sortColumn,

            @Parameter(description = "정렬 방향 (ASC, DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC")
            @Pattern(regexp = "^(ASC|DESC)$", message = "정렬 방향은 ASC 또는 DESC여야 합니다")
            String sortDir,

            @Parameter(description = "가수 이름 키워드")
            @RequestParam(required = false)
            String artistKeyword,

            @Parameter(description = "발매 년도")
            @RequestParam(required = false)
            @Min(value = 1900, message = "발매 년도는 1900년 이상이어야 합니다")
            @Max(value = 2030, message = "발매 년도는 2030년 이하여야 합니다")
            Integer yearKeyword
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortColumn);
        Pageable pageable = PageRequest.of(page, size, sort);

        return Mono.zip(
                albumCountService.getAlbumCountByReleaseYearAndArtist(pageable, artistKeyword, yearKeyword).collectList(),
                albumCountService.countAlbumCounts(artistKeyword, yearKeyword)
        ).map(tuple -> new CommonPageResponse<>(tuple.getT1(), page, size, tuple.getT2()));
    }
}
