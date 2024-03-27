package com.example.gesvet.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private boolean activo;
    private String imagen;

    @ManyToOne()
    @JsonIgnore
    private User usuario;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<ServiciosUser> serviciosusers;

    @ManyToOne
    @JoinColumn(name = "tipocategoria_id")
    @JsonIgnoreProperties("categorias")
    private Tipocategoria tipocategoria;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Productos> productos;

    public Categorias() {

    }

    public Categorias(Integer id, String nombre, String tipo, String descripcion, boolean activo, User usuario, String imagen) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.usuario = usuario;
        this.imagen = imagen;
    }

    public List<Productos> getProductos() {
        return productos;
    }

    public void setProductos(List<Productos> productos) {
        this.productos = productos;
    }

    public Tipocategoria getTipocategoria() {
        return tipocategoria;
    }

    public void setTipocategoria(Tipocategoria tipocategoria) {
        this.tipocategoria = tipocategoria;
    }

    public List<ServiciosUser> getServiciosusers() {
        return serviciosusers;
    }

    public void setServiciosusers(List<ServiciosUser> serviciosusers) {
        this.serviciosusers = serviciosusers;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
        return "Categorias{" + "id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", descripcion=" + descripcion + ", activo=" + activo + ", imagen=" + imagen + ", usuario=" + usuario + ", productos=" + productos + ", tipocategoria=" + tipocategoria + ", serviciosusers=" + serviciosusers + '}';
    }

}
