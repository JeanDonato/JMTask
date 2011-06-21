/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.dao;

import br.com.jmtask.entity.Projeto;
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
public class ProjetoDao implements Serializable{

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

    public void salvar(Projeto projeto) {
        try {
            ut.begin();
            em.persist(projeto);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu salvar projeto #########################");
        }
    }

    public void atualizar(Projeto projeto) {
        try {
            ut.begin();
            em.merge(projeto);
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu atualizar projeto #########################");
        }
    }

    public List<Projeto> getProjetos() {
        Query query = em.createNamedQuery("Projeto.findAll");
        return query.getResultList();
    }

    public void remover(Projeto projeto) {
        try {
            ut.begin();
            em.remove(em.getReference(Projeto.class, projeto.getId()));
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######################### Nao conseguiu remover projeto #########################");
        }
    }

    public boolean projetoTemTarefa(Projeto projeto) {
        Query query = em.createNamedQuery("Tarefa.findByProjeto");
        query.setParameter("projeto", projeto);
        if (query.getResultList().size() > 0) {
            System.out.println("################# Projeto tem tarefa, nao podera ser removido #################");
            return true;
        } else {
            return false;
        }
    }

    public Projeto findByNome(String nomeProjeto) {
        Query query = em.createNamedQuery("Projeto.verifProjeto");
        query.setParameter("nomeProjeto", nomeProjeto);
        try {
            return (Projeto) query.getSingleResult();
        } catch (NoResultException nre) {
            System.out.println("pesquisa sem nenhum resultado para esse nomeProjeto");
            return null;
        }
    }
}
