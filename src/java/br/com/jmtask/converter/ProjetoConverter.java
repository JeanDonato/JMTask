/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.converter;

import br.com.jmtask.controller.ProjetoController;
import br.com.jmtask.entity.Projeto;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jeandonato
 */
@Named
public class ProjetoConverter implements Converter{

    @Inject
    ProjetoController projetoController;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
           if (value == null) {
            return null;
        }
        try {
            if (value != null && (value.equals("-") || value.equals(""))) {
                value = "-1";
            }
            Projeto projeto = projetoController.findByNome(value);
            return projeto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }

    }
    
}
