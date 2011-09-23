/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.service.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.service.WSConstants;
import net.paissad.waqtsalat.service.exception.WSException;

/**
 * @author paissad
 * 
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static enum RequestType {
        HEAD,
        GET;
    }

    private HttpUtils() {
    }

    /**
     * <p>
     * <b>Example</b>: <i>getFilenameFromURL(http://domain.com/aaa/bbb.zip)</i>
     * would return '<i>bbb.zip</i>'
     * </p>
     * <p>
     * <b>Note</b>:
     * <ul>
     * <li>This method will follow the url redirections and will return the name
     * of the file of the last URL which is among all redirections.</li>
     * <li>If the URL has no filename (Ex: <i>http://domain.com)</i>, then an
     * empty string is returned.</li>
     * </ul>
     * </p>
     * 
     * @param url
     * @return The name of the file from the URL.
     * @throws WSException
     */
    public static String getFilenameFromURL(final String url) throws WSException {
        HttpHead request = null;
        HttpClient client = null;
        try {
            request = (HttpHead) getNewHttpRequest(url, RequestType.HEAD, null);
            client = getNewHttpClient();
            client.execute(request);
            String resourcePath = request.getURI().getPath();
            return resourcePath.substring(resourcePath.lastIndexOf("/") + 1);

        } catch (Exception e) {
            throw new WSException("Error while retrieving the ressource name of the URL.", e);

        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * Downloads a file from a specified URL.
     * 
     * @param url - The url from where to download the file.
     * @param destinationFile - Where to save the file.
     * @throws WSException
     */
    public static void downloadFile(final String url, final File destinationFile) throws WSException {

        if (url == null) {
            throw new IllegalArgumentException("The URL of download may not be null.");
        }
        if (destinationFile == null) {
            throw new IllegalArgumentException("The destination file may not be null.");
        }
        InputStream in = null;
        OutputStream out = null;

        try {
            in = sendRequest(url, RequestType.GET);
            out = new BufferedOutputStream(new FileOutputStream(destinationFile));
            if (in != null) {
                IOUtils.copy(in, out);
            }

        } catch (Exception e) {
            throw new WSException("Error while downloading file", e);

        } finally {
            CommonUtils.closeAllStreamsQuietly(in, out);
        }
    }

    private static InputStream sendRequest(final String url, final RequestType requestType) throws WSException {

        InputStream in = null;
        HttpClient client = null;
        HttpRequestBase request = null;
        boolean errorOccured = false;

        try {
            request = getNewHttpRequest(url, requestType, null);
            client = getNewHttpClient();
            HttpResponse resp = client.execute(request);

            int statusCode = resp.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(resp.getEntity().getContent(), baos);
                baos.flush();
                in = new ByteArrayInputStream(baos.toByteArray());

            } else {
                // The response code is not OK.
                StringBuilder errMsg = new StringBuilder();
                errMsg.append("HTTP ").append(requestType).append(" failed => ").append(resp.getStatusLine());
                if (request != null) {
                    errMsg.append(" : ").append(request.getURI());
                }
                throw new WSException(errMsg.toString());
            }

            return in;

        } catch (Exception e) {
            errorOccured = true;
            throw new WSException("Error during file HTTP request.", e);

        } finally {
            if (errorOccured) {
                if (request != null) {
                    request.abort();
                }
            }
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
    }

    private static HttpRequestBase getNewHttpRequest(
            final String url,
            final RequestType requestType,
            final Header[] additionalRequestHeaders) {

        HttpRequestBase request = null;
        if (requestType == RequestType.HEAD) {
            request = new HttpHead(url);
        } else if (requestType == RequestType.GET) {
            request = new HttpGet(url);
        } else {
            throw new IllegalStateException("The 'RequestType' " + requestType + " is not supported yet !");
        }
        request.addHeader("User-Agent", WSConstants.WS_USER_AGENT);
        if (additionalRequestHeaders != null && additionalRequestHeaders.length > 0) {
            for (Header aHeader : additionalRequestHeaders) {
                request.setHeader(aHeader);
            }
        }

        return request;
    }

    private static HttpClient getNewHttpClient() throws WSException {

        try {
            TrustStrategy trustStrategy = new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };
            SSLSocketFactory sf = new CustomSSLFactory(trustStrategy);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry sr = new SchemeRegistry();
            sr.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            sr.register(new Scheme("https", 443, sf));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(sr);

            DefaultHttpClient client = new DefaultHttpClient(ccm);
            client.setHttpRequestRetryHandler(new CustomHttpRequestRetryHandler());
            client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);

            List<String> authpref = new ArrayList<String>();
            // Choose BASIC over DIGEST for proxy authentication
            authpref.add(AuthPolicy.BASIC);
            authpref.add(AuthPolicy.DIGEST);
            client.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);

            client.addRequestInterceptor(new CustomHttpRequestInterceptor());
            client.addResponseInterceptor(new CustomHttpResponseInterceptor());

            return client;

        } catch (Exception e) {
            throw new WSException("Error while creating a HTTP client.", e);
        }
    }

    // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d4e292
    private static class CustomHttpRequestRetryHandler implements HttpRequestRetryHandler {

        private static final int MAX_RETRIES = 5;

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= MAX_RETRIES) {
                return false; // Do not retry if over max retry count
            }
            if (exception instanceof NoHttpResponseException) {
                return true; // Retry if the server dropped connection on us
            }
            if (exception instanceof SSLHandshakeException) {
                return false; // Do not retry on SSL handshake exception
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                return true; // Retry if the request is considered idempotent
            }
            return false;
        }
    }

    private static class CustomSSLFactory extends SSLSocketFactory {

        SSLContext sslContext;

        public CustomSSLFactory(TrustStrategy trustStrategy)
                throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {

            super(trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            };

            this.sslContext = SSLContext.getInstance("TLS");
            this.sslContext.init(null, new TrustManager[] { tm }, null);
        }
    }

    private static class CustomHttpRequestInterceptor implements HttpRequestInterceptor {

        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            // logger.trace("------------- REQUEST PARAMS ---------------");
            // for (Header aHeader : request.getAllHeaders()) {
            // logger.trace(aHeader.toString());
            // }
            // logger.trace("--------------------------------------------");
        }
    }

    private static class CustomHttpResponseInterceptor implements HttpResponseInterceptor {

        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Header redirectHeader = response.getFirstHeader("Location");
                if (redirectHeader != null) {
                    logger.debug("Redirecting to {}", redirectHeader.getValue());
                    return;
                }
                logger.warn("------------- RESPONSE PARAMS ---------------");
                for (Header aHeader : response.getAllHeaders()) {
                    logger.warn(aHeader.toString());
                }
                logger.warn("---------------------------------------------");
            }
        }
    }
}
