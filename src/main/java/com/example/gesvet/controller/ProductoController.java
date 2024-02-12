package com.example.gesvet.controller;

import com.example.gesvet.models.Productos;
import com.example.gesvet.models.Usuario;
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

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String showProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/Gestion_Productos_Servicios";
    }

    @GetMapping("/crear")
    public String create() {
        return "productos/agregar";
    }

    @PostMapping("/save")
    public String save(Productos productos, @RequestParam("img") MultipartFile file) throws IOException {

        Usuario u = new Usuario(1, "", "", "", "", "", "", "", "");
        productos.setUsuario(u);

        //imagen
        if (productos.getId() == null) {//cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            productos.setImagen(nombreImagen);
        } else {

        }

        productoService.save(productos);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Productos producto = new Productos();
        Optional<Productos> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();
        model.addAttribute("producto", producto);
        return "productos/editar";
    }

    @PostMapping("/update")
    public String update(Productos producto, @RequestParam("img") MultipartFile file) throws IOException {

        Productos p = new Productos();
        p = productoService.get(producto.getId()).get();

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

        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Productos p = new Productos();
        p = productoService.get(id).get();

        //Eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }

}
