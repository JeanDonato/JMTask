/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.dao.ColaboradorDao;
import br.com.jmtask.entity.Colaborador;
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
public class ColaboradorController implements Serializable {

    @Inject
    private ColaboradorDao colaboradorDao;
    private Colaborador colaborador;
    private Colaborador colaboradorPesquisa;
    private List<Colaborador> colaboradores;
    String mensagem = "";

    public Colaborador getColaborador() {
        if (colaborador == null) {
            colaborador = new Colaborador();
        }
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public void salvar() {
        try {
            if (colaborador.getId() == null) {
                if (!existeColaborador(colaborador.getNome())) {
                    colaboradorDao.salvar(colaborador);
                    mensagem = "Colaborador Salvo com Sucesso!";
                } else {
                    mensagem = "Já existe Colaborador(es) cadastrado(s) com esse nome no sistema!";
                }
            } else {
                colaboradorDao.atualizar(colaborador);
                mensagem = "Colaborador Atualizado com Sucesso!";
            }
            colaborador = new Colaborador();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Colaborador> getColaboradores() {
        colaboradores = colaboradorDao.getColaboradores();
        return colaboradores;
    }

    public List<Colaborador> getColaboradores(Long idProjeto) {
        colaboradores = colaboradorDao.getColaboradores(idProjeto);
        return colaboradores;
    }

    public String editarColaborador() {
        return "/colaborador.xhtml";
    }

    public void removerColaborador(Colaborador colaborador) {
        boolean colaboradortemTarefa = colaboradorDao.colaboradorTemTarefa(colaborador);
        if (colaboradortemTarefa) {
            mensagem = "Colaborador não pode ser removido, pois possui tarefa(s)";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
        } else {
            colaboradorDao.remover(colaborador);
        }
        getColaboradores();
    }

    public Colaborador findByNome(String nomeColaborador) {
        colaboradorPesquisa = colaboradorDao.findByNome(nomeColaborador);
        return colaboradorPesquisa;
    }

    public boolean existeColaborador(String nomeColaborador) {
        colaboradorPesquisa = findByNome(nomeColaborador);
        if (colaboradorPesquisa != null) {
            return true;
        } else {
            return false;
        }
    }
}