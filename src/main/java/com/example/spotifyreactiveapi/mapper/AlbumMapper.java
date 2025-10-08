package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.domain.Album;
import com.example.spotifyreactiveapi.model.AlbumModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    Album toEntity(AlbumModel model);

    AlbumModel toModel(Album entity);
}
