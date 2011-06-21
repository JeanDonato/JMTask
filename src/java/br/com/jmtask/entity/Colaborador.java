/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

/**
 *
 * @author jeandonato
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Colaborador.findAll", query = "Select c from Colaborador c"),
    @NamedQuery(name = "Colaborador.verifColaborador", query = "Select c from Colaborador c where c.nome =:nomeColaborador")
})
public class Colaborador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Colaborador other = (Colaborador) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
