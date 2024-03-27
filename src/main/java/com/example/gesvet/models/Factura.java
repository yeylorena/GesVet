package com.example.gesvet.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numero;
    private Date fecha;
    private double total;
    private String estadoPago;
    private String imagen;
    private String estadoEnvio;
    private String role;
    private boolean procesada;

    @ManyToOne
    @JoinColumn(name = "metodo_pago_id")
    private MetodoPago metodoPago;

    @ManyToOne
    @JsonIgnore
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "usuarioventas_id")
    @JsonIgnore
    private UsuarioVentas usuarioventas;

    @OneToMany(mappedBy = "factura")
    @JsonIgnore
    private List<DetalleFactura> listaDetalles;

    public Factura() {
    }

    public Factura(Integer id, String numero, Date fecha, double total, String estadoPago, String imagen, String estadoEnvio, String role, User usuario) {
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.total = total;
        this.estadoPago = estadoPago;
        this.imagen = imagen;
        this.estadoEnvio = estadoEnvio;
        this.role = role;
        this.usuario = usuario;
    }

    public boolean isProcesada() {
        return procesada;
    }

    public void setProcesada(boolean procesada) {
        this.procesada = procesada;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUser() {
        return usuario;
    }

    public void setUser(User usuario) {
        this.usuario = usuario;
    }

    public List<DetalleFactura> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleFactura> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public UsuarioVentas getUsuarioventas() {
        return usuarioventas;
    }

    public void setUsuarioventas(UsuarioVentas usuarioventas) {
        this.usuarioventas = usuarioventas;
    }

    @Override
    public String toString() {
        return "Factura{" + "id=" + id + ", numero=" + numero + ", fecha=" + fecha + ", total=" + total + ", estadoPago=" + estadoPago + ", imagen=" + imagen + ", estadoEnvio=" + estadoEnvio + ", role=" + role + ", usuario=" + usuario + '}';
    }
}
