package com.adonis.data.vehicles.img;

import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "foto", schema = "")
@Getter
@Setter
@NoArgsConstructor
@Cacheable(value = false)
public class Foto extends Audit{

    @Column(name = "FOTO_NAME", length = 1000)
    private String fotoName;

    @ManyToOne
    @JoinColumn(name = "FOTO_ALBUM_ID")
    private FotoAlbum fotoAlbum;
}
