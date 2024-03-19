package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "citasRapidas")
public class citaRapida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCita;
    private String nombreMascota;
    private String nombreDueño;
    private String colorMascota;
    private double precio;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String estado;
    private String formattedFecha; // Campo para almacenar la fecha y hora formateada
    private String formattedFechaFin; // Campo para almacenar la fecha y hora formateada
    private String veterinarioCita;
    private String nombreVeterinario;

    // Mapeo de Especie
    @ManyToOne
    @JoinColumn(name = "especie_id")
    private Especie especie;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private ServiciosUser servicio;

    // Relación con Mascota
    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public String getVeterinarioCita() {
        return veterinarioCita;
    }

    public void setVeterinarioCita(String veterinarioCita) {
        this.veterinarioCita = veterinarioCita;
    }

    public citaRapida() {
        this.estado = "pendiente"; // Establecer el estado inicial como pendiente al crear la cita
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCita() {
        return nombreCita;
    }

    public void setNombreCita(String nombreCita) {
        this.nombreCita = nombreCita;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getNombreDueño() {
        return nombreDueño;
    }

    public void setNombreDueño(String nombreDueño) {
        this.nombreDueño = nombreDueño;
    }

    public String getColorMascota() {
        return colorMascota;
    }

    public void setColorMascota(String colorMascota) {
        this.colorMascota = colorMascota;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public String getFormattedFecha() {
        return formattedFecha;
    }

    public void setFormattedFecha(String formattedFecha) {
        this.formattedFecha = formattedFecha;
    }

    public String getFormattedFechaFin() {
        return formattedFechaFin;
    }

    public void setFormattedFechaFin(String formattedFechaFin) {
        this.formattedFechaFin = formattedFechaFin;
    }

    public ServiciosUser getServicio() {
        return servicio;
    }

    public void setServicio(ServiciosUser servicio) {
        this.servicio = servicio;
    }

    public citaRapida(Long id, String nombreCita, String nombreMascota, String nombreDueño, String colorMascota, double precio, LocalDateTime inicio, LocalDateTime fin, String estado, String formattedFecha, String formattedFechaFin, String veterinarioCita, String nombreVeterinario, Especie especie, User usuario, ServiciosUser servicio, Mascota mascota) {
        this.id = id;
        this.nombreCita = nombreCita;
        this.nombreMascota = nombreMascota;
        this.nombreDueño = nombreDueño;
        this.colorMascota = colorMascota;
        this.precio = precio;
        this.inicio = inicio;
        this.fin = fin;
        this.estado = estado;
        this.formattedFecha = formattedFecha;
        this.formattedFechaFin = formattedFechaFin;
        this.veterinarioCita = veterinarioCita;
        this.nombreVeterinario = nombreVeterinario;
        this.especie = especie;
        this.usuario = usuario;
        this.servicio = servicio;
        this.mascota = mascota;
    }

    @Override
    public String toString() {
        return "citaRapida{" + "id=" + id + ", nombreCita=" + nombreCita + ", nombreMascota=" + nombreMascota + ", nombreDue\u00f1o=" + nombreDueño + ", colorMascota=" + colorMascota + ", precio=" + precio + ", inicio=" + inicio + ", fin=" + fin + ", estado=" + estado + ", formattedFecha=" + formattedFecha + ", formattedFechaFin=" + formattedFechaFin + ", veterinarioCita=" + veterinarioCita + ", nombreVeterinario=" + nombreVeterinario + ", especie=" + especie + ", usuario=" + usuario + ", servicio=" + servicio + ", mascota=" + mascota + '}';
    }

}
