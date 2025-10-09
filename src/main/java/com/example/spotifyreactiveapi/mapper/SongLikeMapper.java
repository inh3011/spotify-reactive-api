package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.domain.SongLike;
import com.example.spotifyreactiveapi.model.SongLikeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongLikeMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SongLike toEntity(SongLikeModel model);

    SongLikeModel toModel(SongLike entity);
}
