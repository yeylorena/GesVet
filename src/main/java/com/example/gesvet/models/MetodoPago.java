package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "metodoPago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String cuenta;
    private boolean activo;
     private boolean activos = true;

    public boolean isActivos() {
        return activos;
    }

    public void setActivos(boolean activos) {
        this.activos = activos;
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

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public MetodoPago(Integer id, String nombre, String cuenta, String imagen, boolean activo) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.cuenta = cuenta;

        this.activo = activo;
    }

    @Override
    public String toString() {
        return "MetodoPago{" + "id=" + id + ", nombre=" + nombre + ", cuenta=" + cuenta + ", activo=" + activo + '}';
    }

    @OneToMany(mappedBy = "metodoPago")
    private List<Factura> factura;

    public List<Factura> getFactura() {
        return factura;
    }

    public void setFactura(List<Factura> factura) {
        this.factura = factura;
    }


  public MetodoPago(){
      
  } 
   @ManyToOne
    private User usuario;

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;

    }
}
