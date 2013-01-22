package net.paissad.waqtsalat.controller;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.dao.impl.RoleDAO;
import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.domain.Auth;
import net.paissad.waqtsalat.domain.Role;
import net.paissad.waqtsalat.enums.RoleType;
import net.paissad.waqtsalat.service.AccountService;
import net.paissad.waqtsalat.util.RandomUtils;

@Named
@RequestScoped
@Getter
@Setter
public class AccountRegistrationController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(AccountRegistrationController.class);

    @Inject
    private Account           account;

    @EJB
    private AccountService    accountService;

    private String            passwordConfirmation;
    private String            captchaValue;
    private Boolean           agreeTermsAndConditions;
    private Boolean           errorOccured;
    private String            backupName;
    private String            backupEmail;

    public String register() {

        try {
            this.backupName = this.account.getUsername();
            this.backupEmail = this.account.getEmail();

            logger.info("Registering a new account '{} / {}'.", this.backupName, this.backupEmail);

            logger.trace("Encrypting password (SHA-1 algorigthm) of account '{}'.", this.backupName);
            final String encryptedPassword = RandomUtils.encryptText(this.account.getPassword());
            this.account.setPassword(encryptedPassword);

            logger.trace("Generating auth key for account '{} / {}'.", this.backupName, this.backupEmail);
            final Auth auth = new Auth();
            auth.setValue(RandomUtils.generateRandomString());
            this.account.setAuth(auth);

            logger.trace("Generating activation key for account '{} / {}'.", this.backupName, this.backupEmail);
            this.account.setActivationKey(RandomUtils.generateRandomString());

            // Registration date.
            final Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
            this.account.setRegistrationDate(cal.getTime());

            logger.trace("Adding default role '{}' for account '{}'.", RoleType.USER, this.backupName);
            final RoleDAO roleDAO = new RoleDAO();
            final Role userRole = roleDAO.findByType(RoleType.USER);
            this.account.getRoles().add(userRole);

            logger.debug("Persisting account '{} / {}' into the database.", this.backupName, this.backupEmail);
            this.accountService.getAccountDAO().create(this.account);

            logger.debug("Sending activation key for '{}' to '{}'.", this.backupName, this.backupEmail);
            final MailHelper mailHelper = new MailHelper();
            mailHelper.sendActivationKey(this.account);

            this.clear(); // Clear the form

            logger.info("New account '{} / {}' registered successfully.", this.backupName, this.backupEmail);

        } catch (final Exception e) {
            this.errorOccured = Boolean.TRUE;
            logger.error("Unable to register the account '{} / {}'.", this.backupName, this.backupEmail);
            logger.error("An internal error while registering the user : {}", e.getMessage(), e);
            final MessageFormat format = new MessageFormat(I18N.getString("_registration_internal_error"));
            final String summary = format.format(new Object[] { this.backupName });
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, summary));
        }

        return null;
    }

    public String clear() {
        this.account = null;
        this.passwordConfirmation = null;
        this.captchaValue = null;
        this.agreeTermsAndConditions = null;
        return null;
    }

}
