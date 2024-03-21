package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.DetalleFactura;
import com.example.gesvet.models.Factura;
import com.example.gesvet.models.MetodoPago;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.User;
import com.example.gesvet.repository.UserRepository;
import com.example.gesvet.service.ICategoriasService;
import com.example.gesvet.service.IDetalleFactService;
import com.example.gesvet.service.IFacturaService;
import com.example.gesvet.service.IMetodoPagoService;
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
import com.example.gesvet.service.UploadFileService;
import com.example.gesvet.service.UserService;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class HomeController {
    
    @Autowired
    ICategoriasService categoriasservice;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private UserRepository usuarioService;

    @Autowired
    private IFacturaService facturaService;

    @Autowired
    private IDetalleFactService detalleFactService;

    @Autowired
    private UploadFileService upload;

    @Autowired
    private IMetodoPagoService metodopagoservice;

    //Array para almacenar los detalles de la factura
    List<DetalleFactura> detalles = new ArrayList<DetalleFactura>();

    //datos de la factura
    Factura factura = new Factura();
    
    @GetMapping("verhome")
    public String homecategoria(Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        
        // Obtén las categorías asociadas al tipo "Producto"
        List<Categorias> categoriasProducto = categoriasservice.findByTipoCategoria("Producto");

        model.addAttribute("categoriasProducto", categoriasProducto);

        model.addAttribute("userDto", userDto);
       

        return "usuario/Homecategoria";
    }
    
    @GetMapping("productohomes/{id}")
public String productoHomes(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
    // Obtener los detalles del usuario actual
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    model.addAttribute("userdetail", userDetails);

    // Obtener el nombre de usuario actual
    String username = authentication.getName();

    // Buscar al usuario por su nombre de usuario
    User user = userService.findByUsername(username);

    // Crear un objeto UserDto
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setNombre(user.getNombre());
    userDto.setApellido(user.getApellido());
    userDto.setDireccion(user.getDireccion());
    userDto.setTelefono(user.getTelefono());
    userDto.setRole(user.getRole());
    userDto.setAcercade(user.getAcercade());
    userDto.setImagen("/images/" + user.getImagen());

    // Verificar si los datos personales del usuario están completos
    if (!userService.usuarioDatosPersonalesCompletos(user)) {
        // Si los datos personales no están completos, agrega el atributo para mostrar la alerta
        model.addAttribute("mostrarAlerta", true);
    } else {
        // Obtener la categoría por su ID
        Categorias categoria = categoriasservice.findById(id);

        // Obtener los productos asociados a la categoría
        List<Productos> productos = productoService.findByCategoria(categoria);

        model.addAttribute("categoria", categoria);
        model.addAttribute("productos", productos);
    }

    model.addAttribute("userDto", userDto);

    return "usuario/Home";
}


    @GetMapping("verproducto")
    public String home(Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        model.addAttribute("userDto", userDto);
        model.addAttribute("productos", productoService.findAll());

        return "usuario/Home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        Productos producto = new Productos();
        Optional<Productos> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("userDto", userDto);
        model.addAttribute("producto", producto);

        return "usuario/Producto_Home";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        DetalleFactura detalleFactura = new DetalleFactura();
        Productos producto = new Productos();
        double sumaTotal = 0;

        Optional<Productos> optionalProducto = productoService.get(id);
        producto = optionalProducto.orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product is already in the cart
        Optional<DetalleFactura> existingDetail = detalles.stream()
                .filter(p -> p.getProductos().getId().equals(id))
                .findFirst();

        if (existingDetail.isPresent()) {
            // Update the quantity if the product is already in the cart
            DetalleFactura existingDetalle = existingDetail.get();

            // Check if adding the new quantity exceeds the available quantity
            if (existingDetalle.getCantidad() + cantidad > producto.getCantidad()) {

                redirectAttributes.addFlashAttribute("error", true);
                return "redirect:/productohome/" + id;
            }

            existingDetalle.setCantidad(existingDetalle.getCantidad() + cantidad);
            existingDetalle.setTotal(existingDetalle.getPrecio() * existingDetalle.getCantidad());
        } else {
            // Check if adding the new quantity exceeds the available quantity
            if (cantidad > producto.getCantidad()) {

                redirectAttributes.addFlashAttribute("error", true);
                return "redirect:/productohome/" + id;
            }

            detalleFactura.setCantidad(cantidad);
            detalleFactura.setPrecio(producto.getPrecio());
            detalleFactura.setNombre(producto.getNombre());
            detalleFactura.setTotal(producto.getPrecio() * cantidad);
            detalleFactura.setProductos(producto);

            detalles.add(detalleFactura);
        }

        // Calculate the total sum
        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        factura.setTotal(sumaTotal);

        model.addAttribute("userDto", userDto);
        model.addAttribute("cart", detalles);
        model.addAttribute("factura", factura);

        return "usuario/Carrito";
    }
@PostMapping("/cart/update")
public String updateCart(@RequestParam Integer id, @RequestParam Integer newQuantity, Model model, Authentication authentication, Principal principal,RedirectAttributes redirectAttributes) {
    // Obtener los detalles del usuario actual
UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
model.addAttribute("userdetail", userDetails);

    // Obtener el usuario actual
    String username = authentication.getName();
    User user = userService.findByUsername(username);

    // Crear un objeto UserDto
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setNombre(user.getNombre());
    userDto.setApellido(user.getApellido());
    userDto.setDireccion(user.getDireccion());
    userDto.setTelefono(user.getTelefono());
    userDto.setRole(user.getRole());
    userDto.setAcercade(user.getAcercade());
    userDto.setImagen("/images/" + user.getImagen());
    
     // Obtener el producto de la base de datos
    Optional<Productos> optionalProducto = productoService.get(id);
    Productos producto = optionalProducto.orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    // Verificar si la nueva cantidad excede la cantidad disponible en la base de datos
    if (newQuantity > producto.getCantidad()) {
        int cantidadDisponible = producto.getCantidad(); // Obtener la cantidad disponible del producto
        redirectAttributes.addFlashAttribute("error", "La cantidad seleccionada excede la cantidad disponible en el inventario (" + cantidadDisponible + " disponibles).");
        return "redirect:/getCart";
    }

    // Buscar el detalle correspondiente en la lista de detalles del carrito
    DetalleFactura detalle = detalles.stream()
            .filter(df -> df.getProductos().getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Detalle de factura no encontrado"));

    // Actualizar la cantidad del producto
    detalle.setCantidad(newQuantity);

    // Recalcular el total del detalle
    detalle.setTotal(detalle.getPrecio() * newQuantity);

    // Recalcular el total de la factura
    double sumaTotal = detalles.stream().mapToDouble(df -> df.getTotal()).sum();
    factura.setTotal(sumaTotal);

    // Actualizar el modelo con los datos actualizados
    model.addAttribute("cart", detalles);
    model.addAttribute("factura", factura);
    model.addAttribute("userDto", userDto);

    // Devolver la vista del carrito
    return "usuario/Carrito";
}

    //Quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

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
        model.addAttribute("userDto", userDto);
        model.addAttribute("cart", detalles);
        model.addAttribute("factura", factura);

        return "usuario/Carrito";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        model.addAttribute("userDto", userDto);
        model.addAttribute("cart", detalles);
        model.addAttribute("factura", factura);

        return "/usuario/Carrito";
    }

    @GetMapping("/factura")
    public String factura(Model model, Authentication authentication, Principal principal,RedirectAttributes redirectAttributes) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        List<MetodoPago> metodosDePago = metodopagoservice.findAll(); // Reemplaza esto con tu lógica para obtener los métodos de pago
        model.addAttribute("metodosDePago", metodosDePago);
        model.addAttribute("userDto", userDto);
        model.addAttribute("cart", detalles);
        model.addAttribute("factura", factura);
        model.addAttribute("usuario", user);

        return "usuario/ResumenFactura";
    }

    @PostMapping("saveFact")
    public String saveFact(Model model, Authentication authentication, Principal principal,@RequestParam("img") MultipartFile file,@RequestParam("metodoPago") Integer metodoPagoId,RedirectAttributes redirectAttributes) throws IOException  {


        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        // Aquí puedes usar el ID recibido para buscar el Método de Pago seleccionado
        MetodoPago metodoPagoSeleccionado = metodopagoservice.findById(metodoPagoId);
        // Asignar el Método de Pago a la Factura
        factura.setMetodoPago(metodoPagoSeleccionado);

        // Añadir el role del usuario a la factura
        factura.setRole(user.getRole());

        Date fecha = new Date();

        //Se guarda la fecha de la factura
        factura.setFecha(fecha);

        //Se guarda el número de la factura
        factura.setNumero(facturaService.generarNumFactura());

        // Establecer estados como "Pendiente"
        factura.setEstadoPago("Pendiente");
        factura.setEstadoEnvio("Pendiente");

        //imagen
        if (factura.getId() == null) {//cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            factura.setImagen(nombreImagen);
        } else {

        }

        factura.setUser(user);

        //Se guardan los datos de la factura
        facturaService.save(factura);

        //Guardar detalles
        for (DetalleFactura dt : detalles) {
            dt.setFactura(factura);
            detalleFactService.save(dt);
        }

        //limpiar lista  y factura
        factura = new Factura();
        detalles.clear();

        model.addAttribute("userDto", userDto);
        // Agregar mensaje de éxito para mostrar en la página de destino
    redirectAttributes.addFlashAttribute("exitofacturauser", "Compra efectuada con éxito.");
        return "redirect:/verhome";
    }

    @PostMapping("/buscar")
    public String buscarProducto(@RequestParam String palabra, Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        //Filtro para retornar un nombre utilizando un filtro que busca en la lista de productos. Retorna un string y se pasa a una lista
        List<Productos> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(palabra)).collect(Collectors.toList());
        model.addAttribute("userDto", userDto);
        model.addAttribute("productos", productos);
        return "usuario/home";
    }

    /* detalles compras usuario */
    @GetMapping("/comprasUser")
    public String obtenerCompras(Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        List<Factura> factura = facturaService.findByUsuario(user);

        model.addAttribute("userDto", userDto);
        model.addAttribute("factura", factura);

        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        Optional<Factura> factura = facturaService.findById(id);

        model.addAttribute("userDto", userDto);
        model.addAttribute("detalles", factura.get().getListaDetalles());

        return "usuario/detallecompra";
    }

}
