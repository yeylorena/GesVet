package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.jwtUtil.JwtUtils;
import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.DetalleFactura;
import com.example.gesvet.models.Factura;
import com.example.gesvet.models.MetodoPago;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.User;
import com.example.gesvet.models.respuesta;
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
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/carrito")
public class HomeControllerapi {

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

    @GetMapping("/verhome")
    public ResponseEntity<Object> homecategoria(HttpServletRequest request) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtén las categorías asociadas al tipo "Producto"
                    List<Categorias> categoriasProducto = categoriasservice.findByTipoCategoria("Producto");

                    // Objeto que contiene los datos que quieres devolver como JSON
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("categoriasProducto", categoriasProducto);

                    return ResponseEntity.ok(responseData);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {

            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping(value = "/productohomes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> productoHomes(@PathVariable Integer id, HttpServletRequest request) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener la categoría por su ID
                    Categorias categoria = categoriasservice.findById(id);

                    // Obtener los productos asociados a la categoría
                    List<Productos> productos = productoService.findByCategoria(categoria);

                    // Construir un objeto que contenga los datos que deseas devolver como JSON
                    Map<String, Object> response = new HashMap<>();
                    response.put("categoria", categoria);
                    response.put("productos", productos);

                    // Devolver la respuesta como JSON con un ResponseEntity
                    return ResponseEntity.ok(response);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/verproducto")
    public ResponseEntity<Object> verProductos(HttpServletRequest request, Authentication authentication) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener todos los productos
                    List<Productos> productos = productoService.findAll();

                    // Construir un objeto de respuesta que contenga los productos
                    Map<String, Object> response = new HashMap<>();
                    response.put("productos", productos);

                    // Devolver la respuesta como JSON con un ResponseEntity
                    return ResponseEntity.ok(response);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<Object> verProducto(@PathVariable Integer id, HttpServletRequest request, Authentication authentication) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener el producto por su ID
                    Optional<Productos> productoOptional = productoService.get(id);
                    if (productoOptional.isPresent()) {
                        Productos producto = productoOptional.get();

                        // Construir un objeto de respuesta que contenga el producto
                        Map<String, Object> response = new HashMap<>();

                        response.put("producto", producto);

                        // Devolver la respuesta como JSON con un ResponseEntity
                        return ResponseEntity.ok(response);
                    } else {

                        var respuesta = new respuesta(
                                "error",
                                "Producto no encontrado"
                        );
                        // Si el producto no se encuentra, devolver una respuesta de no encontrado
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }
                }
            }

            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {

            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/cart")
    public ResponseEntity<Object> addToCart(@RequestBody Map<String, Object> body, HttpServletRequest request, Authentication authentication, HttpSession httpSession) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener los detalles del usuario actual
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

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

                    // Obtener el ID y la cantidad del cuerpo de la solicitud JSON
                    Integer id = (Integer) body.get("id");
                    Integer cantidad = (Integer) body.get("cantidad");

                    // Obtener el producto por su ID
                    Optional<Productos> optionalProducto = productoService.get(id);
                    if (optionalProducto.isPresent()) {
                        Productos producto = optionalProducto.get();

                        // Verificar si la cantidad a agregar supera la cantidad disponible
                        if (cantidad > producto.getCantidad()) {
                            var respuesta = new respuesta("error", "La cantidad solicitada supera la disponibilidad del producto");
                            // Devolver una respuesta de error si la cantidad excede la disponibilidad
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                        }

                        // Buscar si el producto ya está en el carrito
                        Optional<DetalleFactura> existingDetail = detalles.stream()
                                .filter(detalle -> detalle.getProductos().getId().equals(id))
                                .findFirst();

                        if (existingDetail.isPresent()) {
                            // Si el producto ya está en el carrito, actualizar la cantidad
                            DetalleFactura existingDetalle = existingDetail.get();
                            int newCantidad = (int) (existingDetalle.getCantidad() + cantidad);

                            // Verificar si la nueva cantidad supera la disponibilidad del producto
                            if (newCantidad > producto.getCantidad()) {
                                var respuesta = new respuesta("error", "La cantidad solicitada supera la disponibilidad del producto");
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                            }

                            // Actualizar la cantidad y el total del detalle existente
                            existingDetalle.setCantidad(newCantidad);
                            existingDetalle.setTotal(existingDetalle.getPrecio() * newCantidad);
                        } else {
                            // Si el producto no está en el carrito, agregar un nuevo detalle
                            DetalleFactura detalleFactura = new DetalleFactura();
                            detalleFactura.setCantidad(cantidad);
                            detalleFactura.setId(producto.getId());
                            detalleFactura.setImagen(producto.getImagen());
                            detalleFactura.setPrecio(producto.getPrecio());
                            detalleFactura.setNombre(producto.getNombre());
                            detalleFactura.setTotal(producto.getPrecio() * cantidad);
                            detalleFactura.setProductos(producto);

                            // Agregar el nuevo detalle al carrito
                            detalles.add(detalleFactura);
                        }

                        double total = detalles.stream().mapToDouble(detalle -> detalle.getTotal()).sum();

                        // Crear una instancia de Factura y establecer el total
                        Factura factura = new Factura();
                        factura.setTotal(total);
                        var respuesta = new respuesta("Creado", "Producto agregado al carrito exitosamente");
                        return ResponseEntity.ok(respuesta);
                    } else {
                        // Devolver una respuesta de error si el producto no se encuentra
                        var respuesta = new respuesta("error", "Producto no encontrado");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }
                }
            }

            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );

            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver una respuesta de error
            var respuesta = new respuesta("error", "Error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/cart/update")
    public ResponseEntity<Object> updateCartItemQuantity(@RequestParam Integer productId,
            @RequestParam Integer newQuantity,
            HttpServletRequest request,
            HttpSession httpSession) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token JWT
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                   

                    if (detalles == null || detalles.isEmpty()) {
                        var respuesta = new respuesta("error", "No se encontraron detalles del carrito");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }

                    // Buscar el detalle correspondiente en la lista de detalles del carrito
                    Optional<DetalleFactura> optionalDetalle = detalles.stream()
                            .filter(detalle -> detalle.getProductos().getId().equals(productId))
                            .findFirst();

                    if (optionalDetalle.isPresent()) {
                        DetalleFactura detalle = optionalDetalle.get();
                        // Verificar si la nueva cantidad excede la cantidad disponible en el inventario
                        if (newQuantity > detalle.getProductos().getCantidad()) {
                            var respuesta = new respuesta("error", "La cantidad seleccionada excede la cantidad disponible en el inventario");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                        }

                        // Actualizar la cantidad del producto en el detalle del carrito
                        detalle.setCantidad(newQuantity);
                        detalle.setTotal(detalle.getPrecio() * newQuantity);

                        // Guardar los detalles del carrito actualizados en la sesión del usuario
                        httpSession.setAttribute("carrito", detalles);

                        var respuesta = new respuesta(
                                "Creado",
                                "Cantidad del producto actualizada exitosamente"
                        );

                        return ResponseEntity.ok(respuesta);
                    } else {

                        var respuesta = new respuesta(
                                "error",
                                "El producto no se encontró en el carrito"
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }
                }
            }

            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {

            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    //Quitar un producto del carrito
    @GetMapping("/delete/cart/{productId}")
    public ResponseEntity<Object> deleteCartItem(@PathVariable Integer productId, HttpServletRequest request, HttpSession httpSession) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {

                    if (detalles == null || detalles.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron detalles en el carrito");
                    }

                    // Filtrar los detalles para eliminar el producto con el ID especificado
                    detalles = detalles.stream()
                            .filter(detalle -> !detalle.getProductos().getId().equals(productId))
                            .collect(Collectors.toList());

                    // Guardar los detalles actualizados del carrito en la sesión del usuario
                    httpSession.setAttribute("carrito", detalles);
                    var respuesta = new respuesta(
                            "Creado",
                            "Producto eliminado del carrito exitosamente"
                    );
                    return ResponseEntity.ok(respuesta);
                }
            }

            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/getCart")
    @ResponseBody
    public ResponseEntity<Object> getCart(HttpServletRequest request, Authentication authentication, HttpSession httpSession) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener los detalles del usuario actual
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Buscar al usuario por su nombre de usuario
                    User user = userService.findByUsername(username);

