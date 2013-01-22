package net.paissad.waqtsalat.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Getter;

import net.paissad.paissadtools.mail.MailTool;
import net.paissad.paissadtools.mail.MailToolMessageSettings;
import net.paissad.paissadtools.mail.MailToolSMTPSettings;
import net.paissad.paissadtools.mail.exception.MailToolException;
import net.paissad.waqtsalat.conf.SMTPConf;
import net.paissad.waqtsalat.dao.impl.AccountDAO;
import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.template.ITemplateEngineService;
import net.paissad.waqtsalat.template.TemplateRenderingException;
import net.paissad.waqtsalat.template.impl.StreamTemplateEngineService;
import net.paissad.waqtsalat.util.CommonUtils;

@Stateless
public class AccountService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @Getter
    private AccountDAO        accountDAO;

    public boolean isEmailAlreadyUsed(final String email) {
        if (email == null) throw new IllegalArgumentException("The email address cannot be null.");
        final Account account = this.accountDAO.findByEmail(email);
        return account != null;
    }

    public boolean isUsernameAlreadyUsed(final String name) {
        if (name == null) throw new IllegalArgumentException("The name cannot be null.");
        final Account account = this.accountDAO.findByName(name);
        return account != null;
    }

    public boolean isAccountActivated(final Account account) {
        if (account == null) throw new IllegalArgumentException("The account cannot be null.");
        return account.getActivationKey() == null;
    }

    public void activateAccount(final Account account) {
        if (account == null) throw new IllegalArgumentException("The account cannot be null.");
        account.setActivationKey(null);
        this.accountDAO.update(account);
    }

    public void updatePassword(final String email, final String newPassword) {
        final Account account = this.accountDAO.findByEmail(email);
        if (account == null) throw new IllegalStateException("No account having the email '" + email + "' found.");
        account.setPassword(newPassword);
        this.accountDAO.update(account);
    }

    /**
     * @param account - The account to who to send the email.
     * @param subject - The subject of the mail.
     * @param templatePath - The path of the resource. (should starts with a slash '/' and must be in the classpath)
     * @param params - The parameters to use for rendering the template.
     * @throws TemplateRenderingException
     * @throws MailToolException
     */
    public void sendTemplateMail(final Account account, final String subject, final String templatePath,
            final Map<String, Object> params) throws TemplateRenderingException, MailToolException {

        if (account == null) throw new IllegalArgumentException("The account cannot be null.");

        // Retrieve SMTP configuration/settings.
        final MailToolSMTPSettings smtpSettings = SMTPConf.getSettings();
        final MailTool mailTool = new MailTool(smtpSettings);

        // Configure the subject & the body of the message to send.
        final MailToolMessageSettings messageSettings = new MailToolMessageSettings();
        messageSettings.setSubject(subject);

        final ITemplateEngineService<InputStream> templateService = new StreamTemplateEngineService();
        InputStream template = null;

        // Now let's render the template file.
        try {
            template = AccountService.class.getResourceAsStream(templatePath);
            if ((template == null) && (new File(templatePath).isFile())) {
                try {
                    template = new BufferedInputStream(new FileInputStream(new File(templatePath)), 8192);
                } catch (final FileNotFoundException e) {
                    throw new IllegalStateException("Unable to find the template file '" + new File(templatePath) + "'");
                }
            }
            if (template == null) throw new IllegalStateException("Unable to find the tempate '" + templatePath + "'");
            final String mailBody = templateService.render(template, params);
            messageSettings.setContent(mailBody);
            messageSettings.getRecipientsTO().add(account.getEmail());

        } finally {
            CommonUtils.closeAllStreamsQuietly(template);
        }

        // Send the mail.
        mailTool.send(messageSettings);
    }
}
