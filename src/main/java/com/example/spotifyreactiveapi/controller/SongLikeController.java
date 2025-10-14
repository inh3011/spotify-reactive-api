package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.controller.dto.SongLikeResponse;
import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponse;
import com.example.spotifyreactiveapi.mapper.SongLikeMapper;
import com.example.spotifyreactiveapi.service.SongLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Song Like API", description = "노래 좋아요 관리 API")
@RestController
@RequestMapping("/api/spotify/song")
@RequiredArgsConstructor
public class SongLikeController {

    private final SongLikeService songLikeService;
    private final SongLikeMapper songLikeMapper;

    @Operation(summary = "좋아요 증가 API", description = "특정 노래의 좋아요를 1건 생성합니다.")
    @PostMapping("/{songId}/like")
    public Mono<SongLikeResponse> likeSong(@PathVariable("songId") Long songId) {
        return songLikeService.save(songId).map(songLikeMapper::toResponse);
    }

    @Operation(summary = "좋아요 Top 조회", description = "hour 과 limit 으로 좋아요 상위 곡을 조회합니다. 예: hour=1&top=10")
    @GetMapping("/likes/top")
    public Flux<SongLikeTopResponse> getTopLikes(
            @RequestParam(name = "hour", defaultValue = "1")
            @Min(value = 1, message = "시간은 1 이상이어야 합니다")
            @Max(value = 24, message = "시간은 24 이하여야 합니다")
            Integer hour,

            @RequestParam(name = "top", defaultValue = "10")
            @Min(value = 1, message = "제한 크기는 1 이상이어야 합니다")
            @Max(value = 100, message = "제한 크기는 100 이하여야 합니다")
            Integer top
    ) {
        return songLikeService.getTopLikes(hour, top);
    }
}
