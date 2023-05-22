
package com.Egg.Noticias.repositorios;

import com.Egg.Noticias.entidad.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * métodos necesarios para guardar/actualizar noticias en la base de datos, 
 * realizar consultas o dar de baja según corresponda.
 * @author 
 */

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String>{
    

    
}
