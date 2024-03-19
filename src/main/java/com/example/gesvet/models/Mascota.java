package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "Mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String imagen;
    private String color;
    private String edad;
    private String tiempo;
    private String genero;
    private String detalles;

    // Mapeo de Especie
    @ManyToOne
    @JoinColumn(name = "especie_id")
    private Especie especie;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
    // Mapeo de razas
    @ManyToOne
    @JoinColumn(name = "raza_id")
    private Raza raza;

    public Mascota(int par, String string, String string1, String string2, String string3, String string4, String string5) {
    }

    public Mascota() {
    }

    public Mascota(Integer id, String nombre, String imagen, String color, String edad, String tiempo, String genero, String detalles, Especie especie, Raza raza) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.color = color;
        this.edad = edad;
        this.tiempo = tiempo;
        this.genero = genero;
        this.detalles = detalles;
        this.especie = especie;
        this.raza = raza;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Mascota{" + "id=" + id + ", nombre=" + nombre + ", imagen=" + imagen + ", color=" + color + ", edad=" + edad + ", tiempo=" + tiempo + ", genero=" + genero + ", detalles=" + detalles + ", especie=" + especie + ", raza=" + raza + '}';
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

}
