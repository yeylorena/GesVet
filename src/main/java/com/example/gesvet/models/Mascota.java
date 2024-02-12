/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author sofia
 */
@Entity
@Table(name = "Mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imagen;
    private String color;
    private String edad;
    private String genero;
    private String detalles;

    // Mapeo de Especie
    @ManyToOne
    private Especie especie;

    // Mapeo de Clientes
    @ManyToOne
    private Clientes clientes;

    @ManyToOne
    @JoinColumn(name = "raza_id")
    private Raza raza;

    public Mascota(int par, String string, String string1, String string2, String string3, String string4, String string5) {
    }

    public Mascota(Integer id, String imagen, String color, String edad, String genero, String detalles, Especie especie, Clientes clientes, Raza raza) {
        this.id = id;
        this.imagen = imagen;
        this.color = color;
        this.edad = edad;
        this.genero = genero;
        this.detalles = detalles;
        this.especie = especie;
        this.clientes = clientes;
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

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    @Override
    public String toString() {
        return "Mascota{" + "id=" + id + ", imagen=" + imagen + ", color=" + color + ", edad=" + edad + ", genero=" + genero + ", detalles=" + detalles + '}';
    }

}
