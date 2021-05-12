package com.stripe.net;

import static java.util.Objects.requireNonNull;

import com.stripe.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

/** A response from Stripe's API. */
@Value
@Accessors(fluent = true)
public class StripeResponseStream {
  /** The HTTP status code of the response. */
  int code;

  /** The HTTP headers of the response. */
  HttpHeaders headers;

  /** The body of the response. */
  @NonFinal
  @Getter(AccessLevel.PACKAGE)
  InputStream bodyStream;

  /** Number of times the request was retried. Used for internal tests only. */
  @NonFinal
  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  int numRetries;

  /**
   * Initializes a new instance of the {@link StripeResponseStream} class.
   *
   * @param code the HTTP status code of the response
   * @param headers the HTTP headers of the response
   * @param body the body of the response
   * @throws NullPointerException if {@code headers} or {@code body} is {@code null}
   */
  public StripeResponseStream(int code, HttpHeaders headers, InputStream bodyStream) {
    requireNonNull(headers);
    requireNonNull(bodyStream);

    this.code = code;
    this.headers = headers;
    this.bodyStream = bodyStream;
  }

  public StripeResponse unstream() throws IOException {
    final String bodyString = StreamUtils.readToEnd(this.bodyStream, ApiResource.CHARSET);
    this.bodyStream.close();
    return new StripeResponse(this.code, this.headers, bodyString);
  }

  /**
   * Gets the date of the request, as returned by Stripe.
   *
   * @return the date of the request, as returned by Stripe
   */
  public Instant date() {
    Optional<String> dateStr = this.headers.firstValue("Date");
    if (!dateStr.isPresent()) {
      return null;
    }
    return ZonedDateTime.parse(dateStr.get(), DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
  }

  /**
   * Gets the idempotency key of the request, as returned by Stripe.
   *
   * @return the idempotency key of the request, as returned by Stripe
   */
  public String idempotencyKey() {
    return this.headers.firstValue("Idempotency-Key").orElse(null);
  }

  /**
   * Gets the ID of the request, as returned by Stripe.
   *
   * @return the ID of the request, as returned by Stripe
   */
  public String requestId() {
    return this.headers.firstValue("Request-Id").orElse(null);
  }
}
