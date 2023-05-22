package com.Egg.Noticias.controladores;

import com.Egg.Noticias.NoticiaService.NoticiaService;
import com.Egg.Noticias.entidad.Noticia;
import com.Egg.Noticias.excepciones.MyException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author
 */
@Controller
@RequestMapping("/noticias")//localhost:8080/noticias
public class NoticiaControlador {

    @Autowired
    private NoticiaService notiServi;

    @GetMapping("/registrar") //localhost:8080/noticias/registrar
    public String cargar() {
        return "formulario_noticia.html";
    }
    // @RequestParam Date fecha

    @PostMapping("/registro") //localhost:8080/noticias/registro
    public String registro(@RequestParam String titulo, @RequestParam String cuerpo, ModelMap model) {
        try {
            notiServi.crearNoticia(titulo, cuerpo);
            model.put("exito", "La noticia se guardó con éxito");
        } catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "formulario_noticia.html";
        }
        return "index.html";
    }

    @GetMapping("/listado")
    public String listado(ModelMap model) {
        List<Noticia> noticias = notiServi.listaDeNoticias();
        model.addAttribute("noticias", noticias);
        return "listado_noticias.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap model, @PathVariable String id) {
        model.put("noticia", notiServi.getOne(id));
        return "noticia_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modifico(@PathVariable String id, @RequestParam String titulo, @RequestParam String cuerpo, ModelMap model) {
        try {
            notiServi.modificarNoticia(id, titulo, cuerpo);
            model.put("exito", "La noticia se modificó con éxito");
            return "redirect:../listado";
        } catch (MyException ex) {

            model.put("error", ex.getMessage());
            return "formulario_noticia.html";
        }
    }

    @PostMapping("/borrar/{id}")
    public String eliminarNoticia(ModelMap model, @PathVariable String id) {
        try {
            notiServi.eliminarNoticia(id);
            model.put("exito", "La noticia se eliminó con éxito");
            return "redirect:../listado";
        } catch (Exception e) {
            return "redirect:../listado";
        }
        
    }
}
