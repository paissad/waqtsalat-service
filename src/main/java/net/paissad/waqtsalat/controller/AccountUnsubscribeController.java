package net.paissad.waqtsalat.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.service.AccountService;

@Named
@RequestScoped
public class AccountUnsubscribeController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(AccountUnsubscribeController.class);

    @Getter
    private Boolean           connected;

    @Getter
    private Boolean           successful;

    @EJB
    private AccountService    accountService;

    @PostConstruct
    public void init() {
        final Account account = this.getLoggedAccount();
        this.connected = account != null;
        if (this.connected) {
            this.unsubscribe(account);
        }
    }

    private void unsubscribe(final Account account) {
        final String email = account.getEmail();
        try {

            logger.info("Unsubscribing the account '{} / {}'.", account.getUsername(), account.getEmail());

            logger.debug("Removing the account '{} 'and its metadatas from the database.", account.getEmail());
            this.accountService.getAccountDAO().delete(account);

            final HttpSession session = this.getCurrentHttpSession();
            if (session != null) session.invalidate();

            this.successful = Boolean.TRUE;

            final MailHelper mailHelper = new MailHelper();
            mailHelper.sendUnsuscribeConfirmation(account);

            logger.info("Account '{} / {}' unsubscribed successfully.", account.getUsername(), account.getEmail());

        } catch (final Exception e) {
            logger.error("An internal error occurred while unsubscribing the account '{}'.", email, e);
            this.wrapAndTreatInternalError(e);
        }
    }

}
