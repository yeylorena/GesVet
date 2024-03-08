package com.example.gesvet.service;

import com.example.gesvet.models.MetodoPago;
import com.example.gesvet.repository.MetodoPagoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetodoPagoServiceImpl implements IMetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodopagorepository;

    @Override
    public MetodoPago save(MetodoPago metodopago) {
        return metodopagorepository.save(metodopago);
    }

    @Override
    public Optional<MetodoPago> get(Integer id) {
        return metodopagorepository.findById(id);

    }

    @Override
    public void update(MetodoPago metodopago) {
        metodopagorepository.save(metodopago);
    }

    @Override
    public void delete(Integer id) {

        metodopagorepository.deleteById(id);
    }

    @Override
    public List<MetodoPago> findAll() {
        return metodopagorepository.findAll();
    }

    @Override
    public MetodoPago findById(Integer id) {
        Optional<MetodoPago> optionalMetodoPago = metodopagorepository.findById(id);
        return optionalMetodoPago.orElse(null);
    }
}
