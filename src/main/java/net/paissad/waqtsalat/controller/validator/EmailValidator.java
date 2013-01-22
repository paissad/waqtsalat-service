package net.paissad.waqtsalat.controller.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.paissad.waqtsalat.I18N;

@FacesValidator(EmailValidator.VALIDATOR_ID)
public class EmailValidator implements Validator {

    public static final String  VALIDATOR_ID = "emailValidator";

    private static final String EMAIL_REGEX  = "^[A-Z\\d._%+-]+@[A-Z\\d]+\\.[A-Z]{2,4}$";

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {

        final String email = (String) value;
        final Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            final String summary = I18N.getString("Invalid_email_address");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
        }
    }

}
