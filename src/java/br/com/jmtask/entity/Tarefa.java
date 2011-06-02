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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 *
 * @author jeandonato
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Tarefa.findAll", query = "Select t from Tarefa t"),
    @NamedQuery(name = "Tarefa.findById", query = "Select t from Tarefa t where t.id=:id"),
    @NamedQuery(name = "Tarefa.findByStatus", query = "Select t from Tarefa t where t.status=:status")
})
public class Tarefa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Colaborador colaborador;
    @ManyToOne
    private Projeto projeto;
    private String status;
    private String descricao;
    private String obs;
    @Transient
    private String statusDesc;
    @Transient
    private Boolean tarefaEstaFinalizada;

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public String getStatus() {
//        if (status == null || status.isEmpty()) {
//            setStatus("1");
//        }
        return status;
    }

    public Boolean getTarefaEstaFinalizada() {
        if (status != null) {
            if (status.equals("3")) {
                setTarefaEstaFinalizada(true);
            }
        }
        return tarefaEstaFinalizada;
    }

    public void setTarefaEstaFinalizada(Boolean tarefaEstaFinalizada) {
        this.tarefaEstaFinalizada = tarefaEstaFinalizada;
    }

    public String getStatusDesc() {
        if (status.equals("1")) {
            return "Em Andamento";
        } else if (status.equals("2")) {
            return "Aguardando Retorno";
        } else if (status.equals("3")) {
            return "Finalizada";
        } else if (status.equals("4")) {
            return "Cancelada";
        } else {
            return "";
        }
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tarefa other = (Tarefa) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
