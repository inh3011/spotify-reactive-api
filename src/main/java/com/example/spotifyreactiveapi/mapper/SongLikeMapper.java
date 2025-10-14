package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.controller.dto.SongLikeResponse;
import com.example.spotifyreactiveapi.controller.dto.SongLikeTopResponse;
import com.example.spotifyreactiveapi.domain.SongLike;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import com.example.spotifyreactiveapi.model.SongLikeTopModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongLikeMapper {

    SongLike toEntity(SongLikeModel model);

    SongLikeModel toModel(SongLike entity);

    SongLikeResponse toResponse(SongLikeModel model);

    SongLikeTopResponse toTopResponse(SongLikeTopModel model);
}
