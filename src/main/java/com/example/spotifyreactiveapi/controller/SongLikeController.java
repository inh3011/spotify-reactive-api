package com.example.spotifyreactiveapi.controller;

import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.service.SongLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Song Like API", description = "노래 좋아요 관리 API")
@RestController
@RequestMapping("/api/spotify")
@RequiredArgsConstructor
public class SongLikeController {

    private final SongLikeService songLikeService;

    @Operation(summary = "좋아요 증가 API", description = "특정 노래의 좋아요를 1건 생성합니다.")
    @PostMapping("/songs/{songId}/like")
    public Mono<SongLikeModel> likeSong(@PathVariable("songId") Long songId) {
        return songLikeService.save(songId);
    }
}
