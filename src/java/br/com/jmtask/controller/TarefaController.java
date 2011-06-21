/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.controller;

import br.com.jmtask.dao.ColaboradorDao;
import br.com.jmtask.dao.ProjetoDao;
import br.com.jmtask.dao.TarefaDao;
import br.com.jmtask.entity.Colaborador;
import br.com.jmtask.entity.LogTarefa;
import br.com.jmtask.entity.Projeto;
import br.com.jmtask.entity.Tarefa;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class TarefaController implements Serializable {

    private Tarefa tarefa;
    private Tarefa tarefaOld = new Tarefa();
    private List<Colaborador> colaboradores;
    private List<Projeto> projetos;
    private List<Tarefa> tarefas;
    private LogTarefa logTarefa = new LogTarefa();
    private List<LogTarefa> logsDaTarefaSelecionada;
    @Inject
    ColaboradorDao colaboradorDao;
    @Inject
    ProjetoDao projetoDao;
    @Inject
    TarefaDao tarefaDao;
    private Long idDaTarefaSelecionada;
    Map parametros = new HashMap();
    String mensagem = "";
    private boolean tarefaPodeSerEditada;

    public boolean isTarefaPodeSerEditada() {
        if (!tarefa.getStatus().equals("3")) {
            setTarefaPodeSerEditada(true);
        }
        return tarefaPodeSerEditada;
    }

    public void setTarefaPodeSerEditada(boolean tarefaPodeSerEditada) {
        this.tarefaPodeSerEditada = tarefaPodeSerEditada;
    }

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
        colaboradores = colaboradorDao.getColaboradores();
        return colaboradores;
    }

    public List<Projeto> getProjetos() {
        projetos = projetoDao.getProjetos();
        return projetos;
    }

    public String salvar() {
        try {
            if (tarefa.getId() == null) {
                tarefa.setStatus("1");
                tarefaDao.salvar(tarefa);
                mensagem = "Tarefa Salva com Sucesso!";
                tarefa = new Tarefa();
            } else {
//                setando esses atributos, pois, eles não estão na tela. Assim não veem para o servidor, sendo necessário fazer isso, ou colocar campo hidden na view
                tarefaOld = getTarefa(tarefa.getId());
                tarefa.setColaborador(tarefaOld.getColaborador());
                tarefa.setProjeto(tarefaOld.getProjeto());
                tarefa.setDataInicial(tarefaOld.getDataInicial());
                //se tiver finalizando a tarefa...
                if (tarefa.getStatus().equals("3")) {
                    tarefa.setDataFinal(new Date());
                }
                tarefaDao.atualizar(tarefa);
                SalvarLog();
                mensagem = "Tarefa Atualizada com Sucesso!";
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensagem));
            getTarefas();
            return "listaTarefas";

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void SalvarLog() {
        try {
            logTarefa.setTarefa(tarefa);
            logTarefa.setDataLog(null);
            tarefaDao.SalvarLog(logTarefa);
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

        if (tarefa != null) {
            if (tarefa.getStatus() != null) {
                parametros.put("status", tarefa.getStatus());
            }

            if (tarefa.getColaborador() != null) {
                parametros.put("colaborador", tarefa.getColaborador().getId());
            }

            if (tarefa.getProjeto() != null) {
                parametros.put("projeto", tarefa.getProjeto().getId());
            }
        }
        tarefas = tarefaDao.getTarefas(sqlFiltro, parametros);
        return tarefas;
    }

    public List<LogTarefa> getLogsDaTarefaSelecionada() {
        if (tarefa != null) {
            if (tarefa.getId() != null) {
                logsDaTarefaSelecionada = tarefaDao.getLogsDaTarefaSelecionada(tarefa);
            } else {
                logsDaTarefaSelecionada = null;
            }
        } else {
            logsDaTarefaSelecionada = null;
        }
        return logsDaTarefaSelecionada;
    }

    public Tarefa getTarefa(Long id) {
        tarefaOld = tarefaDao.getTarefa(id);
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
