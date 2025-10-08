package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.domain.Song;
import com.example.spotifyreactiveapi.model.SongModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    Song toEntity(SongModel model);

    SongModel toModel(Song entity);
}
