/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.entity.Colaborador;
import br.com.jmtask.entity.Projeto;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author jeandonato
 */
@Named
@RequestScoped
public class ProjetoController implements Serializable {

    @PersistenceContext
    private EntityManager em;
    @Resource
    UserTransaction ut;
    private Projeto projeto;
    private List<Projeto> projetos;
    @Inject
    ColaboradorController colaboradorController;

    public Projeto getProjeto() {
        if (projeto == null) {
            projeto = new Projeto();
//            projeto.setColaboradores(colaboradoresDisponiveis());
        } else {
        }
        return projeto;
    }

    public List<Colaborador> colaboradoresDisponiveis() {
        return colaboradorController.getColaboradores();
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
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
            if (projeto.getId() == null) {
                em.persist(projeto);
                mensagem = "Projeto Salvo com Sucesso!";
            } else {
                em.merge(projeto);
                mensagem = "Projeto Atualizado com Sucesso!";
            }
            ut.commit();
            projeto = new Projeto();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Projeto> getProjetos() {
        Query query = em.createNamedQuery("Projeto.findAll");
        projetos = query.getResultList();
        return projetos;
    }

    public String editarProjeto() {
        return "/projeto.xhtml";
    }

    public void removerProjeto(Projeto projeto) {
        try {
            ut.begin();
            em.remove(em.getReference(Projeto.class, projeto.getId()));
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getProjetos();
    }

    public Projeto findByNome(String nomeProjeto) {
        Query query = em.createNamedQuery("Projeto.verifProjeto");
        query.setParameter("nomeProjeto", nomeProjeto);
        try {
            projeto = (Projeto) query.getSingleResult();
        } catch (NoResultException nre) {
            nomeProjeto = "";
        }
        return projeto;
    }
}