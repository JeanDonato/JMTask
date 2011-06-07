/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmtask.converter;

import br.com.jmtask.controller.ColaboradorController;
import br.com.jmtask.entity.Colaborador;
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
public class ColaboradorConverter implements Converter {

    @Inject
    ColaboradorController colaboradorController;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        try {
            if (value != null && (value.equals("-") || value.equals(""))) {
                value = "-1";
            }
            Colaborador colaborador = colaboradorController.findByNome(value);
            return colaborador;
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
