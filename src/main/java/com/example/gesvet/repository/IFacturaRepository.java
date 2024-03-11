package com.example.gesvet.repository;

import com.example.gesvet.models.Factura;
import com.example.gesvet.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IFacturaRepository extends JpaRepository<Factura, Integer> {

    List<Factura> findByUsuario(User usuario);

    @Query("SELECT f FROM Factura f WHERE f.usuario.role = :role")
    List<Factura> findByUserRole(@Param("role") String role);
    
   @Query(value = "SELECT p.id, p.nombre,  CAST(ROUND(SUM(df.cantidad)) AS SIGNED) as totalVentas, SUM(df.cantidad * p.precio) as ventaTotal " +
               "FROM detalles df " +
               "JOIN facturas f ON df.factura_id = f.id " +
               "JOIN productos p ON df.productos_id = p.id " +
               "WHERE f.estado_pago = 'aprobado' " +
               "GROUP BY p.id, p.nombre " +
               "ORDER BY totalVentas DESC " +
               "LIMIT 10", nativeQuery = true)
List<Object[]> findTopProductosMasVendidos();







}
