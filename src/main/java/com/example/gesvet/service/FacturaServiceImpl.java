
package com.example.gesvet.service;

import com.example.gesvet.models.Factura;
import com.example.gesvet.repository.IFacturaRepository;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }
    
    public String generarNumFactura(){
        
        //Se crean dos variables. Uno para int y otro para string
        int numero = 0;
        String numeroConcatenado = "";
        
        //Se crea lista para almacenar facturas
        List<Factura> facturas = findAll();
        
        //Se crea lista para almacenar números
        List<Integer> numeros = new ArrayList<Integer>();
        
        //Método para recorrer lista factura y adicionar el numero de cada una en la lista numeros, haciendo el debido parseo
        facturas.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
        
        if(facturas.isEmpty()){
            numero=1;
        }else{
            //Método para extraer el número mayor de factura y almacenarlo en la lista números
            numero = numeros.stream().max(Integer::compare).get();
            
            //El último número se incrementa el valor
            numero++;
        }
        
        if (numero<10) { //0000001000
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}
        
        return numeroConcatenado;
    }
    
}
