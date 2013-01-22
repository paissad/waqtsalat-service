package net.paissad.waqtsalat.conf;

import net.paissad.paissadtools.mail.MailToolSMTPSettings;

public class SMTPConf {

    private SMTPConf() {
    }

    public static String getHost() {
        return ConfigHelper.getValue("smtp.host");
    }

    public static String getPort() {
        return ConfigHelper.getValue("smtp.port");
    }

    public static String getUser() {
        return ConfigHelper.getValue("smtp.user");
    }

    public static String getPassword() {
        return ConfigHelper.getValue("smtp.password");
    }

    public static boolean isAuth() {
        return Boolean.valueOf(ConfigHelper.getValue("smtp.auth"));
    }

    public static boolean isStartTLS() {
        return Boolean.valueOf(ConfigHelper.getValue("smtp.starttls"));
    }

    public static boolean isSSL() {
        return Boolean.valueOf(ConfigHelper.getValue("smtp.ssl"));
    }

    public static MailToolSMTPSettings getSettings() {
        final MailToolSMTPSettings settings = new MailToolSMTPSettings(getUser(), getPassword(), getHost(), getPort());
        settings.setAuth(isAuth());
        settings.setSsl(isSSL());
        settings.setStartTLS(isStartTLS());
        return settings;
    }
}
