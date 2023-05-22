
package com.Egg.Noticias.NoticiaService;

import com.Egg.Noticias.entidad.Noticia;
import com.Egg.Noticias.excepciones.MyException;
import com.Egg.Noticias.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 
 */
@Service
public class NoticiaService {
    @Autowired
    private NoticiaRepositorio notiRepo;
    
    @Transactional
    public void crearNoticia(String titulo, String cuerpo) throws MyException{
        validar(titulo, cuerpo);
        Noticia noti = new Noticia();
        noti.setTitulo(titulo);
        noti.setCuerpo(cuerpo);
//        noti.setFecha(new Date());
        notiRepo.save(noti);
    }
    
   // @Transactional()
    public List<Noticia> listaDeNoticias(){
        List<Noticia> noticias = new ArrayList();
        noticias = notiRepo.findAll();
        return noticias; 
    }
    
     public void buscarPorTitulo(String titulo){
        Noticia noti = notiRepo.findById(titulo).get();
    }
    
    public Noticia getOne(String id){
        
        return notiRepo.getOne(id);
    }
     
    @Transactional
    public void modificarNoticia(String id, String titulo, String cuerpo) throws MyException{
        validar(titulo, cuerpo);
        Optional<Noticia> respuesta = notiRepo.findById(id);
        if (respuesta.isPresent()) {
            Noticia noti = respuesta.get();
            noti.setTitulo(titulo);
            noti.setCuerpo(cuerpo);
            notiRepo.save(noti);
        }
    }
    
    public void eliminarNoticia(String id){
        Optional<Noticia> resp = notiRepo.findById(id);
        if (resp.isPresent()) {
            notiRepo.delete(resp.get());
        }
    }
    
    public void validar(String titulo, String cuerpo) throws MyException{
        if (titulo.isEmpty() || titulo == null) {
            throw new MyException("el tiutlo no puede ser nulo o estar vacio");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MyException("el cuerpo no puede ser nulo o estar vacio");
        }
    }
}
