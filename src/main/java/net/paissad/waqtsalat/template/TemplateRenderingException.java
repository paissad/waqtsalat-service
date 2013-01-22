package net.paissad.waqtsalat.template;

/**
 * Exception which is thrown when an error occurs during template rendering.
 * 
 * @author paissad
 */
public class TemplateRenderingException extends Exception {

    private static final long serialVersionUID = 1L;

    public TemplateRenderingException(final String message) {
        super(message);
    }

    public TemplateRenderingException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
