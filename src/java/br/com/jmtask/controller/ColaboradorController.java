/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.entity.Colaborador;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class ColaboradorController implements Serializable {

    @PersistenceContext
    private EntityManager em;
    @Resource
    UserTransaction ut;
    private Colaborador colaborador;
    private List<Colaborador> colaboradores;

    public Colaborador getColaborador() {
        if (colaborador == null) {
            colaborador = new Colaborador();
        }
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
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
            if (colaborador.getId() == null) {
                if (!existeColaborador(colaborador.getNome())) {
                    em.persist(colaborador);
                    mensagem = "Colaborador Salvo com Sucesso!";
                } else {
                    mensagem = "Já existe Colaborador(es) cadastrado(s) com esse nome no sistema!";
                }
            } else {
                em.merge(colaborador);
                mensagem = "Colaborador Atualizado com Sucesso!";
            }
            ut.commit();
            colaborador = new Colaborador();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Colaborador> getColaboradores() {
        Query query = em.createNamedQuery("Colaborador.findAll");
        colaboradores = query.getResultList();
        return colaboradores;
    }

    public List<Colaborador> getColaboradores(Long idProjeto) {
        Query query = em.createNamedQuery("Colaborador.findAll");
        colaboradores = query.getResultList();
        return colaboradores;
    }

    public String editarColaborador() {
        return "/colaborador.xhtml";
    }

    public void removerColaborador(Colaborador colaborador) {
        try {
            ut.begin();
            em.remove(em.getReference(Colaborador.class, colaborador.getId()));
            ut.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getColaboradores();
    }

    public Colaborador findByNome(String nomeColaborador) {
        Query query = em.createNamedQuery("Colaborador.verifColaborador");
        query.setParameter("nomeColaborador", nomeColaborador);
        try {
            colaborador = (Colaborador) query.getSingleResult();
        } catch (NoResultException nre) {
            nomeColaborador = "";
        }
        return colaborador;
    }

    public boolean existeColaborador(String nomeColaborador) {
        Query query = em.createNamedQuery("Colaborador.verifColaborador");
        query.setParameter("nomeColaborador", nomeColaborador);
        try {
            colaborador = (Colaborador) query.getSingleResult();
        } catch (NoResultException nre) {
            nomeColaborador = "";
        }
        if (!nomeColaborador.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}