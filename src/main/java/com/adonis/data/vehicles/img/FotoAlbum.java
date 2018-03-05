package com.adonis.data.vehicles.img;

import com.adonis.data.Audit;
import com.adonis.data.persons.Address;
import com.adonis.data.vehicles.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "foto_album", schema = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(value = false)
public class FotoAlbum extends Audit{

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fotoAlbum")
    private List<Foto> fotos;


}
