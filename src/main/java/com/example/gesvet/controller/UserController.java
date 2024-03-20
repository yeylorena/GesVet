package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.User;
import com.example.gesvet.service.IFacturaService;
import com.example.gesvet.service.IProductoService;
import com.example.gesvet.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IFacturaService facturaService;

    @Autowired
    private UserDetailsService userDetailsService;

    private UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        String username = "";

        if (principal != null) {
            // Obtener el nombre de usuario actual
            username = principal.getName();

            // Obtener los detalles del usuario actual
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            model.addAttribute("userdetail", userDetails);

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
            userDto.setImagen("/images/" + user.getImagen()); // Asegúrate de tener la ruta correcta

           if (!userService.usuarioDatosPersonalesCompletos(user)) {
        // Si los datos personales no están completos, agrega el atributo para mostrar la alerta
        model.addAttribute("mostrarAlerta", true);
    }
            model.addAttribute("userDto", userDto);
        }

        return "Inicio_usu";
    }
    @ModelAttribute("mostrarAlerta")
public boolean mostrarAlerta() {
    return false; // Este método se asegurará de que el atributo esté presente en el modelo incluso si la lógica en el método home no lo agrega
}

    @GetMapping("/homes")
    public String homes(Model model, Principal principal) {
        String username = "";

        if (principal != null) {
            // Obtener el nombre de administrador actual
            username = principal.getName();

            // Obtener los detalles del administrador actual
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            model.addAttribute("userdetail", userDetails);

            // Buscar al administrador por su nombre de usuario
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
            userDto.setImagen("/images/" + user.getImagen()); // Asegúrate de tener la ruta correcta

            //para mostarr el total de ventas en la acarta
            List<Object[]> detallesVentas = facturaService.findDetallesVentas();

            // Lógica para calcular el total de ventas
            double totalVentas = detallesVentas.stream()
                    .mapToDouble(arr -> ((Number) arr[3]).doubleValue())
                    .sum();

            model.addAttribute("detallesVentas", detallesVentas);
            model.addAttribute("totalVentas", totalVentas);

            // para mostrar el total de compras en la carta 
            List<Object[]> detallesCompras = facturaService.findDetallesCompras();

            // Lógica para calcular el total de ventas
            double totalCompras = detallesCompras.stream()
                    .mapToDouble(arr -> ((Number) arr[3]).doubleValue())
                    .sum();

            model.addAttribute("detallesCompras", detallesCompras);
            model.addAttribute("totalCompras", totalCompras);
            
            // para mostrar el total d la facturacion diaria

            List<Object[]> detallesFacturas = facturaService.findDetallesFactura();

            // Lógica para calcular el total de ventas
            double totalFacturas = detallesFacturas.stream()
                    .mapToDouble(arr -> ((Number) arr[3]).doubleValue())
                    .sum();

            model.addAttribute("detallesFacturas", detallesFacturas);
            model.addAttribute("totalFacturas", totalFacturas);

            model.addAttribute("userDto", userDto);

            List<Object[]> productosMasVendidos = facturaService.obtenerTopProductosMasVendidos();

            model.addAttribute("productosMasVendidos", productosMasVendidos);
            int limite = 10; // Cantidad máxima de productos a mostrar
            int minimoStock = 5; // Establece el límite inferior para considerar un producto con poco stock

            List<Productos> productosConPocoStock = productoService.getTopProductosConPocoStock(limite, minimoStock);

            model.addAttribute("productosConPocoStock", productosConPocoStock);

            model.addAttribute("userDto", userDto);
        }

        return "Inicio_admin";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "Login_usu";
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {

        model.addAttribute("user", userDto);
        return "Registro_Usu";
    }

    @PostMapping("/register")
    public String registerSave(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Registro_Usu"; // Devolver al formulario de registro si hay errores de validación
        }

        User user = userService.findByUsername(userDto.getUsername());

        if (user != null) {
            model.addAttribute("userexist", user);
            return "Registro_Usu";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("passwordMismatch", true);
            return "Registro_Usu";
        }

        userService.save(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/totalcompras")
    public String totalcompras(Model model, Authentication authentication, Principal principal) {
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

        List<Object[]> detallesCompras = facturaService.findDetallesCompras();

        // Lógica para calcular el total de ventas
        double totalCompras = detallesCompras.stream()
                .mapToDouble(arr -> ((Number) arr[3]).doubleValue())
                .sum();

        model.addAttribute("detallesCompras", detallesCompras);
        model.addAttribute("totalCompras", totalCompras);

        model.addAttribute("userDto", userDto);

        return "total_ventas";
    }

    @GetMapping("/totalventas")
    public String totalventas(Model model, Authentication authentication, Principal principal) {
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

        List<Object[]> detallesVentas = facturaService.findDetallesVentas();

        // Lógica para calcular el total de ventas
        double totalVentas = detallesVentas.stream()
                .mapToDouble(arr -> ((Number) arr[3]).doubleValue())
                .sum();

        model.addAttribute("detallesVentas", detallesVentas);
        model.addAttribute("totalVentas", totalVentas);

        model.addAttribute("userDto", userDto);

        return "total_Ventas_1";
    }

    @GetMapping("/totalfacturas")
    public String totalfacturas(Model model, Authentication authentication, Principal principal) {
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

        List<Object[]> detallesFacturas = facturaService.findDetallesFactura();

        // Lógica para calcular el total de ventas
        double totalFacturas = detallesFacturas.stream()
                .mapToDouble(arr -> ((Number) arr[3]).doubleValue())
                .sum();

        model.addAttribute("detallesFacturas", detallesFacturas);
        model.addAttribute("totalFacturas", totalFacturas);

        model.addAttribute("userDto", userDto);

        return "total_factura";
    }

}
