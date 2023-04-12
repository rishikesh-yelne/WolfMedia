package edu.ncsu.csc540.s23.backend.service;

import edu.ncsu.csc540.s23.backend.constants.OperationQuery;
import edu.ncsu.csc540.s23.backend.model.Song;
import edu.ncsu.csc540.s23.backend.model.dto.ArtistSongDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class SongService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Connection getConnection() {
        try {
            return jdbcTemplate.getDataSource().getConnection();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public List<Song> getAllSongs() {
        return jdbcTemplate.query(OperationQuery.GET_ALL_SONGS, BeanPropertyRowMapper.newInstance(Song.class));
    }

    public Song getSong(Long song_id, Long album_id) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(OperationQuery.GET_SONG_BY_ID);
            statement.setLong(1, song_id);
            statement.setLong(2, album_id);

            ResultSet result = statement.executeQuery();
            Song song = new Song();
            while (result.next()) {
                song.setSongId(result.getLong(1));
                song.setAlbumId(result.getLong(2));
                song.setTitle(result.getString(3));
                song.setDuration(result.getTime(4));
                song.setTrackNo(result.getLong(5));
                song.setReleaseDate(result.getDate(6));
                song.setReleaseCountry(result.getString(7));
                song.setLanguage(result.getString(8));
                song.setRoyaltyRate(result.getDouble(9));
            }
            result.close();
            statement.close();

            return song;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Long createNewSong(Song song){
        try {
            Connection connection = getConnection();
            String[] generatedColumns = { "song_id" };
            PreparedStatement statement = connection.prepareStatement(OperationQuery.INSERT_SONG, generatedColumns);
            statement.setLong(1, song.getAlbumId());
            statement.setString(2, song.getTitle());
            statement.setTime(3, song.getDuration());
            statement.setLong(4, song.getTrackNo());
            statement.setDate(5, song.getReleaseDate());
            statement.setString(6, song.getReleaseCountry());
            statement.setString(7, song.getLanguage());
            statement.setDouble(8, song.getRoyaltyRate());


            int rowsAffected = statement.executeUpdate();
            if (rowsAffected <= 0) throw new SQLException("Song creation failed");

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                return result.getLong(1);
            }
            return -1L;
        }catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public List<ArtistSongDTO> getSongsByArtist(Long artistId) {
        return jdbcTemplate.query(OperationQuery.GET_SONGS_BY_ARTIST_ID, (rs, rowNum) -> {
            ArtistSongDTO song = new ArtistSongDTO();
            song.setSongId(rs.getLong(1));
            song.setAlbumId(rs.getLong(2));
            song.setTitle(rs.getString(3));
            song.setDuration(rs.getTime(4));
            song.setTrackNo(rs.getLong(5));
            song.setReleaseDate(rs.getDate(6));
            song.setReleaseCountry(rs.getString(7));
            song.setLanguage(rs.getString(8));
            song.setRoyaltyRate(rs.getDouble(9));
            song.setMainArtist(rs.getBoolean(10));
            return song;
        }, artistId);
    }

    public List<Song> getSongsByAlbum(Long albumId) {
        return jdbcTemplate.query(OperationQuery.GET_SONGS_BY_ALBUM_ID, BeanPropertyRowMapper.newInstance(Song.class), albumId);
    }

    public List<Song> getSongsByRecordLabel(Long recordLabelId) {
        return jdbcTemplate.query(OperationQuery.GET_SONGS_BY_RECORD_LABEL_ID, BeanPropertyRowMapper.newInstance(Song.class), recordLabelId);
    }

    public boolean updateSong(Song song){
        int rowsAffected = jdbcTemplate.update(OperationQuery.UPDATE_SONG, song.getTitle(), song.getDuration(), song.getTrackNo(), song.getReleaseDate(), song.getReleaseCountry(), song.getLanguage(), song.getRoyaltyRate(), song.getSongId(), song.getAlbumId());
        return rowsAffected>0;
    }

    public Long getPlayCount(Long songId, Long albumId, int month, int year) {
        return jdbcTemplate.queryForObject(OperationQuery.GET_SONG_PLAY_COUNT_FOR_MONTH, Long.class, songId, albumId, month, year);
    }

    public Long getArtistCount(Long songId, Long albumId) {
        return jdbcTemplate.queryForObject(OperationQuery.GET_SONG_ARTIST_COUNT, Long.class, songId, albumId);

    public boolean deleteSong(Long song_id){
        int rowsAffected=jdbcTemplate.update(OperationQuery.DELETE_SONG, song_id);
        return rowsAffected>0;
    }
}
