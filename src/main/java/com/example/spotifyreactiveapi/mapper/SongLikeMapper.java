package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.controller.dto.SongLikeResponseDto;
import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponseDto;
import com.example.spotifyreactiveapi.domain.SongLike;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.model.SongLikeTopModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongLikeMapper {

    SongLike toEntity(SongLikeModel model);

    SongLikeModel toModel(SongLike entity);

    SongLikeResponseDto toResponse(SongLikeModel model);

    SongLikeTopResponseDto toTopResponse(SongLikeTopModel model);
}
