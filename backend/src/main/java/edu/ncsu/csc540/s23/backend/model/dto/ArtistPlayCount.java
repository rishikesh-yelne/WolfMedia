package edu.ncsu.csc540.s23.backend.model.dto;

public class ArtistPlayCount extends AlbumPlayCount {
    private Long albumId;

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
}
