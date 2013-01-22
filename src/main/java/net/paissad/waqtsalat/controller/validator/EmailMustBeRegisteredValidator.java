package net.paissad.waqtsalat.controller.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.service.AccountService;
import net.paissad.waqtsalat.util.EJBUtil;

@FacesValidator(EmailMustBeRegisteredValidator.VALIDATOR_ID)
public class EmailMustBeRegisteredValidator implements Validator {

    public static final String VALIDATOR_ID   = "emailMustBeRegisteredValidator";

    private AccountService     accountService = EJBUtil.lookup(AccountService.class);

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {

        final String email = (String) value;
        if (!this.accountService.isEmailAlreadyUsed(email)) {
            final String summary = I18N.getString("_login_Email_not_registered");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, summary));
        }
    }

}
