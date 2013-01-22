package net.paissad.waqtsalat.template.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import net.paissad.waqtsalat.util.CommonUtils;

public class FileTemplateEngineService extends AbstractTemplateEngineService<File> {

    @Override
    public String render(final File templateFile, final String encoding, final Map<String, Object> params) {

        // Let's render the template.
        StringWriter writer = null;
        try {
            writer = new StringWriter();

            final VelocityContext context = this.initializeAndPopulateContext(params);
            Velocity.mergeTemplate(templateFile.getAbsolutePath(), encoding, context, writer);
            writer.flush();

            return writer.toString();

        } finally {
            CommonUtils.closeAllStreamsQuietly(writer);
        }
    }
}
