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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author jeandonato
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Projeto.findAll", query = "Select p from Projeto p"),
    @NamedQuery(name = "Projeto.verifProjeto", query = "Select p from Projeto p where p.nome =:nomeProjeto")
})
public class Projeto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dataInicio;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dataFim;
//    @ManyToMany
//    private List<Colaborador> colaboradores;
//    private char status;

//    public List<Colaborador> getColaboradores() {
//        return colaboradores;
//    }
//
//    public void setColaboradores(List<Colaborador> colaboradores) {
//        this.colaboradores = colaboradores;
//    }
//    public Date getDataFim() {
//        return dataFim;
//    }
//
//    public void setDataFim(Date dataFim) {
//        this.dataFim = dataFim;
//    }
//
//    public Date getDataInicio() {
//        return dataInicio;
//    }
//
//    public void setDataInicio(Date dataInicio) {
//        this.dataInicio = dataInicio;
//    }
//
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

//    public char getStatus() {
//        return status;
//    }
//
//    public void setStatus(char status) {
//        this.status = status;
//    }
//
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Projeto other = (Projeto) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
