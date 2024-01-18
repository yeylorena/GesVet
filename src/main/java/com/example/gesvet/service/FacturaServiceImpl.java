
package com.example.gesvet.service;

import com.example.gesvet.models.Factura;
import com.example.gesvet.repository.IFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaServiceImpl implements IFacturaService{

    @Autowired
    private IFacturaRepository facturaRepository;
    
    @Override
    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }
    
}
