package net.paissad.waqtsalat.controller;

import java.io.File;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.domain.Account;

abstract class AbstractController implements Controller {

    private static final long serialVersionUID = 1L;

    /**
     * @param exception - The root exception cause to treat.
     */
    protected void wrapAndTreatInternalError(final Exception exception) {
        this.wrapAndTreatInternalError(exception, null, null, false);
    }

    /**
     * @param exception - The root exception cause to treat.
     * @param errorMessage - The message to show to the end user on the web browser. May be <tt>null</tt>
     */
    protected void wrapAndTreatInternalError(final Exception exception, final String errorMessage) {
        this.wrapAndTreatInternalError(exception, errorMessage, null, false);
    }

    /**
     * @param exception - The root exception cause to treat.
     * @param errorMessage - The message to show to the end user on the web browser. May be <tt>null</tt>
     * @param clientId - The client identifier with which this message is associated (if any). May be <tt>null</tt>.
     * @param reThrowException - Whether or not to re-throw the exception wrapped into a {@link RuntimeException}.
     */
    protected void wrapAndTreatInternalError(final Exception exception, final String errorMessage,
            final String clientId, final boolean reThrowException) {
        final String summary = (errorMessage == null) ? I18N.getString("_global_internal_error") : errorMessage;
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, summary));
        if (reThrowException) throw new RuntimeException("__INTERNAL_ERROR__", exception);
    }

    protected String getApplicationURL() {

        final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        final String scheme = context.getRequestScheme();
        final String serverName = context.getRequestServerName();
        final String serverPort = context.getRequestServerPort() + "";
        final String contextPath = context.getRequestContextPath();

        final StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (!("http".equals(scheme) && "80".equals(serverPort))
                || !("https".equals(scheme) && "443".equals(serverPort))) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath); // Already contain the leading "/"
        return url.toString();
    }

    protected String getWebappMailTemplatesPath() {
        return new File(this.getApplicationRealPath(), "META-INF/mailTemplates").getAbsolutePath();
    }

    protected String getApplicationRealPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    }

    protected Account getLoggedAccount() {
        final HttpSession session = this.getCurrentHttpSession();
        if (session != null) {
            final Object sessionObj = session.getAttribute(SessionAttributes.LOGGED_ACCOUNT);
            if ((sessionObj != null) && (sessionObj instanceof Account)) return (Account) sessionObj;
        }
        return null;
    }

    protected HttpSession getCurrentHttpSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
}
