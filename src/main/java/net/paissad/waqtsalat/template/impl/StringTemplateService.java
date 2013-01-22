package net.paissad.waqtsalat.template.impl;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import net.paissad.waqtsalat.template.TemplateRenderingException;
import net.paissad.waqtsalat.util.CommonUtils;

public class StringTemplateService extends AbstractTemplateEngineService<String> {

    /**
     * <b>NOTE</b> : the 'encoding' parameter' is not used here. It may <tt>be null</tt>.
     */
    @Override
    public String render(final String template, final String encoding, final Map<String, Object> params)
            throws TemplateRenderingException {

        StringWriter writer = null;
        try {
            writer = new StringWriter();
            final VelocityContext context = this.initializeAndPopulateContext(params);
            final boolean isOK = Velocity.evaluate(context, writer, "__template_string__", template);
            if (!isOK) {
                throw new TemplateRenderingException("Template not rendered successfully.");
            }
            writer.flush();

            return writer.toString();

        } finally {
            CommonUtils.closeAllStreamsQuietly(writer);
        }
    }

}
