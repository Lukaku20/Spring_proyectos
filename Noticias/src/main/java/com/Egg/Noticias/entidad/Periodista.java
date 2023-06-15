
package com.Egg.Noticias.entidad;

import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

/**
 * @author lukaku
 */
@Entity
@Table(name="periodistas")
public class Periodista extends Usuario{

    private ArrayList<Noticia> misNoticias;
    @Column(name="sueldo")
    private Integer sueldo;

    public Periodista() {
    }

    public ArrayList<Noticia> getMisNoticias() {
        return misNoticias;
    }

    public void setMisNoticias(ArrayList<Noticia> misNoticias) {
        this.misNoticias = misNoticias;
    }

    public Integer getSueldo() {
        return sueldo;
    }

    public void setSueldo(Integer sueldo) {
        this.sueldo = sueldo;
    }
    
    
}
