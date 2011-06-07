/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.dao.ProjetoDao;
import br.com.jmtask.entity.Colaborador;
import br.com.jmtask.entity.Projeto;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jeandonato
 */
@Named
@RequestScoped
public class ProjetoController implements Serializable {

    private Projeto projeto;
    private Projeto projetoPesquisa;
    private List<Projeto> projetos;
    @Inject
    ColaboradorController colaboradorController;
    @Inject
    ProjetoDao projetoDao;
    String mensagem = "";

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

    public void salvar() {
        try {
            if (projeto.getId() == null) {
                projetoDao.salvar(projeto);
                mensagem = "Projeto Salvo com Sucesso!";
            } else {
                projetoDao.atualizar(projeto);
                mensagem = "Projeto Atualizado com Sucesso!";
            }
            projeto = new Projeto();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Projeto> getProjetos() {
        projetos = projetoDao.getProjetos();
        return projetos;
    }

    public String editarProjeto() {
        return "/projeto.xhtml";
    }

    public void removerProjeto(Projeto projeto) {
        boolean projetotemTarefa = projetoDao.projetoTemTarefa(projeto);
        if (projetotemTarefa) {
            mensagem = "Projeto não pode ser removido, pois possui tarefa(s)";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } else {
            projetoDao.remover(projeto);
        }
        getProjetos();
    }

    public Projeto findByNome(String nomeProjeto) {
        projetoPesquisa = projetoDao.findByNome(nomeProjeto);
        return projetoPesquisa;
    }
}