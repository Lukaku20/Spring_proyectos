
package com.Egg.Noticias.NoticiaService;

import com.Egg.Noticias.Enum.Rol;
import com.Egg.Noticias.entidad.Usuario;
import com.Egg.Noticias.excepciones.MyException;
import com.Egg.Noticias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author lukaku
 */

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Transactional
    public void registrar(String nombre, String email, String password, String password2) throws MyException, ValidationException {
        validar(nombre, email, password, password2);
        
        Usuario usuarioExistente = usuarioRepositorio.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new ValidationException("Ya existe un usuario registrado con ese email");
        }
        
        Usuario usuario = new Usuario();
        Date alta = new Date();
        usuario.setNombreUsuario(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        usuario.setFechaAlta(alta);
        
        usuarioRepositorio.save(usuario);
    }
    
    public void validar(String nombre, String email, String password, String password2) throws ValidationException {
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        Objects.requireNonNull(email, "El email no puede ser nulo");
        Objects.requireNonNull(password, "La contraseña no puede ser nula");

        if (nombre.isEmpty()) {
            throw new ValidationException("El nombre no puede estar vacío");
        }

        if (email.isEmpty()) {
            throw new ValidationException("El email no puede estar vacío");
        }

        if (password.length() <= 5) {
            throw new ValidationException("La contraseña debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new ValidationException("Las contraseñas ingresadas deben ser iguales");
        }

    }
    
    public Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepositorio.buscarPorEmail(email);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    
    
}
