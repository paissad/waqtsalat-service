package net.paissad.waqtsalat.template.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.template.ITemplateEngineService;
import net.paissad.waqtsalat.template.TemplateRenderingException;

abstract class AbstractTemplateEngineService<T> implements ITemplateEngineService<T> {

    private static Logger      logger                  = LoggerFactory.getLogger(AbstractTemplateEngineService.class);

    public static final String DEFAULT_RENDER_ENCODING = "UTF-8";

    @Override
    public String render(T template, java.util.Map<String, Object> params) throws TemplateRenderingException {
        return this.render(template, DEFAULT_RENDER_ENCODING, params);
    }

    protected VelocityContext initializeAndPopulateContext(final Map<String, Object> params) {

        Velocity.init();
        final VelocityContext context = new VelocityContext();

        if (params != null && !params.isEmpty()) {
            final Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                final Entry<String, Object> o = iter.next();
                context.put(o.getKey(), o.getValue());
            }
        } else {
            logger.warn("The parameters to use for populating the template are null or empty. The template will not be rendered.");
        }

        return context;
    }

}
