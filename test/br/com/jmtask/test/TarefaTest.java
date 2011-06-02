/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.test;

import org.junit.Test;
import br.com.jmtask.controller.TarefaController;
import br.com.jmtask.entity.Tarefa;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.easymock.EasyMock.*;

/**
 *
 * @author jeandonato
 */
public class TarefaTest {

    private Tarefa tarefaMock;
    private TarefaController tarefaController;

    public TarefaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        tarefaMock = createMock(Tarefa.class);
        tarefaController = new TarefaController();
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void SalvarTarefa() {
 //        tarefaController.salvar(tarefaMock);
         
     }
}
