package com.example.gesvet.service;

import com.example.gesvet.models.DetalleFactura;
import com.example.gesvet.repository.IDetalleFactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleFactServiceImpl implements IDetalleFactService {

    @Autowired
    private IDetalleFactRepository detalleFactRepository;

    @Override
    public DetalleFactura save(DetalleFactura detalleFactura) {
        return detalleFactRepository.save(detalleFactura);
    }

}
