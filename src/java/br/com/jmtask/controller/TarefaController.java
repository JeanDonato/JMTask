/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.entity.Colaborador;
import br.com.jmtask.entity.LogTarefa;
import br.com.jmtask.entity.Projeto;
import br.com.jmtask.entity.Tarefa;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author jeandonato
 */
@Named
@RequestScoped
public class TarefaController implements Serializable {

    @PersistenceContext
    private EntityManager em;
    @Resource
    UserTransaction ut;
    private Tarefa tarefa;
    private Tarefa tarefaOld = new Tarefa();
    private List<Colaborador> colaboradores;
    private List<Projeto> projetos;
    private List<Tarefa> tarefas;
    private LogTarefa logTarefa = new LogTarefa();
    private List<LogTarefa> logsDaTarefaSelecionada;
    @Inject
    ColaboradorController colaboradorController;
    @Inject
    ProjetoController projetoController;
    
    private Long idDaTarefaSelecionada;

    public Long getIdDaTarefaSelecionada() {
        return idDaTarefaSelecionada;
    }

    public void setIdDaTarefaSelecionada(Long idDaTarefaSelecionada) {
        this.idDaTarefaSelecionada = idDaTarefaSelecionada;
    }
    
    
    public Tarefa getTarefa() {
        if (tarefa == null) {
            tarefa = new Tarefa();
        }
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public LogTarefa getLogTarefa() {
        return logTarefa;
    }

    public List<Colaborador> getColaboradores() {
        colaboradores = colaboradorController.getColaboradores();
        return colaboradores;
    }

    public List<Projeto> getProjetos() {
        projetos = projetoController.getProjetos();
        return projetos;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void salvar() {
        String mensagem = "";
        try {
            ut.begin();
            if (tarefa.getId() == null) {
                tarefa.setStatus("1");
                em.persist(tarefa);
                mensagem = "Tarefa Salva com Sucesso!";
                tarefa = new Tarefa();
            } else {
                tarefaOld = getTarefa(tarefa.getId());
                tarefa.setColaborador(tarefaOld.getColaborador());
                tarefa.setProjeto(tarefaOld.getProjeto());
                em.merge(tarefa);
                mensagem = "Tarefa Atualizada com Sucesso!";
            }
            ut.commit();
            if (tarefa.getId() != null) {
                SalvarLog();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SalvarLog() {
        try {
            ut.begin();
            logTarefa.setTarefa(tarefa);
            em.persist(logTarefa);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Tarefa> getTarefas() {
        String sqlFiltro = "Select * from Tarefa t";

        if (tarefa != null) {
            if (tarefa.getStatus() != null || tarefa.getColaborador() != null || tarefa.getProjeto() != null) {
                sqlFiltro = sqlFiltro + " where t.status<>''";
            }
            if (tarefa.getStatus() != null) {
                sqlFiltro = sqlFiltro + " and t.status=:status";
            }

            if (tarefa.getColaborador() != null) {
                sqlFiltro = sqlFiltro + " and t.colaborador_id=:colaborador";
            }

            if (tarefa.getProjeto() != null) {
                sqlFiltro = sqlFiltro + " and t.projeto_id=:projeto";
            }
        }

        Query query = em.createNativeQuery(sqlFiltro, Tarefa.class);

        if (tarefa != null) {
            if (tarefa.getStatus() != null) {
                query.setParameter("status", tarefa.getStatus());
            }

            if (tarefa.getColaborador() != null) {
                query.setParameter("colaborador", tarefa.getColaborador().getId());
            }

            if (tarefa.getProjeto() != null) {
                query.setParameter("projeto", tarefa.getProjeto().getId());
            }
        }

        tarefas = query.getResultList();
        return tarefas;

    }

    public List<LogTarefa> getLogsDaTarefaSelecionada() {
        if (tarefa != null) {
            if (tarefa.getId() != null) {
                Query query = em.createNamedQuery("LogTarefa.findByTarefa");
                query.setParameter("tarefa", tarefa);
                logsDaTarefaSelecionada = query.getResultList();
            } else {
                logsDaTarefaSelecionada = null;
            }
        } else {
            logsDaTarefaSelecionada = null;
        }
        return logsDaTarefaSelecionada;
    }

    public Tarefa getTarefa(Long id) {
        Query query = em.createNamedQuery("Tarefa.findById");
        query.setParameter("id", id);
        tarefaOld = (Tarefa) query.getSingleResult();
        return tarefaOld;
    }

    public String editarTarefa() {
        return "/tarefaEditar.xhtml";
    }

    public String novoStatus() {
        return "/novoStatusTarefa.xhtml";
    }
    //    public void removerTarefa(Tarefa tarefa) {
//        try {
//            ut.begin();
//            em.remove(em.getReference(Tarefa.class, tarefa.getId()));
//            ut.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        getTarefaes();
//    }
//
//    public Tarefa findByNome(String nomeTarefa) {
//        Query query = em.createNamedQuery("Tarefa.verifTarefa");
//        query.setParameter("nomeTarefa", nomeTarefa);
//        try {
//            tarefa = (Tarefa) query.getSingleResult();
//        } catch (NoResultException nre) {
//            nomeTarefa = "";
//        }
//        return tarefa;
//    }
//
//    public boolean existeTarefa(String nomeTarefa) {
//        Query query = em.createNamedQuery("Tarefa.verifTarefa");
//        query.setParameter("nomeTarefa", nomeTarefa);
//        try {
//            tarefa = (Tarefa) query.getSingleResult();
//        } catch (NoResultException nre) {
//            nomeTarefa = "";
//        }
//        if (!nomeTarefa.equals("")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
