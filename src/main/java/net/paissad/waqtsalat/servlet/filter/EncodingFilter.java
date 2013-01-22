package net.paissad.waqtsalat.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "EncodingFilter", urlPatterns = { "/*" }, asyncSupported = true)
public class EncodingFilter implements Filter {

    private static Logger       logger        = LoggerFactory.getLogger(EncodingFilter.class.getName());

    private static final String UTF8_ENCODING = "UTF-8";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        logger.info("Setting the encoding to use for HTTP requests (" + UTF8_ENCODING + ")");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding(UTF8_ENCODING);
        response.setCharacterEncoding(UTF8_ENCODING);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
