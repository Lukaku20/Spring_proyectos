package com.Egg.Noticias.controladores;

import com.Egg.Noticias.Enum.Rol;
import com.Egg.Noticias.NoticiaService.UsuarioService;
import com.Egg.Noticias.entidad.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author lukaku
 */
@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioService usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuarios_list.html";
    }

    @PostMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, @RequestParam("rol") Rol rol) {
        usuarioServicio.cambiarRol(id, rol);
        return "redirect:/admin/usuarios";
    }

}