// Calcular el total del carrito
                    double totalCarrito = detalles.stream().mapToDouble(DetalleFactura::getTotal).sum();
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

                    // Devolver el carrito en formato JSON
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("userDto", userDto);
                    responseData.put("cart", detalles);
                    responseData.put("totalCarrito", totalCarrito);

                    return ResponseEntity.ok(responseData);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver una respuesta de error
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/factura")
    public ResponseEntity<Object> getFactura(Authentication authentication, HttpSession httpSession, HttpServletRequest request) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener los detalles del usuario actual
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

                    if (detalles == null || detalles.isEmpty()) {
                        // Devolver una respuesta de error si el producto no se encuentra
                        var respuesta = new respuesta("error", "Por favor, añade un producto antes de ver la factura");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                    }

                    // Obtener los métodos de pago disponibles
                    List<MetodoPago> metodosDePago = metodopagoservice.findAll();

                    // Calcular el total de la factura
                    double total = detalles.stream().mapToDouble(detalle -> detalle.getTotal()).sum();

                    // Crear un objeto que contenga los datos de la factura
                    Factura facturaDto = new Factura();
                    facturaDto.setListaDetalles(detalles); // Establecer los detalles
                    facturaDto.setTotal(total); // Establecer el total

                    // Agregar los métodos de pago a la respuesta
                    Map<String, Object> responseData = new HashMap<>();

                    responseData.put("factura", facturaDto);
                    responseData.put("userDto", userDto);
                    responseData.put("cart", detalles);
                    responseData.put("metodosDePago", metodosDePago);

                    return ResponseEntity.ok(responseData);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {

            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/saveFact")
    @ResponseBody
    public ResponseEntity<Object> saveFact(
            @RequestParam("metodoPago") Integer metodoPagoId,
            Authentication authentication,
            Principal principal,
            HttpServletRequest request) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Obtener los detalles del usuario actual
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Buscar al usuario por su nombre de usuario
                    User user = userService.findByUsername(username);

                    // Obtener el Método de Pago seleccionado
                    MetodoPago metodoPagoSeleccionado = metodopagoservice.findById(metodoPagoId);

                    if (detalles == null || detalles.isEmpty()) {
                        var respuesta = new respuesta("error", "No hay productos en el carrito para generar la factura.");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                    }

                    // Calcular el total de la factura
                    double totalFactura = detalles.stream().mapToDouble(detalle -> detalle.getTotal()).sum();

                    // Crear una nueva instancia de Factura
                    Factura factura = new Factura();

                    // Asignar el Método de Pago a la Factura
                    factura.setMetodoPago(metodoPagoSeleccionado);

                    // Añadir el role del usuario a la factura
                    factura.setRole(user.getRole());

                    // Establecer la fecha de la factura como la fecha actual
                    Date fecha = new Date();
                    factura.setFecha(fecha);

                    // Generar y establecer el número de la factura
                    factura.setNumero(facturaService.generarNumFactura());

                    // Establecer estados como "Pendiente"
                    factura.setEstadoPago("Pendiente");
                    factura.setEstadoEnvio("Pendiente");

                    // Asignar el total de la factura
                    factura.setTotal(totalFactura);

                    // Asignar el usuario a la factura
                    factura.setUser(user);

                    // Guardar la factura en la base de datos
                    Factura facturaGuardada = facturaService.save(factura);

                    // Asociar los detalles de la factura
                    for (DetalleFactura dt : detalles) {
                        dt.setFactura(facturaGuardada);
                        detalleFactService.save(dt);
                    }

                    // Limpiar la lista de detalles después de guardar la factura
                    detalles.clear();

                    // Devolver una respuesta de éxito
                    var respuesta = new respuesta("Creado", "Factura generada exitosamente");
                    return ResponseEntity.ok(respuesta);
                }
            }

            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
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
    public ResponseEntity<Object> obtenerCompras(HttpServletRequest request) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Buscar al usuario por su nombre de usuario
                    User user = userService.findByUsername(username);

                    // Verificar si el usuario existe
                    if (user == null) {
                        var respuesta = new respuesta(
                                "error",
                                "Usuario no encontrado"
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }

                    // Obtener las facturas del usuario
                    List<Factura> facturas = facturaService.findByUsuario(user);

                    // Crear un objeto de respuesta que contenga los datos del usuario y las facturas
                    Map<String, Object> responseData = new HashMap<>();

                    responseData.put("facturas", facturas);

                    return ResponseEntity.ok(responseData);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Object> detalleCompra(@PathVariable Integer id, HttpServletRequest request) {
        try {
            // Extraer el token del encabezado de la solicitud
            String jwtToken = extractTokenFromRequest(request);

            // Validar el token
            if (jwtToken != null) {
                // Obtener las reclamaciones del token
                Claims claims = JwtUtils.extractClaims(jwtToken);

                if (claims != null) {
                    String username = claims.getSubject();

                    // Buscar al usuario por su nombre de usuario
                    User user = userService.findByUsername(username);

                    // Verificar si el usuario existe
                    if (user == null) {
                        var respuesta = new respuesta(
                                "error",
                                "Usuario no encontrado"
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }

                    // Obtener la factura por su ID
                    Optional<Factura> optionalFactura = facturaService.findById(id);

                    // Verificar si la factura existe
                    if (!optionalFactura.isPresent()) {
                        var respuesta = new respuesta(
                                "error",
                                "Error Factura no encontrada"
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                    }

                    Factura factura = optionalFactura.get();

                    // Verificar si la factura pertenece al usuario
                    if (!factura.getUsuario().equals(user)) {
                        var respuesta = new respuesta(
                                "error",
                                "No tiene permiso para acceder a esta factura"
                        );
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
                    }

                    // Obtener los detalles de la factura
                    List<DetalleFactura> detalles = factura.getListaDetalles();

                    // Crear un objeto de respuesta que contenga los detalles de la factura
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("idFactura", factura.getId());
                    responseData.put("detalles", detalles);

                    // Devolver la respuesta JSON
                    return ResponseEntity.ok(responseData);
                }
            }
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            // Si el token es inválido o no se proporciona, devuelve una respuesta de no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        } catch (Exception e) {
            var respuesta = new respuesta(
                    "error",
                    "Error al procesar la solicitud"
            );
            // Manejar cualquier excepción y devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // Método auxiliar para extraer el token del encabezado de la solicitud
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
