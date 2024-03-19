package com.example.gesvet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "citasAgendar")
public class agendarcitaAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String servicio;
    private String nombreMascota;
    private String nombreDueño;
    private String colorMascota;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String estado;
    private String formattedFecha; // Campo para almacenar la fecha y hora formateada
    private String formattedFechaFin; // Campo para almacenar la fecha y hora formateada
    private String veterinarioCita;
    private String nombreVeterinario;

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "veterinario_id")
    private User veterinario;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private ServiciosUser servicioo;

    // Mapeo de Especie
    @ManyToOne
    @JoinColumn(name = "especie_id")
    private Especie especie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getVeterinarioCita() {
        return veterinarioCita;
    }

    public void setVeterinarioCita(String veterinarioCita) {
        this.veterinarioCita = veterinarioCita;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public User getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(User veterinario) {
        this.veterinario = veterinario;
    }

    public ServiciosUser getServicioo() {
        return servicioo;
    }

    public void setServicioo(ServiciosUser servicioo) {
        this.servicioo = servicioo;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public agendarcitaAdmin(Integer id, String servicio, String nombreMascota, String nombreDueño, String colorMascota, LocalDateTime inicio, LocalDateTime fin, String estado, String formattedFecha, String formattedFechaFin, String veterinarioCita, String nombreVeterinario, Mascota mascota, User usuario, User veterinario, ServiciosUser servicioo, Especie especie) {
        this.id = id;
        this.servicio = servicio;
        this.nombreMascota = nombreMascota;
        this.nombreDueño = nombreDueño;
        this.colorMascota = colorMascota;
        this.inicio = inicio;
        this.fin = fin;
        this.estado = estado;
        this.formattedFecha = formattedFecha;
        this.formattedFechaFin = formattedFechaFin;
        this.veterinarioCita = veterinarioCita;
        this.nombreVeterinario = nombreVeterinario;
        this.mascota = mascota;
        this.usuario = usuario;
        this.veterinario = veterinario;
        this.servicioo = servicioo;
        this.especie = especie;
    }

    @Override
    public String toString() {
        return "agendarcitaAdmin{" + "id=" + id + ", servicio=" + servicio + ", nombreMascota=" + nombreMascota + ", nombreDue\u00f1o=" + nombreDueño + ", colorMascota=" + colorMascota + ", inicio=" + inicio + ", fin=" + fin + ", estado=" + estado + ", formattedFecha=" + formattedFecha + ", formattedFechaFin=" + formattedFechaFin + ", veterinarioCita=" + veterinarioCita + ", nombreVeterinario=" + nombreVeterinario + ", mascota=" + mascota + ", usuario=" + usuario + ", veterinario=" + veterinario + ", servicioo=" + servicioo + ", especie=" + especie + '}';
    }

}
