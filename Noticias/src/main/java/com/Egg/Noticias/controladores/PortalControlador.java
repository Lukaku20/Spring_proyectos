
package com.Egg.Noticias.controladores;

import com.Egg.Noticias.NoticiaService.NoticiaService;
import com.Egg.Noticias.entidad.Noticia;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author siduncuNotebook
 */
@Controller
@RequestMapping("/")//localhost:8080/
public class PortalControlador {
    
    @Autowired
    private NoticiaService notiServi;
    
    @GetMapping("/")
    public String index(ModelMap model){
        List<Noticia> noticias = notiServi.listaDeNoticias();
        model.addAttribute("noticias", noticias);
        return "index.html";
    }
    
    @GetMapping("/registrar")//localhost:8080/registrar
    public String registrar(){
        return "registro.html";
    }
    
    @GetMapping("/login")//localhost:8080/login
    public String login(){
        return "login.html";
    }
}
