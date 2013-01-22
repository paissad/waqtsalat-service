package net.paissad.waqtsalat.controller.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.paissad.waqtsalat.servlet.CaptchaServlet;

@FacesValidator(CaptchaValidator.VALIDATOR_ID)
public class CaptchaValidator implements Validator {

    public static final String VALIDATOR_ID = "captchaValidator";

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {

        final String enteredValue = (String) value;
        final String expectedValue = (String) context.getExternalContext().getSessionMap()
                .get(CaptchaServlet.CAPTCHA_KEY);

        final boolean ok = (enteredValue != null) && (enteredValue.equals(expectedValue));
        if (!ok) {
            String summary = null;
            final String uiComponentParamId = component.getNamingContainer().getClientId() + ":failureMessageId";
            for (final UIComponent ui : component.getChildren()) {
                if (uiComponentParamId.equals(ui.getClientId())) {
                    summary = (String) ((UIParameter) ui).getValue();
                    break;
                }
            }
            // TODO : This should be available for internationalization
            if (summary == null) summary = "Captcha validation failed";
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, summary));
        }
    }

}
