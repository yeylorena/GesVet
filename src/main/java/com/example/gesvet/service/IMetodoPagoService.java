package com.example.gesvet.service;

import com.example.gesvet.models.MetodoPago;
import java.util.List;
import java.util.Optional;

public interface IMetodoPagoService {

    MetodoPago save(MetodoPago metodopago);

    Optional<MetodoPago> get(Integer id);

    void update(MetodoPago metodopago);

    void delete(Integer id);

    List<MetodoPago> findAll();

    MetodoPago findById(Integer id);

}
