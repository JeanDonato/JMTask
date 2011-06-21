/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.dao;

import br.com.jmtask.entity.Colaborador;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author jeandonato
 */
public class ColaboradorDao implements Serializable {

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

    public void salvar(Colaborador colaborador) {
        try {
            ut.begin();
            em.persist(colaborador);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu salvar colaborador #########################");
        }
    }

    public void atualizar(Colaborador colaborador) {
        try {
            ut.begin();
            em.merge(colaborador);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu atualizar colaborador #########################");
        }

    }

    public void remover(Colaborador colaborador) {
        try {
            ut.begin();
            em.remove(em.getReference(Colaborador.class, colaborador.getId()));
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu remover colaborador #########################");
        }
    }

    public List<Colaborador> getColaboradores() {
        Query query = em.createNamedQuery("Colaborador.findAll");
        return query.getResultList();
    }

    public List<Colaborador> getColaboradores(Long idProjeto) {
        Query query = em.createNamedQuery("Colaborador.findAll");
        return query.getResultList();
    }

    public Colaborador findByNome(String nomeColaborador) {
        Query query = em.createNamedQuery("Colaborador.verifColaborador");
        query.setParameter("nomeColaborador", nomeColaborador);
        try {
            return (Colaborador) query.getSingleResult();
        } catch (NoResultException nre) {
            System.out.println("pesquisa sem nenhum resultado para esse nomeColaborador");
            return null;
        }
    }

    public boolean colaboradorTemTarefa(Colaborador colaborador) {
        Query query = em.createNamedQuery("Tarefa.findByColaborador");
        query.setParameter("colaborador", colaborador);
        if (query.getResultList().size() > 0) {
            System.out.println("################# Colaborador tem tarefa, nao podera ser removido #################");
            return true;
        } else {
            return false;
        }
    }
}
