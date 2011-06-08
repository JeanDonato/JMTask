/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.dao;

import br.com.jmtask.entity.LogTarefa;
import br.com.jmtask.entity.Tarefa;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author jeandonato
 * 
 */
public class TarefaDao {

    @PersistenceContext
    private EntityManager em;
    @Resource
    UserTransaction ut;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void salvar(Tarefa tarefa) {
        try {
            ut.begin();
            em.persist(tarefa);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu salvar tarefa #########################");
        }
    }

    public void atualizar(Tarefa tarefa) {
        try {
            ut.begin();
            em.merge(tarefa);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu atualizar tarefa #########################");
        }
    }

    public List<Tarefa> getTarefas(String sql, Map<String, Object> parametros) {

        Query query = em.createNativeQuery(sql, Tarefa.class);

        if (!parametros.isEmpty()) {
            if (parametros.containsKey("colaborador")) {
                query.setParameter("colaborador", parametros.get("colaborador"));
            }
            if (parametros.containsKey("status")) {
                query.setParameter("status", parametros.get("status"));
            }
            if (parametros.containsKey("projeto")) {
                query.setParameter("projeto", parametros.get("projeto"));
            }
        }
        return query.getResultList();
    }

    public Tarefa getTarefa(Long id) {
        Query query = em.createNamedQuery("Tarefa.findById");
        query.setParameter("id", id);
        return (Tarefa) query.getSingleResult();
    }

// ################# LOG DAS TAREFAS #################
    public List<LogTarefa> getLogsDaTarefaSelecionada(Tarefa tarefa) {
        Query query = em.createNamedQuery("LogTarefa.findByTarefa");
        query.setParameter("tarefa", tarefa);
        return query.getResultList();
    }

    public void SalvarLog(LogTarefa logTarefa) {
        try {
            ut.begin();
            em.persist(logTarefa);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ################# LOG DAS TAREFAS #################
}
