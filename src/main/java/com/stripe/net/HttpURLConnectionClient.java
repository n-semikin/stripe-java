package com.stripe.net;

import com.stripe.Stripe;
import com.stripe.exception.ApiConnectionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpURLConnectionClient extends HttpClient {

  private static boolean NETWORK_LOGS_ENABLED = false;

  static {
    if (NETWORK_LOGS_ENABLED) {
      ConsoleHandler handler = new ConsoleHandler();
      handler.setLevel(Level.FINEST);
      Logger log = LogManager.getLogManager().getLogger("");
      log.addHandler(handler);
      log.setLevel(Level.FINEST);
    }
  }

  /** Initializes a new instance of the {@link HttpURLConnectionClient}. */
  public HttpURLConnectionClient() {
    super();
  }

  /**
   * Sends the given request to Stripe's API.
   *
   * @param request the request
   * @return the response
   * @throws ApiConnectionException if an error occurs when sending or receiving
   */
  @Override
  public StripeResponseStream requestStream(StripeRequest request) throws ApiConnectionException {
    try {
      final HttpURLConnection conn = createStripeConnection(request);

      // Calling `getResponseCode()` triggers the request.
      final int responseCode = conn.getResponseCode();

      final HttpHeaders headers = HttpHeaders.of(conn.getHeaderFields());
      log.debug("response: code={}, headers={}", responseCode, headers);

      final InputStream responseStream =
          (responseCode >= 200 && responseCode < 300)
              ? conn.getInputStream()
              : conn.getErrorStream();

      return new StripeResponseStream(responseCode, headers, responseStream);

    } catch (IOException e) {
      throw new ApiConnectionException(
          String.format(
              "IOException during API request to Stripe (%s): %s "
                  + "Please check your internet connection and try again. If this problem persists,"
                  + "you should check Stripe's service status at https://twitter.com/stripestatus,"
                  + " or let us know at support@stripe.com.",
              Stripe.getApiBase(), e.getMessage()),
          e);
    }
  }

  /**
   * Sends the given request to Stripe's API, and returns a buffered response.
   *
   * @param request the request
   * @return the response
   * @throws ApiConnectionException if an error occurs when sending or receiving
   */
  @Override
  public StripeResponse request(StripeRequest request) throws ApiConnectionException {
    final StripeResponseStream responseStream = requestStream(request);
    try {
      return responseStream.unstream();
    } catch (IOException e) {
      throw new ApiConnectionException(
          String.format(
              "IOException during API request to Stripe (%s): %s "
                  + "Please check your internet connection and try again. If this problem persists,"
                  + "you should check Stripe's service status at https://twitter.com/stripestatus,"
                  + " or let us know at support@stripe.com.",
              Stripe.getApiBase(), e.getMessage()),
          e);
    }
  }

  static HttpHeaders getHeaders(StripeRequest request) {
    Map<String, List<String>> userAgentHeadersMap = new HashMap<>();

    userAgentHeadersMap.put("User-Agent", Arrays.asList(buildUserAgentString()));
    userAgentHeadersMap.put(
        "X-Stripe-Client-User-Agent", Arrays.asList(buildXStripeClientUserAgentString()));

    return request.headers().withAdditionalHeaders(userAgentHeadersMap);
  }

  private static HttpURLConnection createStripeConnection(StripeRequest request)
      throws IOException, ApiConnectionException {
    HttpURLConnection conn = null;

    if (request.options().getConnectionProxy() != null) {
      conn =
          (HttpURLConnection) request.url().openConnection(request.options().getConnectionProxy());
      Authenticator.setDefault(
          new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
              return request.options().getProxyCredential();
            }
          });
    } else {
      conn = (HttpURLConnection) request.url().openConnection();
    }

    conn.setConnectTimeout(request.options().getConnectTimeout());
    conn.setReadTimeout(request.options().getReadTimeout());
    conn.setUseCaches(false);
    for (Map.Entry<String, List<String>> entry : getHeaders(request).map().entrySet()) {
      String value = String.join(",", entry.getValue());
      log.trace("setting header: name={}; value={}", entry.getKey(), value);
      conn.setRequestProperty(entry.getKey(), value);
    }

    conn.setRequestMethod(request.method().name());
    log.debug("request method={}", request.method().name());

    if (request.content() != null) {
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", request.content().contentType());
      log.debug("setting header: name={}; value={}", "Content-Type", request.content().contentType());

      @Cleanup OutputStream output = conn.getOutputStream();
      log.debug("request content={}", new String(request.content().byteArrayContent(), ApiResource.CHARSET));
      output.write(request.content().byteArrayContent());
    }

    return conn;
  }
}
