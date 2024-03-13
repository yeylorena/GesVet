package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.DetalleFactura;
import com.example.gesvet.models.Factura;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.User;
import com.example.gesvet.models.UsuarioVentas;
import com.example.gesvet.repository.UserRepository;
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
import com.example.gesvet.service.UserService;
import com.example.gesvet.service.UsuarioVentasService;
import java.security.Principal;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class HomeControllerAdmin {

    @Autowired
    private UsuarioVentasService serviceusuarioventas;

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

    //Array para almacenar los detalles de la factura
    List<DetalleFactura> detalles = new ArrayList<DetalleFactura>();

    //datos de la factura
    Factura factura = new Factura();

    @GetMapping("verhomeadmin")
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

        //ESTA ES PARA EL ADMINISTRADOR
        List<Factura> factura = facturaService.findByUsuario(user);

        //ESTA ES PARA EL USUARIO
        List<Factura> facturas = facturaService.findByUser_Role("USER");

        // Calculate the count of pending invoices
        long pendingInvoiceCount = facturas.stream().filter(f -> f.getEstadoPago().equals("Pendiente")).count();
        long pendingInvoiceentregado = facturas.stream().filter(f -> f.getEstadoPago().equals("Aprobado")).count();
        long pendingInvoicecancelado = facturas.stream().filter(f -> f.getEstadoPago().equals("Cancelado")).count();

        // Add the count to the model
        model.addAttribute("pendingInvoiceCount", pendingInvoiceCount);
        model.addAttribute("pendingInvoiceentregado", pendingInvoiceentregado);
        model.addAttribute("pendingInvoicecancelado", pendingInvoicecancelado);

        // Agrega las facturas al modelo
        model.addAttribute("facturas", facturas);

        model.addAttribute("userDto", userDto);

        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("factura", factura);
        return "usuario/Homeadmin";
    }

    @GetMapping("productohomeadmin/{id}")
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

        return "usuario/Producto_Homeadmin";
    }

    @PostMapping("/cartadmin")
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

                redirectAttributes.addFlashAttribute("erroradmin", true);
                return "redirect:/productohomeadmin/" + id;
            }

            existingDetalle.setCantidad(existingDetalle.getCantidad() + cantidad);
            existingDetalle.setTotal(existingDetalle.getPrecio() * existingDetalle.getCantidad());
        } else {
            // Check if adding the new quantity exceeds the available quantity
            if (cantidad > producto.getCantidad()) {

                redirectAttributes.addFlashAttribute("erroradmin", true);
                return "redirect:/productohomeadmin/" + id;
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
        return "usuario/Carritoadmin";
    }

    //Quitar un producto del carrito
    @GetMapping("/deleteadmin/cartadmin/{id}")
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

        return "usuario/Carritoadmin";
    }

    @GetMapping("/getCartadmin")
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

        return "/usuario/Carritoadmin";
    }

    @GetMapping("/facturaadmin")
    public String factura(Model model, Authentication authentication, Principal principal) {
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
        model.addAttribute("usuario", user);

        return "usuario/ResumenFacturaadmin";
    }

    @PostMapping("/saveFactadmin")
    public String saveFactAdmin(Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("documento") String documento,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String email) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        String username = authentication.getName();
        User user = userService.findByUsername(username);

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

        UsuarioVentas usuarioVentas = new UsuarioVentas();
        usuarioVentas.setNombre(nombre);
        usuarioVentas.setApellido(apellido);
        usuarioVentas.setDocumento(documento);
        usuarioVentas.setDireccion(direccion);
        usuarioVentas.setTelefono(telefono);
        usuarioVentas.setEmail(email);
        factura.setRole(user.getRole());

        serviceusuarioventas.save(usuarioVentas);

        Date fecha = new Date();
        factura.setFecha(fecha);
        factura.setNumero(facturaService.generarNumFactura());
        factura.setUser(user);
        factura.setUsuarioventas(usuarioVentas);

        facturaService.save(factura);

        boolean facturaCancelada = false;  // Variable para controlar si la factura debe cancelarse

        for (DetalleFactura dt : detalles) {
            dt.setFactura(factura);
            detalleFactService.save(dt);

            // Lógica de descuento de productos
            Productos producto = dt.getProductos(); // Cambiado para obtener el producto directamente del detalle
            double cantidadVendida = dt.getCantidad();

            // Obtener el producto de la base de datos para asegurarse de tener la cantidad más actualizada
            Productos productoEnBD = productoService.findById(producto.getId());

            // Verificar si hay suficientes productos disponibles antes de realizar el descuento
            if (productoEnBD.getCantidad() >= cantidadVendida) {
                productoEnBD.setCantidad((int) (productoEnBD.getCantidad() - cantidadVendida));
                // Actualizar el producto en la base de datos
                productoService.update(productoEnBD);
            } else {
                facturaCancelada = true;  // Marcar la factura como cancelada
                detalles.clear();
                // Agregar mensaje de éxito para mostrar en la página de destino
                redirectAttributes.addFlashAttribute("errorcarritocompra", "Factura cancelada debido a la falta de disponibilidad de la cantidad solicitada de productos.");
                break;  // Salir del bucle si la factura está cancelada
            }
        }

        // Cambiar el estado de la factura a "Cancelada" si es necesario
        if (facturaCancelada) {
            factura.setEstadoPago("Cancelada");

        } else {
            // Si la factura no está cancelada, la guardamos como aprobada
            factura.setEstadoPago("Aprobado");

        }

        // Guardar la factura después de procesar todos los detalles
        facturaService.save(factura);

        factura = new Factura();
        detalles.clear();
        model.addAttribute("userDto", userDto);

        // Agregar mensaje de éxito para mostrar en la página de destino
        redirectAttributes.addFlashAttribute("exitos", "Compra efectuada con éxito.");
        return "redirect:/verhomeadmin";
    }

    @GetMapping("/cancelarcarrito")
    public String eliminardatosC(Model model) {
        // Lógica para limpiar los datos del carrito
        detalles.clear();
        factura = new Factura();

        // Otras lógicas necesarias...
        return "redirect:/verhomeadmin"; // Redirige a la página deseada
    }

    // Método para mostrar el formulario de edición
    @GetMapping("/editarFactura/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
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

        Factura factura = facturaService.findById(id).orElse(new Factura());
        // Agrega la factura al modelo para mostrar los detalles en el formulario de edición
        model.addAttribute("factura", factura);
        model.addAttribute("userDto", userDto);

        return "usuario/editarfactura"; // Reemplaza con la ruta correcta de tu formulario de edición
    }

    @PostMapping("/editarFactura/{id}")
    public String actualizarFactura(@PathVariable Integer id, Factura factura, Model model,
            Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
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

        Factura existingFactura = facturaService.findById(id).orElse(null);

        if (existingFactura != null) {
            // Verificar si la factura ya ha sido procesada
            if (!existingFactura.isProcesada()) {
                // Verificar que el estado de envío sea "Enviado" al aprobar la factura
                if ("Aprobado".equals(factura.getEstadoPago())) {
                    if (!"Enviado".equals(factura.getEstadoEnvio())) {
                        // Mostrar un mensaje de error si el estado de envío no es "Enviado"
                        redirectAttributes.addFlashAttribute("errorfactenvio", "Para aprobar la factura, el estado de envío debe ser 'Enviado'.");
                        return "redirect:/editarFactura/{id}";
                    }
                }

                // Actualizar solo los campos deseados
                existingFactura.setEstadoPago(factura.getEstadoPago());
                existingFactura.setEstadoEnvio(factura.getEstadoEnvio());

                // Lógica de descuento de productos al aprobar la factura
                if ("Aprobado".equals(factura.getEstadoPago())) {
                    for (DetalleFactura detalle : existingFactura.getListaDetalles()) {
                        Productos producto = detalle.getProductos();
                        // Verificar si hay suficientes productos disponibles antes de realizar el descuento
                        if (producto.getCantidad() >= detalle.getCantidad()) {
                            producto.setCantidad((int) (producto.getCantidad() - detalle.getCantidad()));
                            // Actualizar el producto en la base de datos
                            productoService.update(producto);
                        } else {
                            // Manejar la situación donde no hay suficientes productos disponibles
                            // Agregar un mensaje de error para mostrar en el HTML
                            redirectAttributes.addFlashAttribute("errorfact", "No hay suficientes productos disponibles. Producto: " + producto.getNombre());
                            return "redirect:/editarFactura/{id}";
                        }
                    }

                    // Marcar la factura como procesada para evitar descuentos adicionales
                    existingFactura.setProcesada(true);
                    // Agregar mensaje de éxito
                    redirectAttributes.addFlashAttribute("exito", "Factura aprobada con éxito.");
                }

                facturaService.update(existingFactura);
            } else {
                // Manejar el caso en que la factura ya ha sido procesada
                redirectAttributes.addFlashAttribute("error", "Esta factura ya ha sido procesada.");
                return "redirect:/editarFactura/{id}";
            }
        }

        model.addAttribute("userDto", userDto);
        return "redirect:/verhomeadmin";
    }

    @PostMapping("/buscaradmin")
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
        return "usuario/Homeadmin";
    }
    
    /* detalles compras admin */
 /*
    
	@GetMapping("/comprasadmin")
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
        
        
		List<Factura> factura= facturaService.findByUsuario(user);
                
		 model.addAttribute("userDto", userDto);
		model.addAttribute("factura", factura);
		
		return "usuario/Homeadmin";
	}
     */
    @GetMapping("/detalleadmin/{id}")
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

        return "usuario/detallecompraadmin";
    }

}
