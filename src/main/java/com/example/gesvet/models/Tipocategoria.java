package com.example.gesvet.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tipocategorias")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tipocategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private boolean activo;

    @ManyToOne()
    @JsonIgnore
    private User usuario;

    @OneToMany(mappedBy = "tipocategoria")
    @JsonIgnoreProperties("tipocategoria")
    private List<Categorias> categorias;

    public Tipocategoria() {

    }

    public Tipocategoria(Integer id, String nombre, boolean activo, User usuario) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.usuario = usuario;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Categorias> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categorias> categorias) {
        this.categorias = categorias;
    }

    @Override
    public String toString() {
        return "Tipocategoria{" + "id=" + id + ", nombre=" + nombre + ", activo=" + activo + ", usuario=" + usuario + ", categorias=" + categorias + '}';
    }

}
