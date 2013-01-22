package net.paissad.waqtsalat.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("helper")
@RequestScoped
public class ControllerHelper extends AbstractController {

    private static final long serialVersionUID = 1L;

    @Override
    public String getApplicationURL() {
        return super.getApplicationURL();
    }

}
