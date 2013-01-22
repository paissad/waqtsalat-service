package net.paissad.waqtsalat.controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.domain.AccountMetadata;
import net.paissad.waqtsalat.service.AccountService;
import net.paissad.waqtsalat.util.RandomUtils;

@Getter
@Setter
@Named
@SessionScoped
public class AccountLoginController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(AccountLoginController.class);

    @Inject
    private Account           account;

    @EJB
    private AccountService    accountService;

    @Email
    private String            emailForForgottenPass;

    public String signIn() {
        try {
            final String specifiedEmail = this.account.getEmail();
            logger.debug("Sign-In the account '{}'", specifiedEmail);

            final String specifiedPassword = RandomUtils.encryptText(this.account.getPassword());

            final Account expectedAccount = this.accountService.getAccountDAO().findByEmail(specifiedEmail);
            final String expectedPassword = (expectedAccount != null) ? expectedAccount.getPassword() : null;

            if ((expectedAccount == null) || !(specifiedPassword.equals(expectedPassword))) {
                final String summary = I18N.getString("_login_Wrong_username_or_password");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, summary));
                return null;
            }

            // Credentials are correct.
            final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(true);
            session.setAttribute(SessionAttributes.LOGGED_ACCOUNT, expectedAccount);

            logger.debug("Account '{}' logged in successfully.", specifiedEmail);

        } catch (final Exception e) {
            logger.error("An internal error occurred while signing in '{}'.", this.account.getEmail(), e);
            this.wrapAndTreatInternalError(e);
        }
        return null;
    }

    public String signOut() {
        try {
            logger.debug("Sign-Out the account '{}'.", this.getLoggedAccount());
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        } catch (final Exception e) {
            logger.error("An internal error occurred while signing out '{}'.", this.account.getEmail(), e);
            this.wrapAndTreatInternalError(e);
        }
        return null;
    }

    public String processForgottenPassword() {
        try {
            logger.info("Reseting password for account having e-mail '{}'.", this.emailForForgottenPass);

            // NOTE : The account existence has already been verified by the EmailMustBeRegisteredValitor ...
            final Account dest = this.accountService.getAccountDAO().findByEmail(this.emailForForgottenPass);

            // Let's the compute the deadline date until when the key for reseting the password is valid. Defaults to 2
            // days.
            final Calendar deadline = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
            deadline.add(Calendar.DAY_OF_MONTH, 2);

            AccountMetadata accountMetadata = dest.getMetadata();
            if (accountMetadata == null) {
                accountMetadata = new AccountMetadata();
                dest.setMetadata(accountMetadata);
            }
            final String resetKey = RandomUtils.generateRandomString();
            accountMetadata.setPasswordResetKey(resetKey);
            accountMetadata.setPasswordResetDeadline(deadline);

            this.accountService.getAccountDAO().update(dest);

            final MailHelper mailHelper = new MailHelper();
            mailHelper.sendNewPasswordResetKey(dest, deadline, resetKey);

            logger.info("New password sent successfully to '{}'", this.emailForForgottenPass);

        } catch (final Exception e) {
            logger.error("An internal error occured while reseting password for '{}'.", this.emailForForgottenPass, e);
            this.wrapAndTreatInternalError(e);
        }
        return null;
    }

    public Boolean getConnected() {
        return this.getLoggedAccount() != null;
    }

  
}
