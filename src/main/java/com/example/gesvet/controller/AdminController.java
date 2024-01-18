
package com.example.gesvet.controller;

import com.example.gesvet.models.Productos;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.gesvet.service.IProductoService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private IProductoService productoService;
    
    @GetMapping("")
    public String home(Model model){
        
        List<Productos> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        
        return "administrador/Ver_Productos";
    }
    
}
