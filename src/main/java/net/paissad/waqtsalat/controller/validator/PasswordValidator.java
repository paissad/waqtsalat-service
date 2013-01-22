/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.controller.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.paissad.waqtsalat.I18N;

@FacesValidator(PasswordValidator.VALIDATOR_ID)
public class PasswordValidator implements Validator {

    public static final String VALIDATOR_ID = "passwordValidator";

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {

        final UIInput confirmPasswordComponent = (UIInput) component.getAttributes().get("confirmPasswordComponent");
        final String passwordConfirmation = (String) confirmPasswordComponent.getSubmittedValue();

        final String password = (String) value;

        // Compare the first password with the second password.
        if (!password.equals(passwordConfirmation)) {
            final String summary = I18N.getString("Password_mismatch");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, summary));
        }
    }

}
