/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.dao.ColaboradorDao;
import br.com.jmtask.dao.ProjetoDao;
import br.com.jmtask.entity.Colaborador;
import br.com.jmtask.entity.Projeto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.richfaces.event.DropEvent;

/**
 *
 * @author jeandonato
 */
@Named
@ConversationScoped
public class ProjetoController implements Serializable {

    @Inject
    Conversation conversation;
    private Projeto projeto;
    private Projeto projetoPesquisa;
    private List<Projeto> projetos;
    @Inject
    ColaboradorDao colaboradorDao;
    @Inject
    ProjetoDao projetoDao;
    private List<Colaborador> colaboradoresDisponiveis;
    private List<Colaborador> colaboradoresSelecionados = new ArrayList<Colaborador>();
    private Colaborador colaborador;
    String mensagem = "";

    public String novoProjeto() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        getProjeto();
        return "projeto";
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public List<Colaborador> getColaboradoresDisponiveis() {
        setColaboradoresDisponiveis(colaboradorDao.getColaboradores());
        return colaboradoresDisponiveis;
    }

    public void setColaboradoresDisponiveis(List<Colaborador> colaboradoresDisponiveis) {
        this.colaboradoresDisponiveis = colaboradoresDisponiveis;
    }

    public List<Colaborador> getColaboradoresSelecionados() {
        return colaboradoresSelecionados;
    }

    public void setColaboradoresSelecionados(List<Colaborador> colaboradoresSelecionados) {
        this.colaboradoresSelecionados = colaboradoresSelecionados;
    }

    public Projeto getProjeto() {
        if (projeto == null) {
            projeto = new Projeto();
        }
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Projeto getProjetoPesquisa() {
        return projetoPesquisa;
    }

    public void setProjetoPesquisa(Projeto projetoPesquisa) {
        this.projetoPesquisa = projetoPesquisa;
    }

    public List<Projeto> getProjetos() {
        projetos = projetoDao.getProjetos();
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    public void salvar() {
        try {
            if (projeto.getId() == null) {
                projeto.setColaboradores(colaboradoresSelecionados);
                projeto.setDataInicio(new Date());
                projeto.setStatus("1");
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
        if (!conversation.isTransient()) {
            conversation.end();
        }
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

    public void processDrop(DropEvent dropEvent) {
        Object produtoArrastado = dropEvent.getDragValue();
        Object dropValue = dropEvent.getDropValue();
        if ("SELECIONADO".equals(dropValue)) {
            if (colaboradoresDisponiveis.contains(produtoArrastado)) {
                int indiceProdutoSelecionado = colaboradoresDisponiveis.indexOf(produtoArrastado);
                Colaborador colaborador = new Colaborador();
                colaborador = (Colaborador) produtoArrastado;
                colaboradoresDisponiveis.remove(indiceProdutoSelecionado);
                colaboradoresSelecionados.add(colaborador);
            }
        }
    }

    public void reset() {
        colaboradoresSelecionados = new ArrayList<Colaborador>();
    }
}