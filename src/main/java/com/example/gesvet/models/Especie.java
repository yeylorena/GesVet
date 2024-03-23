package com.example.gesvet.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Especies")
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private boolean activo = true;
    private String descripcion;
    // Relación con Raza
    @OneToMany(mappedBy = "especie")
   // @JsonIgnoreProperties("especie")
    private List<Raza> razas;

    public Especie() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public List<Raza> getRazas() {
        return razas;
    }

    public void setRazas(List<Raza> razas) {
        this.razas = razas;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Especie(Integer id, String nombre, String descripcion, List<Raza> razas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.razas = razas;
        this.activo = true;
    }

    @Override
    public String toString() {
        return "Especie{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", razas=" + razas + '}';
    }

}
