package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private boolean activo;
    @ManyToOne()
    private User usuario;

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Categorias(Integer id, String nombre, String tipo, String descripcion, boolean activo, User usuario) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.activo = activo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Categorias{" + "id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", descripcion=" + descripcion + ", activo=" + activo + ", usuario=" + usuario + ", productos=" + productos + ", tipocategoria=" + tipocategoria + '}';
    }

    public Categorias() {

    }
    @OneToMany(mappedBy = "categoria")
    private List<Productos> productos;

    public List<Productos> getProductos() {
        return productos;
    }

    public void setProductos(List<Productos> productos) {
        this.productos = productos;
    }

    @ManyToOne
    @JoinColumn(name = "tipocategoria_id")
    private Tipocategoria tipocategoria;

    public Tipocategoria getTipocategoria() {
        return tipocategoria;
    }

    public void setTipocategoria(Tipocategoria tipocategoria) {
        this.tipocategoria = tipocategoria;
    }

    @OneToMany(mappedBy = "categoria")
    private List<ServiciosUser> serviciosusers;

    public List<ServiciosUser> getServiciosusers() {
        return serviciosusers;
    }

    public void setServiciosusers(List<ServiciosUser> serviciosusers) {
        this.serviciosusers = serviciosusers;
    }
}
