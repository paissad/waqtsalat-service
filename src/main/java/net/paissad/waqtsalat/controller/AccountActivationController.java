package net.paissad.waqtsalat.controller;

import static net.paissad.waqtsalat.WSConstants.DEFAULT_LOCALE;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.service.AccountService;

@Named
@RequestScoped
public class AccountActivationController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(AccountActivationController.class);

    @ManagedProperty(value = "#{param.activationKey}")
    private String            activationKey;

    @Getter
    private Boolean           valid;

    @Getter
    private String            activatedUsername;

    @EJB
    private AccountService    accountService;

    @PostConstruct
    public void init() {
        if (this.activationKey == null) {
            this.activationKey = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                    .get("activationKey");
        }
        this.valid = this.checkAndValidate(this.activationKey);
    }

    private boolean checkAndValidate(final String key) {
        boolean result = false;
        try {
            final Account account = this.accountService.getAccountDAO().findByActivationKey(key);
            if (account != null) {
                this.activatedUsername = account.getUsername();
                this.accountService.activateAccount(account);
                result = true;

                try {
                    final MailHelper mailHelper = new MailHelper();
                    mailHelper.sendApiKey(account);

                } catch (final Exception e) {
                    logger.error("Error while sending the authentication key to the account '{} / {}' : {}",
                            account.getUsername(), account.getEmail(), e.getMessage());
                    final MessageFormat format = new MessageFormat(
                            I18N.getString("_activation_Account_activated_but_sendmail_failed"), DEFAULT_LOCALE);
                    final Object[] arguments = { account.getUsername() };
                    final String summary = format.format(arguments);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, summary, summary));
                    throw new Exception(e);
                }
            }

        } catch (final Exception e) {
            logger.error("An internal error occured while checking the activation key '{}'.", key, e);
            this.wrapAndTreatInternalError(e);
        }
        return result;
    }

}
