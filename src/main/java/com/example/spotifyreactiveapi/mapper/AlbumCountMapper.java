package com.example.spotifyreactiveapi.mapper;

import com.example.spotifyreactiveapi.controller.dto.AlbumCountResponse;
import com.example.spotifyreactiveapi.model.AlbumCountModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumCountMapper {

    AlbumCountResponse toResponse(AlbumCountModel model);
}
