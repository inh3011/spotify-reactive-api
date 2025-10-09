package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.domain.Song;
import com.example.spotifyreactiveapi.model.SongModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(target = "songLikes", ignore = true)
    Song toEntity(SongModel model);

    SongModel toModel(Song entity);
}
