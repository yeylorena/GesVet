package com.example.gesvet.controller;

import com.example.gesvet.models.Proveedor;
import com.example.gesvet.service.ProveedorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/compras")
public class ProveedorControlador {

    @Autowired //inyectar servicio
    private ProveedorServicio proveedorServicio;

    @GetMapping("")
    public String paginaCompras(Model modelo) {
        List<Proveedor> listaProveedores = proveedorServicio.listAll();
        modelo.addAttribute("listaProveedores", listaProveedores);

        return "Gestion_Compras";
    }

    @GetMapping("/nuevo")
    public String mostrarGuardarProveedor(Model modelo) {
        Proveedor proveedor = new Proveedor();
        modelo.addAttribute("proveedor", proveedor);

        return "Agregar_Proveedor";
    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public String guardarProveedor(@ModelAttribute("proveedor") Proveedor proveedor) {
        proveedorServicio.save(proveedor);
        return "redirect:/compras";
    }

}
