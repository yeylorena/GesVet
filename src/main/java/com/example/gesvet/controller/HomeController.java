
package com.example.gesvet.controller;

import com.example.gesvet.models.DetalleFactura;
import com.example.gesvet.models.Factura;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.Usuario;
import com.example.gesvet.service.IDetalleFactService;
import com.example.gesvet.service.IFacturaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.gesvet.service.IProductoService;
import com.example.gesvet.service.IUsuarioService;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    
    @Autowired
    private IProductoService productoService;
    
    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    private IFacturaService facturaService;
    
    @Autowired
    private IDetalleFactService detalleFactService; 
    
    //Array para almacenar los detalles de la factura
    List<DetalleFactura>detalles = new ArrayList<DetalleFactura>();
    
    //datos de la factura
    Factura factura = new Factura();
    
    @GetMapping("")
    public String home(Model model){
        
        model.addAttribute("productos", productoService.findAll());
        
        return "usuario/Home";
    }
    
    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model){
        
        Productos producto = new Productos();
        Optional<Productos> productoOptional = productoService.get(id);
        producto = productoOptional.get();
        
        model.addAttribute("producto", producto);
        
        return "usuario/Producto_Home";
    }
    
    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model){
        
        DetalleFactura detalleFactura = new DetalleFactura();
        Productos producto = new Productos();
        double sumaTotal = 0;
        
        Optional<Productos> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();
        
        detalleFactura.setCantidad(cantidad);
        detalleFactura.setPrecio(producto.getPrecio());
        detalleFactura.setNombre(producto.getNombre());
        detalleFactura.setTotal(producto.getPrecio() * cantidad);
        detalleFactura.setProductos(producto);
        
        //Validar que los productos se agreguen una vez
        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProductos().getId()==idProducto);
        
        if(!ingresado){
            detalles.add(detalleFactura);
        }
        
        //Función anónima para calcular suma total
        sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
        
        factura.setTotal(sumaTotal);
        
        model.addAttribute("cart", detalles);
        model.addAttribute("factura", factura);
        
        return "usuario/Carrito";
    }
    
    //Quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {

		// lista nueva de prodcutos
		List<DetalleFactura> detalleNueva = new ArrayList<DetalleFactura>();

		for (DetalleFactura detalleFactura : detalles) {
			if (detalleFactura.getProductos().getId() != id) {
				detalleNueva.add(detalleFactura);
			}
		}

		// poner la nueva lista con los productos restantes
		detalles = detalleNueva;

		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		factura.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("factura", factura);

		return "usuario/Carrito";
	}
        
    @GetMapping("/getCart")
    public String getCart(Model model){
        
        model.addAttribute("cart", detalles);
	model.addAttribute("factura", factura);
        
        return "/usuario/Carrito";
    }
    
    @GetMapping("/factura")
    public String factura(Model model){
        
        Usuario usuario = usuarioService.findById(2).get();
        
        model.addAttribute("cart", detalles);
	model.addAttribute("factura", factura);
        model.addAttribute("usuario", usuario);
        
        return "usuario/ResumenFactura";
    }
    
    @GetMapping("saveFact")
    public String saveFact(){
        
        Date fecha= new Date();
        
        //Se guarda la fecha de la factura
        factura.setFecha(fecha);
        
        //Se guarda el número de la factura
        factura.setNumero(facturaService.generarNumFactura());
        
        //Usuario
        Usuario usuario = usuarioService.findById(2).get();
        
        factura.setUsuario(usuario);
        
        //Se guardan los datos de la factura
        facturaService.save(factura);
        
        //Guardar detalles
        for(DetalleFactura dt:detalles){
            dt.setFactura(factura);
            detalleFactService.save(dt);
        }
        
        //limpiar lista  y factura
        factura = new Factura();
        detalles.clear();
       
        return "redirect:/";
    }
    
    @PostMapping("/buscar")
    public String buscarProducto(@RequestParam String palabra, Model model){
        
        //Filtro para retornar un nombre utilizando un filtro que busca en la lista de productos. Retorna un string y se pasa a una lista
        List<Productos> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(palabra)).collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "usuario/home";
    }

    
}
