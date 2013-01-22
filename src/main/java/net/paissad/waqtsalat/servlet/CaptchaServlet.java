package net.paissad.waqtsalat.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "CaptchaServlet", urlPatterns = { CaptchaServlet.URL_PATTERN }, initParams = {
        @WebInitParam(name = CaptchaServlet.PARAM_WIDTH, value = "120", description = "Width of captcha image"),
        @WebInitParam(name = CaptchaServlet.PARAM_HEIGHT, value = "30", description = "Height of captcha image"),
        @WebInitParam(name = CaptchaServlet.PARAM_TIMEOUT, value = "30", description = "Timeout in seconds") }, asyncSupported = true)
public class CaptchaServlet extends HttpServlet {

    private static final long  serialVersionUID = 1L;

    private static Logger      logger           = LoggerFactory.getLogger(CaptchaServlet.class);

    public static final String CAPTCHA_KEY      = "__captcha_key";
    public static final String URL_PATTERN      = "/__captcha.jpg";
    public static final String PARAM_WIDTH      = "width";
    public static final String PARAM_HEIGHT     = "height";
    public static final String PARAM_TIMEOUT    = "timeout";

    private int                width;
    private int                height;
    private int                timeout;

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        this.width = Integer.parseInt(servletConfig.getInitParameter(PARAM_WIDTH));
        this.height = Integer.parseInt(servletConfig.getInitParameter(PARAM_HEIGHT));
        this.timeout = Integer.parseInt(servletConfig.getInitParameter(PARAM_TIMEOUT));
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        // Expire response
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Max-Age", 0);

        // create the async context, otherwise getAsyncContext() will be null
        final AsyncContext asyncContext = req.startAsync();

        asyncContext.setTimeout(TimeUnit.SECONDS.toMillis(timeout));
        asyncContext.addListener(new AsyncListener() {

            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                logger.error("Unable to create the Captcha's image because the request timed out; IP address is '{}'",
                        event.getSuppliedRequest().getRemoteAddr());
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }

            @Override
            public void onError(final AsyncEvent event) throws IOException {
                logger.error("Error while creating the Captcha's image : {}", event.getThrowable());
            }

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
            }
        });

        asyncContext.start(new Runnable() {

            @Override
            public void run() {
                OutputStream out = null;
                try {
                    out = asyncContext.getResponse().getOutputStream();
                    final String captchaValue = CaptchaServlet.this.writeImage(out);
                    final HttpSession session = req.getSession(true);
                    session.setAttribute(CAPTCHA_KEY, captchaValue);
                    asyncContext.complete();

                } catch (final IOException e) {
                    final String errMsg = "Error while computing captcha : " + e.getMessage();
                    logger.error(errMsg, e);
                    throw new IllegalStateException(errMsg, e);

                } finally {
                    if (out != null) try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private String writeImage(final OutputStream out) throws IOException {

        final BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = image.createGraphics();
        final Random r = new Random();
        final String token = Long.toString(Math.abs(r.nextLong()), 36);
        final String captchaValue = token.substring(0, 6);
        final Color c = new Color(0.6662f, 0.4569f, 0.3232f);
        final GradientPaint gp = new GradientPaint(30, 30, c, 15, 25, Color.white, true);
        graphics2D.setPaint(gp);
        final Font font = new Font("Verdana", Font.CENTER_BASELINE, 26);
        graphics2D.setFont(font);
        graphics2D.drawString(captchaValue, 2, 20);
        graphics2D.dispose();

        ImageIO.write(image, "jpeg", out);
        return captchaValue;
    }

}
