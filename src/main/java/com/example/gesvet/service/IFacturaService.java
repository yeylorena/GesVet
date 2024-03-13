package com.example.gesvet.service;

import com.example.gesvet.models.Factura;
import com.example.gesvet.models.User;
import java.util.List;
import java.util.Optional;

public interface IFacturaService {

    List<Factura> findAll();

    Optional<Factura> findById(Integer id);

    Factura save(Factura factura);

    String generarNumFactura();

    List<Factura> findByUsuario(User usuario);


    List<Factura> findByUser_Role(String role);

    public void update(Factura factura);

    void delete(Factura factura);

    List<Object[]> obtenerTopProductosMasVendidos();

    /*
  List<Object[]> findTotalVentasPorProducto();
  Double findSumaTotalVentas();
     */
    List<Factura> findByUsuarioAndEstadoPago(User usuario, String estadoPago);

    //corresponde a las compras del usuario
    List<Object[]> findDetallesCompras();
    
    //corresponde a las compras del usuario
    List<Object[]> findDetallesVentas();
//corresponde a la facturacio diaria
 List<Object[]> findDetallesFactura();
}
