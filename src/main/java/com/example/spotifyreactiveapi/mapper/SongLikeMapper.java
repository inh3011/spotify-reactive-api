package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.domain.SongLike;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongLikeMapper {

    SongLike toEntity(SongLikeModel model);

    SongLikeModel toModel(SongLike entity);
}
