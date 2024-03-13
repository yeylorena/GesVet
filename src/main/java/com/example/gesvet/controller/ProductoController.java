package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.MetodoPago;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.Servicios;
import com.example.gesvet.models.ServiciosUser;
import com.example.gesvet.models.Tipocategoria;
import com.example.gesvet.models.User;
import com.example.gesvet.service.ICategoriasService;
import com.example.gesvet.service.IMetodoPagoService;
import com.example.gesvet.service.UploadFileService;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.gesvet.service.IProductoService;
import com.example.gesvet.service.IServicioService;
import com.example.gesvet.service.IServiciosUserService;
import com.example.gesvet.service.ITipocategoriaservice;
import com.example.gesvet.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    IMetodoPagoService metodopagoservice;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private ICategoriasService categoriasService;

    @Autowired
    private IServicioService serviciosService;

    @Autowired
    private ITipocategoriaservice tipocategoriaservice;

    @Autowired
    private IServiciosUserService serviciouserservice;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String showProductos(Model model, Authentication authentication, Principal principal) {
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

        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("categorias", categoriasService.findAll());
        model.addAttribute("servicios", serviciosService.findAll());
        model.addAttribute("tipocategorias", tipocategoriaservice.findAll());
        model.addAttribute("serviciousers", serviciouserservice.findAll());
        model.addAttribute("metodopagos", metodopagoservice.findAll());
        model.addAttribute("userDto", userDto);
        return "productos/Gestion_Productos_Servicios";
    }

    @GetMapping("/crear")
    public String create(Model model, Authentication authentication, Principal principal) {

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
        List<Categorias> categoriasProducto = categoriasService.findByTipoCategoria("Producto");

        model.addAttribute("categoriasProducto", categoriasProducto);

        model.addAttribute("userDto", userDto);
        model.addAttribute("categorias", categoriasService.findAll());
        return "productos/agregar";

    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Productos productos, BindingResult result, Model model, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("activo") boolean activo, RedirectAttributes redirectAttributes) throws IOException {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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

// Obtener la categoría seleccionada
        Integer categoriaId = productos.getCategoria().getId();
        Categorias categoria = categoriasService.get(categoriaId).orElse(null);

        // Establecer la categoría en el producto
        productos.setCategoria(categoria);
        //imagen
        if (productos.getId() == null) {//cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            productos.setImagen(nombreImagen);
        } else {

        }

        model.addAttribute("userDto", userDto);
        productos.setActivo(activo);
        productoService.save(productos);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoproducto", "Producto agregado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        Productos producto = productoService.get(id).orElse(new Productos());
        List<Categorias> categoriasProducto = categoriasService.findByTipoCategoria("Producto");

        model.addAttribute("userDto", userDto);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriasProducto);

        return "productos/editar";
    }

    @PostMapping("/update")
    public String update(Productos producto, Model model, Authentication authentication, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("categoriaId") Integer categoriaId, RedirectAttributes redirectAttributes) throws IOException {

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

        Productos p = productoService.get(producto.getId()).orElse(new Productos());

        Categorias categoria = categoriasService.get(categoriaId).orElse(null);
        producto.setCategoria(categoria);

        if (file.isEmpty()) {//cuando editamos el producto pero no cambiamos la imagen
            producto.setImagen(p.getImagen());
        } else {//cuando editamos el producto y cambiamos la imagen

            //Eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }

            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        model.addAttribute("userDto", userDto);
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoproductoactualizado", "Producto actualizado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
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

        Productos p = productoService.get(id).orElse(null);

        if (p != null) {
            // Desactivar el producto en lugar de borrarlo
            p.setActivos(false);
            productoService.update(p);

            // Eliminar la imagen solo si no es la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
        }
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoproductoeliminado", "Producto eliminado con éxito");
        return "redirect:/productos";
    }


    /*categoria*/
    @GetMapping("/crearcategoria")
    public String createCategoria(Model model, Authentication authentication, Principal principal) {
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

        model.addAttribute("tipocategorias", tipocategoriaservice.findAll());
        model.addAttribute("categoria", new Categorias());
        return "productos/agregarcategoria";
    }

    @PostMapping("/savecategoria")
    public String saveCategoria(@ModelAttribute @Valid Categorias categoria, BindingResult result, Model model, Principal principal,
            @RequestParam("activo") boolean activo, RedirectAttributes redirectAttributes, @RequestParam("img") MultipartFile file) throws IOException {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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

        // Obtener el tipo de categoría seleccionado
        Integer tipoCategoriaId = categoria.getTipocategoria().getId();
        Tipocategoria tipoCategoria = tipocategoriaservice.get(tipoCategoriaId).orElse(null);

        if (categoria.getId() == null) {//cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            categoria.setImagen(nombreImagen);
        } else {

        }

        // Establecer el tipo de categoría en la categoría
        categoria.setTipocategoria(tipoCategoria);
        model.addAttribute("userDto", userDto);
        categoria.setActivo(activo);

        // Validar si hay errores en la validación
        if (result.hasErrors()) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("validarformulario", "Por favor, completa todos los campos del formulario.");
            return "redirect:/productos/crearcategoria";
        }
        // Verificar si ya existe una categoría con el mismo nombre
        if (categoriasService.existsByNombre(categoria.getNombre())) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("errorcategoriaexist", "Ya existe una categoría con el mismo nombre.");
            return "redirect:/productos/crearcategoria";
        }

        // Guardar la categoría
        categoriasService.save(categoria);

        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitocategroia", "Categoría agregada con éxito");

        return "redirect:/productos";
    }

    @GetMapping("/editarcategoria/{id}")
    public String editarCategoria(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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
        Categorias categoria = categoriasService.get(id).orElse(new Categorias());
// Asegúrate de que el objeto Tipocategoria esté inicializado
        if (categoria.getTipocategoria() == null) {
            categoria.setTipocategoria(new Tipocategoria());
        }

        System.out.println("Tipo Categoría ID: " + categoria.getTipocategoria().getId());

        List<Tipocategoria> tipocategorias = tipocategoriaservice.findAll();
        model.addAttribute("categoria", categoria);
        model.addAttribute("userDto", userDto);
        model.addAttribute("tipocategorias", tipocategorias);

        return "productos/editarcategoria";
    }

    @PostMapping("/updatecategoria")
    public String updateCategoria(@ModelAttribute Categorias categoria, Model model, Principal principal, RedirectAttributes redirectAttributes, @RequestParam("img") MultipartFile file) throws IOException {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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

        // Obtener el tipo de categoría seleccionado
        Integer tipoCategoriaId = categoria.getTipocategoria().getId();
        Tipocategoria tipoCategoria = tipocategoriaservice.get(tipoCategoriaId).orElse(null);

        // Establecer el tipo de categoría en la categoría
        categoria.setTipocategoria(tipoCategoria);
        model.addAttribute("userDto", userDto);

        Categorias c = categoriasService.get(categoria.getId()).orElse(new Categorias());

        if (file.isEmpty()) {//cuando editamos el producto pero no cambiamos la imagen
            categoria.setImagen(c.getImagen());
        } else {//cuando editamos el producto y cambiamos la imagen

            //Eliminar cuando no sea la imagen por defecto
            if (!c.getImagen().equals("default.jpg")) {
                upload.deleteImage(c.getImagen());
            }

            String nombreImagen = upload.saveImage(file);
            categoria.setImagen(nombreImagen);
        }

        // Obtener la categoría actual de la base de datos
        Categorias categoriaActual = categoriasService.get(categoria.getId()).orElse(null);

        // Validar si el nombre ha cambiado y ya existe una categoría con el nuevo nombre
        if (!categoriaActual.getNombre().equals(categoria.getNombre()) && categoriasService.existsByNombre(categoria.getNombre())) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("errorcategoriaexist", "Ya existe una categoría con el mismo nombre.");
            return "redirect:/productos/editarcategoria/" + categoria.getId();

        }

        categoriasService.update(categoria);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitocategroiaactualizada", "Categoría actualizada con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/deletecategoria/{id}")
    public String deleteCategoria(@PathVariable Integer id, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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
        categoriasService.delete(id);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoeliminadocategroia", "Categoría eliminada con éxito");
        return "redirect:/productos";
    }

    /* Servicios */
    @GetMapping("/crearservicio")
    public String createservicio(Model model, Authentication authentication, Principal principal) {
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
        return "productos/agregarservicio";
    }

    @PostMapping("/saveservicio")
    public String saveservicio(Servicios servicios, Model model, Authentication authentication, Principal principal, @RequestParam("activo") boolean activo, RedirectAttributes redirectAttributes) throws IOException {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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
        servicios.setActivo(activo);
        serviciosService.save(servicios);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoservicio", "Tipo servicio agregado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/editarservicio/{id}")
    public String editarservicio(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        Servicios servicio = new Servicios();
        Optional<Servicios> optionalServicio = serviciosService.get(id);
        servicio = optionalServicio.get();

        model.addAttribute("userDto", userDto);
        model.addAttribute("servicio", servicio);

        return "productos/editarservicio";
    }

    @PostMapping("/updateservicio")
    public String updateservicio(Servicios servicio, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) throws IOException {

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

        Servicios s = new Servicios();
        s = serviciosService.get(servicio.getId()).get();

        servicio.setUsuario(s.getUsuario());
        serviciosService.update(servicio);

        model.addAttribute("userDto", userDto);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoservicioactualizado", "Tipo servicio actualizado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/deleteservicio/{id}")
    public String deleteservicio(@PathVariable Integer id, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {

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

        Servicios s = new Servicios();
        s = serviciosService.get(id).get();

        if (s != null) {
            // Desactivar el producto en lugar de borrarlo
            s.setActivos(false);
            serviciosService.update(s);
        }

        model.addAttribute("userDto", userDto);

        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitoservicioeliminado", "Tipo servicio eliminado con éxito");
        return "redirect:/productos";
    }

    /* tipocategoria */
    @GetMapping("/creartipocategoria")
    public String createtipocategoria(Model model, Authentication authentication, Principal principal) {
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
        return "productos/agregartipocategoria";
    }

    @PostMapping("/savetipocategoria")
    public String savetipocategoria(Tipocategoria tipocategoria, Model model, Authentication authentication, Principal principal, @RequestParam("activo") boolean activo, RedirectAttributes redirectAttributes) throws IOException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
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

        model.addAttribute("userDto", userDto);
        tipocategoria.setActivo(activo);
        // Verificar si ya existe una categoría con el mismo nombre
        if (tipocategoriaservice.existsByNombre(tipocategoria.getNombre())) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("errortipocategoriaexist", "Ya existe un Tipo de categoría con el mismo nombre.");
            return "redirect:/productos/creartipocategoria";
        }

        try {
            tipocategoriaservice.save(tipocategoria);
            // Agregar mensaje de éxito
            redirectAttributes.addFlashAttribute("exitotipocategoria", "Tipo de categoría guardado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            // Agregar mensaje de error
            redirectAttributes.addFlashAttribute("errortipocategoria", "Error al guardar el tipo de categoría. Intente de nuevo.");
        }

        return "redirect:/productos";
    }

    @GetMapping("/editartipocategoria/{id}")
    public String editartipocategoria(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        Tipocategoria tipocategoria = new Tipocategoria();
        Optional<Tipocategoria> optionaltipoCategoria = tipocategoriaservice.get(id);
        tipocategoria = optionaltipoCategoria.get();

        model.addAttribute("userDto", userDto);
        model.addAttribute("servicio", tipocategoria);
        return "productos/editartipocategoria";
    }

    @PostMapping("/updatetipocategoria")
    public String updatetipocategoria(Tipocategoria tipocategoria, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) throws IOException {
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

        // Obtener la categoría actual de la base de datos
        Tipocategoria categoriaActual = tipocategoriaservice.get(tipocategoria.getId()).orElse(null);

        // Validar si el nombre ha cambiado y ya existe una categoría con el nuevo nombre
        if (!categoriaActual.getNombre().equals(tipocategoria.getNombre()) && tipocategoriaservice.existsByNombre(tipocategoria.getNombre())) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("errortipocategoriaexist", "Ya existe un tipo de categoría con el mismo nombre.");
            return "redirect:/productos/editartipocategoria/" + tipocategoria.getId();

        }

        try {
            Tipocategoria t = tipocategoriaservice.get(tipocategoria.getId()).orElse(null);

            if (t != null) {
                tipocategoria.setUsuario(t.getUsuario());
                tipocategoriaservice.update(tipocategoria);
                // Agregar mensaje de éxito
                redirectAttributes.addFlashAttribute("exitoupdatetipocategoria", "Tipo de categoría actualizado con éxito.");
            } else {
                // Agregar mensaje de error si no se encuentra el tipo de categoría
                redirectAttributes.addFlashAttribute("errortiponoencontrado", "No se encontró el tipo de categoría para actualizar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Agregar mensaje de error
            redirectAttributes.addFlashAttribute("erroractualizartipocategoria", "Error al actualizar el tipo de categoría. Intente de nuevo.");
        }

        return "redirect:/productos";
    }

    @GetMapping("/deletetipocategoria/{id}")
    public String deletetipocategoria(@PathVariable Integer id, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {

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

        try {
            Tipocategoria t = tipocategoriaservice.get(id).orElse(null);

            if (t != null) {
                tipocategoriaservice.delete(id);
                // Agregar mensaje de éxito
                redirectAttributes.addFlashAttribute("exitoeliminartipocategoria", "Tipo de categoría eliminado con éxito.");
            } else {
                // Agregar mensaje de error si no se encuentra el tipo de categoría
                redirectAttributes.addFlashAttribute("encontrareliminartipocategoria", "No se encontró el tipo de categoría para eliminar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Agregar mensaje de error
            redirectAttributes.addFlashAttribute("erroraleliminartipocategoria", "Error al eliminar el tipo de categoría. Intente de nuevo.");
        }

        return "redirect:/productos";
    }


    /*servicio user */
    @GetMapping("/crearserviciouser")
    public String createserviciouser(Model model, Authentication authentication, Principal principal) {

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

        // Obtén las categorías asociadas al tipo "Servicio"
        List<Categorias> categoriasServicio = categoriasService.findByTipoCategoria("Servicio");

        // Utiliza un nombre consistente para el atributo del modelo
        model.addAttribute("categoriasServicio", categoriasServicio);

        model.addAttribute("userDto", userDto);
        model.addAttribute("categorias", categoriasService.findAll());
        return "productos/agregarserviciouser";

    }

    @PostMapping("/saveserviciouser")
    public String saveserviciouser(@ModelAttribute ServiciosUser serviciosuser, Model model, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("activo") boolean activo, RedirectAttributes redirectAttributes) throws IOException {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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

// Obtener la categoría seleccionada
        Integer categoriaId = serviciosuser.getCategoria().getId();
        Categorias categoria = categoriasService.get(categoriaId).orElse(null);

        // Establecer la categoría en el producto
        serviciosuser.setCategoria(categoria);
        //imagen
        if (serviciosuser.getId() == null) {//cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            serviciosuser.setImagen(nombreImagen);
        } else {

        }
        model.addAttribute("userDto", userDto);
        serviciosuser.setActivo(activo);
        serviciouserservice.save(serviciosuser);
        // Agregar mensaje de error
        redirectAttributes.addFlashAttribute("serviciocreado", "Servicio creado con exito");
        return "redirect:/productos";
    }

    @GetMapping("/editarserviciouser/{id}")
    public String editarserviciouser(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        ServiciosUser serviciouser = serviciouserservice.get(id).orElse(new ServiciosUser());
        List<Categorias> categoriasServicio = categoriasService.findByTipoCategoria("Servicio");

        // Utiliza un nombre consistente para el atributo del modelo
        model.addAttribute("userDto", userDto);
        model.addAttribute("serviciouser", serviciouser);
        model.addAttribute("categoriasServicio", categoriasServicio);

        return "productos/editarserviciouser";
    }

    @PostMapping("/updateserviciouser")
    public String updateserviciouser(ServiciosUser serviciosuser, Model model, Authentication authentication, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("categoriaId") Integer categoriaId, RedirectAttributes redirectAttributes) throws IOException {

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

        ServiciosUser s = serviciouserservice.get(serviciosuser.getId()).orElse(new ServiciosUser());

        Categorias categoria = categoriasService.get(categoriaId).orElse(null);
        serviciosuser.setCategoria(categoria);

        if (file.isEmpty()) {//cuando editamos el producto pero no cambiamos la imagen
            serviciosuser.setImagen(s.getImagen());
        } else {//cuando editamos el producto y cambiamos la imagen

            //Eliminar cuando no sea la imagen por defecto
            if (!s.getImagen().equals("default.jpg")) {
                upload.deleteImage(s.getImagen());
            }

            String nombreImagen = upload.saveImage(file);
            serviciosuser.setImagen(nombreImagen);
        }
        model.addAttribute("userDto", userDto);
        serviciosuser.setUsuario(s.getUsuario());
        serviciouserservice.update(serviciosuser);
        // Agregar mensaje de error
        redirectAttributes.addFlashAttribute("servicioactualizado", "Servicio actualizado con exito");

        return "redirect:/productos";
    }

    @GetMapping("/deleteserviciouser/{id}")
    public String deleteserviciouser(@PathVariable Integer id, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {

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

        ServiciosUser s = new ServiciosUser();
        s = serviciouserservice.get(id).get();

        if (s != null) {
            // Desactivar el producto en lugar de borrarlo
            s.setActivos(false);
            serviciouserservice.update(s);

            // Eliminar la imagen solo si no es la imagen por defecto
            if (!s.getImagen().equals("default.jpg")) {
                upload.deleteImage(s.getImagen());
            }
        }

        // Agregar mensaje de error
        redirectAttributes.addFlashAttribute("servicioborrado", "Servicio eliminado con exito");
        return "redirect:/productos";
    }

    /*metodo pago*/
    @GetMapping("/crearmetodopago")
    public String createmetodopago(Model model, Authentication authentication, Principal principal) {
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
        return "productos/agregarmetodopago";
    }

    @PostMapping("/savemetodopago")
    public String savemetodopago(MetodoPago metodopago, Model model, Authentication authentication, Principal principal, @RequestParam("activo") boolean activo, RedirectAttributes redirectAttributes) throws IOException {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        // Obtener el nombre de usuario del usuario autenticado
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Buscar el usuario en la base de datos por su nombre de usuario
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
        metodopago.setActivo(activo);
        metodopagoservice.save(metodopago);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitometodopago", "Tipo Metodo de pago agregado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/editarmetodopago/{id}")
    public String editarmetodopago(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        MetodoPago metodopago = new MetodoPago();
        Optional<MetodoPago> optionalmetodopago = metodopagoservice.get(id);
        metodopago = optionalmetodopago.get();

        model.addAttribute("userDto", userDto);
        model.addAttribute("metodopago", metodopago);

        return "productos/editarmetodopago";
    }

    @PostMapping("/updatemetodopago")
    public String updatemetodopago(MetodoPago metodopago, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) throws IOException {

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

        MetodoPago m = new MetodoPago();
        m = metodopagoservice.get(metodopago.getId()).get();

        metodopago.setUsuario(m.getUsuario());
        metodopagoservice.update(metodopago);

        model.addAttribute("userDto", userDto);
        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitometodopagoactualizado", "Tipo Metodo de pago actualizado con éxito");
        return "redirect:/productos";
    }

    @GetMapping("/deletemetodopago/{id}")
    public String deletemetodopago(@PathVariable Integer id, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {

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

        MetodoPago m = new MetodoPago();
        m = metodopagoservice.get(id).get();

        if (m != null) {
            // Desactivar el producto en lugar de borrarlo
            m.setActivos(false);
            metodopagoservice.update(m);
        }

        model.addAttribute("userDto", userDto);

        // Agregar un mensaje de flash para mostrar en la vista
        redirectAttributes.addFlashAttribute("exitometodopagoeliminado", "Tipo metodo de pago eliminado con éxito");
        return "redirect:/productos";
    }

}
