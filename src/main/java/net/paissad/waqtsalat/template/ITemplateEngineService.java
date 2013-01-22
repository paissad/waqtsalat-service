package net.paissad.waqtsalat.template;

import java.util.Map;

/**
 * 
 * @author paissad
 * 
 * @param <T> - The source type of the template to render.
 */
public interface ITemplateEngineService<T> {

    /**
     * @param template - The template.
     * @param params - The key/value parameters to use for rendering the template.
     * @return The content of after the template has been rendered with the specified parameters.
     * @throws TemplateRenderingException
     * @see #render(Object, String, Map)
     */
    String render(final T template, final Map<String, Object> params) throws TemplateRenderingException;

    /**
     * @param template - The template.
     * @param encoding - The encoding to use while rendering the template.
     * @param params - The key/value parameters to use for rendering the template.
     * @return The content of after the template has been rendered with the specified parameters.
     * @throws TemplateRenderingException
     * @see #render(Object, Map)
     */
    String render(final T template, final String encoding, final Map<String, Object> params)
            throws TemplateRenderingException;
}
