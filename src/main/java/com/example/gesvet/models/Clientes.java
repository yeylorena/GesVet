package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombres;
    private String apellidos;
    private int documento;
    private String telefono;
    private String email;
    private String direccion;

// Mapeo de la relación con Mascota
    /* @OneToMany(mappedBy = "clientes")
    private List<Mascota> mascotas;*/
    public Clientes() {
    }

    public Clientes(Integer id, String nombres, String apellidos, int documento, String telefono, String email, String direccion, String mascotas) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Clientes{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", documento=" + documento + ", telefono=" + telefono + ", email=" + email + ", direccion=" + direccion + '}';
    }

}
