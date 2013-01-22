package net.paissad.waqtsalat.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.service.AccountService;

@Named
@RequestScoped
public class AccountResendActivationKeyController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(AccountResendActivationKeyController.class);

    @EJB
    private AccountService    accountService;

    @Getter
    private Boolean           accountAlreadyActivated;

    @Getter
    @Setter
    private String            emailForActivationResend;

    public String resendActivationKey() {
        try {
            final Account account = this.accountService.getAccountDAO().findByEmail(this.emailForActivationResend);
            if (this.accountService.isAccountActivated(account)) {
                this.accountAlreadyActivated = Boolean.TRUE;
            } else {
                // Account not yet activated ...
                final MailHelper mailHelper = new MailHelper();
                mailHelper.sendActivationKey(account);
            }
            this.clear();

        } catch (final Exception e) {
            logger.error("An internal error occured while resending activation key by mail.", e);
            this.wrapAndTreatInternalError(e);
        }
        return null;
    }

    public String clear() {
        this.emailForActivationResend = null;
        return null;
    }
}
