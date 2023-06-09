package com.Egg.Noticias.controladores;

import com.Egg.Noticias.NoticiaService.NoticiaService;
import com.Egg.Noticias.NoticiaService.UsuarioService;
import com.Egg.Noticias.entidad.Noticia;
import com.Egg.Noticias.entidad.Usuario;
import com.Egg.Noticias.excepciones.MyException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lukaku
 */
@Controller
@RequestMapping("/noticias")//localhost:8080/noticias
public class NoticiaControlador {

    @Autowired
    private NoticiaService notiServi;
    @Autowired
    private UsuarioService userServi;
    @GetMapping("/registrar") //localhost:8080/noticias/registrar
    public String cargar() {
        return "formulario_noticia.html";
    }
    // @RequestParam Date fecha

    @PostMapping("/registro") //localhost:8080/noticias/registro
    @PreAuthorize("hasAnyRole('PERIODISTA', 'ADMIN')")
    public String registro(@RequestParam String titulo, @RequestParam String cuerpo, ModelMap model, HttpSession session) {
        try {
            List<Usuario> usuarios = userServi.listarUsuarios();

            model.addAttribute("usuarios", usuarios);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            System.out.println(logueado.getNombreUsuario());
            notiServi.crearNoticia(titulo, cuerpo, logueado);
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
    @PreAuthorize("hasRole('USER')")
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

    @GetMapping("/ver_noti/{id}")
    public String verNoticia(ModelMap model, @PathVariable String id) {
        Noticia noticia = notiServi.getOne(id);
        model.put("noticia", noticia);
        return "ver.html";
    }
}
