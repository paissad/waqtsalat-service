package net.paissad.waqtsalat.controller;

import static net.paissad.waqtsalat.WSConstants.DEFAULT_ENCODING;
import static net.paissad.waqtsalat.WSConstants.DEFAULT_LOCALE;
import static net.paissad.waqtsalat.WSConstants.DEFAULT_TIMEZONE;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.paissad.paissadtools.mail.exception.MailToolException;
import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.service.AccountService;
import net.paissad.waqtsalat.template.TemplateRenderingException;
import net.paissad.waqtsalat.util.EJBUtil;

/**
 * This class contains mainly the methods that help to send mails to a end users (accounts).
 * 
 * @author paissad
 */
class MailHelper extends AbstractController {

    private static final long serialVersionUID = 1L;

    private AccountService    accountService   = EJBUtil.lookup(AccountService.class);

    public void sendApiKey(final Account account) throws MailToolException, TemplateRenderingException {

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", account.getUsername());
        params.put("apiKey", account.getAuth().getValue());
        params.put("applicationURL", this.getApplicationURL());

        final String subject = "WaqtSalat-Service - API key.";
        final String templatePath = this.getWebappMailTemplatesPath() + "/ApiKey.vm";

        this.accountService.sendTemplateMail(account, subject, templatePath, params);
    }

    public void sendActivationKey(final Account account) throws MailToolException, TemplateRenderingException {

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", account.getUsername());
        params.put("activationKey", account.getActivationKey());
        params.put("applicationURL", this.getApplicationURL());

        final String subject = "WaqtSalat-Service - Registration";
        final String templatePath = this.getWebappMailTemplatesPath() + "/Registration.vm";

        this.accountService.sendTemplateMail(account, subject, templatePath, params);
    }

    public void sendNewPasswordResetKey(final Account account, final Calendar deadline, final String resetKey)
            throws MailToolException, TemplateRenderingException, UnsupportedEncodingException {

        final Map<String, Object> params = new HashMap<String, Object>();
        final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' HH:mm:ss z", DEFAULT_LOCALE);
        sdf.setTimeZone(DEFAULT_TIMEZONE);

        params.put("username", account.getUsername());
        params.put("applicationURL", this.getApplicationURL());
        params.put("resetKey", URLEncoder.encode(resetKey, DEFAULT_ENCODING));
        params.put("validUntil", sdf.format(deadline.getTime()));

        final String subject = "[WaqtSalat-Service] - New password";
        final String templatePath = this.getWebappMailTemplatesPath() + "/NewPassword.vm";

        this.accountService.sendTemplateMail(account, subject, templatePath, params);
    }

    public void sendUnsuscribeConfirmation(final Account account) throws MailToolException, TemplateRenderingException {

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", account.getUsername());

        final String subject = "WaqtSalat-Service - Unsubscription";
        final String templatePath = this.getWebappMailTemplatesPath() + "/Unsubscribe.vm";

        this.accountService.sendTemplateMail(account, subject, templatePath, params);
    }

}
