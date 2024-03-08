package com.example.gesvet.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String role;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String acercade;
    private String imagen;
    private boolean activo;
    private static final String IMAGEN_PREDETERMINADA = "usuario.png";

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Mascota> mascotas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<citaRapida> citasRapidas;

    public List<citaRapida> getCitasRapidas() {
        return citasRapidas;
    }

    public void setCitasRapidas(List<citaRapida> citasRapidas) {
        this.citasRapidas = citasRapidas;
    }

    // Resto de tu código...
    // Agrega este método para obtener la lista de mascotas del usuario
    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAcercade() {
        return acercade;
    }

    public void setAcercade(String acercade) {
        this.acercade = acercade;
    }

    public User(String username, String password, String nombre, String apellido, String direccion, String telefono, String acercade, String imagen, String role) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.acercade = acercade;
        this.imagen = (imagen != null && !imagen.isEmpty()) ? imagen : IMAGEN_PREDETERMINADA;
        // Set default role if not provided
        this.role = role != null ? role : "USER";
        this.activo = true;

    }

    public User() {
    }

}
