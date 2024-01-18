
package com.example.gesvet.controller;

import com.example.gesvet.models.DetalleFactura;
import com.example.gesvet.models.Factura;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.Usuario;
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

@Controller
@RequestMapping("/")
public class HomeController {
    
    @Autowired
    private IProductoService productoService;
    
    @Autowired
    private IUsuarioService usuarioService;
    
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
    
}
