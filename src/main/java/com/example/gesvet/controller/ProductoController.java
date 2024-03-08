package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.Productos;
import com.example.gesvet.models.Servicios;
import com.example.gesvet.models.ServiciosUser;
import com.example.gesvet.models.Tipocategoria;
import com.example.gesvet.models.User;
import com.example.gesvet.service.ICategoriasService;
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
import java.security.Principal;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/productos")
public class ProductoController {

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
    public String save(@ModelAttribute Productos productos, Model model, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("activo") boolean activo) throws IOException {

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
    public String update(Productos producto, Model model, Authentication authentication, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("categoriaId") Integer categoriaId) throws IOException {

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
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
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
    public String saveCategoria(@ModelAttribute Categorias categoria, Model model, Principal principal, @RequestParam("activo") boolean activo) {
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
        categoria.setActivo(activo);
        categoriasService.save(categoria);
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
    public String updateCategoria(@ModelAttribute Categorias categoria, Model model, Principal principal) {
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
        categoriasService.update(categoria);
        return "redirect:/productos";
    }

    @GetMapping("/deletecategoria/{id}")
    public String deleteCategoria(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
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
    public String saveservicio(Servicios servicios, Model model, Authentication authentication, Principal principal, @RequestParam("activo") boolean activo) throws IOException {

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
    public String updateservicio(Servicios servicio, Model model, Authentication authentication, Principal principal) throws IOException {

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
        return "redirect:/productos";
    }

    @GetMapping("/deleteservicio/{id}")
    public String deleteservicio(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        model.addAttribute("userDto", userDto);
        serviciosService.delete(id);
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
    public String savetipocategoria(Tipocategoria tipocategoria, Model model, Authentication authentication, Principal principal, @RequestParam("activo") boolean activo) throws IOException {

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
        tipocategoria.setActivo(activo);
        tipocategoriaservice.save(tipocategoria);
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
    public String updatetipocategoria(Tipocategoria tipocategoria, Model model, Authentication authentication, Principal principal) throws IOException {

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

        Tipocategoria t = new Tipocategoria();
        t = tipocategoriaservice.get(tipocategoria.getId()).get();

        tipocategoria.setUsuario(t.getUsuario());
        tipocategoriaservice.update(tipocategoria);

        model.addAttribute("userDto", userDto);
        return "redirect:/productos";
    }

    @GetMapping("/deletetipocategoria/{id}")
    public String deletetipocategoria(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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

        Tipocategoria t = new Tipocategoria();
        t = tipocategoriaservice.get(id).get();

        model.addAttribute("userDto", userDto);
        tipocategoriaservice.delete(id);
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
    public String saveserviciouser(@ModelAttribute ServiciosUser serviciosuser, Model model, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("activo") boolean activo) throws IOException {

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
    public String updateserviciouser(ServiciosUser serviciosuser, Model model, Authentication authentication, Principal principal, @RequestParam("img") MultipartFile file, @RequestParam("categoriaId") Integer categoriaId) throws IOException {

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
        return "redirect:/productos";
    }

    @GetMapping("/deleteserviciouser/{id}")
    public String deleteserviciouser(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {

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
        return "redirect:/productos";
    }

}
