package net.paissad.waqtsalat.controller;

import java.text.MessageFormat;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.paissadtools.mail.MailTool;
import net.paissad.paissadtools.mail.MailToolMessageSettings;
import net.paissad.paissadtools.mail.MailToolSMTPSettings;
import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.conf.ConfigHelper;
import net.paissad.waqtsalat.conf.SMTPConf;

@Named
@RequestScoped
@Getter
@Setter
public class ContactController implements Controller {

    private static Logger       logger           = LoggerFactory.getLogger(ContactController.class);

    private static final long   serialVersionUID = 1L;

    private static final String CONTACT_KEY      = "application.contact.mail";

    private String              name;
    private String              backupName;
    private String              email;
    private String              description;
    private String              captchaValue;
    private Boolean             receiveCopy;
    private Boolean             errorOccured;

    public String sendMessage() {

        logger.info("Contact Form by '{} / {}'.", this.name, this.email);

        String mailTo = "UNKNOWN_ADDRESS";
        try {
            mailTo = ConfigHelper.getValue(CONTACT_KEY, true);
            this.backupName = this.name;
            final MailToolSMTPSettings smtpSettings = SMTPConf.getSettings();
            final MailTool mailTool = new MailTool(smtpSettings);
            final MailToolMessageSettings message = new MailToolMessageSettings();
            message.setSubject("WaqtSalat-Service - [Contact Form] - " + this.name);
            final String mailBody = this.description.replaceAll("\n|(\r\n)", "<br/>") + "<br/><hr/>" + this.name
                    + "<br/>" + this.email;
            message.setContent(mailBody);
            message.getRecipientsTO().add(mailTo);

            if (this.receiveCopy != null && this.receiveCopy) {
                message.getRecipientsCC().add(this.email);
            }

            mailTool.send(message);
            logger.info("Contact Form, message sent by '{} / {}'.", this.name, this.email);
            this.clear(); // Clear the form.

        } catch (final Exception e) {
            this.errorOccured = Boolean.TRUE;
            logger.error("An internal error occurred form the Contact Form : {}", e.getMessage(), e);
            final MessageFormat format = new MessageFormat(
                    I18N.getString("_contact_error_occurred_while_sending_message"), I18N.getJSFViewRootLocale());
            final Object[] arguments = { this.name, mailTo };
            final String summary = format.format(arguments);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, summary));
        }
        return null;
    }

    public String clear() {
        this.name = null;
        this.email = null;
        this.description = null;
        this.captchaValue = null;
        this.receiveCopy = null;
        return null;
    }

}
