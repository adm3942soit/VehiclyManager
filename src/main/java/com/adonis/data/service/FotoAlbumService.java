package com.adonis.data.service;

import com.adonis.data.persons.Address;
import com.adonis.data.vehicles.Vehicle;
import com.adonis.data.vehicles.img.Foto;
import com.adonis.data.vehicles.img.FotoAlbum;
import com.adonis.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.adonis.utils.DateUtils.pattern;

@Component
public class FotoAlbumService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<FotoAlbum> findAll() {
        String sql = "SELECT * FROM foto_album";
        List<FotoAlbum> fotoAlbums = jdbcTemplate.query(sql, new BeanPropertyRowMapper(FotoAlbum.class));
        return fotoAlbums;
    }

    public FotoAlbum findById(Long albumId) {
        if (albumId == null) return null;
        String sql = "SELECT * FROM foto_album WHERE ID = ?";

        FotoAlbum fotoAlbum = null;
        try {
            fotoAlbum = (FotoAlbum) jdbcTemplate.queryForObject(sql, new Object[]{albumId}, new BeanPropertyRowMapper(FotoAlbum.class));
        } catch (Exception e) {
            return null;
        }
        return fotoAlbum;
    }

    public Foto findFotoById(Long fotoId) {
        if (fotoId == null) return null;
        String sql = "SELECT * FROM foto WHERE ID = ?";

        Foto foto = null;
        try {
            foto = (Foto) jdbcTemplate.queryForObject(sql, new Object[]{fotoId}, new BeanPropertyRowMapper(Foto.class));
        } catch (Exception e) {
            return null;
        }
        return foto;
    }

    public List<Foto> findAllFotos(Long albumId) {
        String sql = "SELECT * FROM foto WHERE FOTO_ALBUM_ID = " + albumId;
        List<Foto> fotos = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Foto.class));
        return fotos;
    }
    public List<Foto> findAllFotos() {
        String sql = "SELECT * FROM foto";
        List<Foto> fotos = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Foto.class));
        return fotos;
    }

    public void addFoto(Long albumId, Foto foto) {
        if (albumId == null) return;
        if (foto == null || foto.getId() == null) return;
        FotoAlbum fotoAlbum = findById(albumId);
        fotoAlbum.getFotos().add(foto);

    }

    public void updateAlbumWithFoto(FotoAlbum fotoAlbum, Foto foto) {
        if (fotoAlbum == null) return;
        if (foto == null || foto.getId() == null) return;
        Foto originalFoto = findFotoById(foto.getId());
        originalFoto.setFotoAlbum(fotoAlbum);
        jdbcTemplate.update("UPDATE foto SET FOTO_ALBUM_ID=?, UPDATED=? WHERE ID=?",
                originalFoto.getFotoAlbum(),
                new Date(),
                originalFoto.getId());
    }
    public FotoAlbum insertAlbum(){
        String date = DateUtils.convertToStringByFormat(new Date(), new SimpleDateFormat(pattern));
        String sql = "INSERT INTO foto_album (UPDATED, CREATED) VALUES ('"+date+"','"+ date+ "') ";
        jdbcTemplate.query(sql, new BeanPropertyRowMapper(FotoAlbum.class));
      return findLastFotoAlbum();
    }
    public FotoAlbum findLastFotoAlbum() {
        String sql = "SELECT * FROM foto_album ORDER BY ID DESC LIMIT 1";
        FotoAlbum fotoAlbum;
        try {
            fotoAlbum = (FotoAlbum) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(FotoAlbum.class));
        } catch (Exception e) {
            return null;
        }
        return fotoAlbum;
    }

    public void insertFoto(Foto foto){
        String date = DateUtils.convertToStringByFormat(new Date(), new SimpleDateFormat(pattern));
        String sql = "INSERT INTO foto (FOTO_NAME, FOTO_ALBUM_ID, UPDATED, CREATED) VALUES ("+foto.getFotoName()+", "+foto.getFotoAlbum().getId()+", '"+date+"', '"+ date+ "') ";
        jdbcTemplate.query(sql, new BeanPropertyRowMapper(Foto.class));
    }

}
