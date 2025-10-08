package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.domain.Artist;
import com.example.spotifyreactiveapi.model.ArtistModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist toEntity(ArtistModel model);

    ArtistModel toModel(Artist entity);
}
