package com.Egg.Noticias.controladores;

import com.Egg.Noticias.NoticiaService.NoticiaService;
import com.Egg.Noticias.NoticiaService.UsuarioService;
import com.Egg.Noticias.entidad.Noticia;
import com.Egg.Noticias.entidad.Usuario;
import com.Egg.Noticias.excepciones.MyException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lukaku
 */
@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private NoticiaService notiServi;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String index(ModelMap model) {
        List<Noticia> noticias = notiServi.listaDeNoticias();
        model.addAttribute("noticias", noticias);
        return "index.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap model, HttpSession session) {
        List<Noticia> noticias = notiServi.listaDeNoticias();
        model.addAttribute("noticias", noticias);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//        if(logueado.getRol().toString().equals("ADMIN")){
//            return "redirect:/admin/dashbord";
//        }
        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registrar") // Cambiado a POST en lugar de GET
    public String registrar(@RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2,
            MultipartFile imagen) {
        try {
            usuarioService.registrar(imagen, nombre, email, password, password2);
            // Registro exitoso, redirigir a la página de inicio de sesión
            return "redirect:/login";
        } catch (MyException | ValidationException e) {
            // Error durante el registro, mostrar mensaje de error en la página de registro
            // Puedes agregar el mensaje de error a través del Model y mostrarlo en la plantilla
            // o redirigir a una página de error personalizada
            return "registro.html";
        }
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Usuario usuario = usuarioService.obtenerUsuarioActual();
            if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isEmpty()) {
                // El usuario no ha establecido su información personal
                return "redirect:/completar-informacion-usuario";
            } else {
                // El usuario ya ha establecido su información personal
                return "redirect:/inicio";
            }
        }
        return "login.html";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout"; // Redirige a la página de inicio de sesión con un parámetro de logout para mostrar un mensaje de éxito
    }
}
