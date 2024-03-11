package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "usuarioVentas")
public class UsuarioVentas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String documento;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;

    public UsuarioVentas(Long id, String documento, String nombre, String apellido, String direccion, String telefono, String email) {
        super();
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public UsuarioVentas() {
    }

     @OneToMany(mappedBy = "usuarioventas")
    private List<Factura> facturasuser;

    public List<Factura> getFacturasuser() {
        return facturasuser;
    }

    public void setFacturasuser(List<Factura> facturasuser) {
        this.facturasuser = facturasuser;
    }

}
