package net.paissad.waqtsalat.template.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import net.paissad.waqtsalat.template.TemplateRenderingException;
import net.paissad.waqtsalat.util.CommonUtils;

public class StreamTemplateEngineService extends AbstractTemplateEngineService<InputStream> {

    @Override
    public String render(final InputStream template, final String encoding, final Map<String, Object> params)
            throws TemplateRenderingException {

        StringWriter writer = null;
        Reader reader = null;
        try {
            writer = new StringWriter();
            reader = new BufferedReader(new InputStreamReader(template, encoding), 8192);

            final VelocityContext context = this.initializeAndPopulateContext(params);
            final boolean isOK = Velocity.evaluate(context, writer, "__template_stream__", reader);
            if (!isOK) {
                throw new TemplateRenderingException("Template not rendered successfully.");
            }
            writer.flush();

            return writer.toString();

        } catch (final Exception e) {
            throw new TemplateRenderingException("Error while rendering the template : " + e.getMessage(), e);
        } finally {
            CommonUtils.closeAllStreamsQuietly(writer, reader);
        }
    }

}
