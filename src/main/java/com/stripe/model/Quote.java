// File generated from our OpenAPI spec
package com.stripe.model;

import com.google.gson.annotations.SerializedName;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import com.stripe.param.QuotePdfParams;
import com.stripe.param.QuoteRetrieveParams;
import java.io.InputStream;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Quote extends ApiResource implements HasId {
  /** Time at which the object was created. Measured in seconds since the Unix epoch. */
  @SerializedName("created")
  Long created;

  /**
   * Three-letter <a href="https://www.iso.org/iso-4217-currency-codes.html">ISO currency code</a>,
   * in lowercase. Must be a <a href="https://stripe.com/docs/currencies">supported currency</a>.
   */
  @SerializedName("currency")
  String currency;

  /** Unique identifier for the object. */
  @Getter(onMethod_ = {@Override})
  @SerializedName("id")
  String id;

  /** Retrieves the quote with the given ID. */
  public static Quote retrieve(String quote) throws StripeException {
    return retrieve(quote, (Map<String, Object>) null, (RequestOptions) null);
  }

  /** Retrieves the quote with the given ID. */
  public static Quote retrieve(String quote, RequestOptions options) throws StripeException {
    return retrieve(quote, (Map<String, Object>) null, options);
  }

  /** Retrieves the quote with the given ID. */
  public static Quote retrieve(String quote, Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s",
            Stripe.getApiBase(), String.format("/v1/quotes/%s", ApiResource.urlEncodeId(quote)));
    return ApiResource.request(ApiResource.RequestMethod.GET, url, params, Quote.class, options);
  }

  /** Retrieves the quote with the given ID. */
  public static Quote retrieve(String quote, QuoteRetrieveParams params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s",
            Stripe.getApiBase(), String.format("/v1/quotes/%s", ApiResource.urlEncodeId(quote)));
    return ApiResource.request(ApiResource.RequestMethod.GET, url, params, Quote.class, options);
  }

  /** Download the PDF for a finalized Quote. */
  public InputStream pdf() throws StripeException {
    return pdf((Map<String, Object>) null, (RequestOptions) null);
  }

  /** Download the PDF for a finalized Quote. */
  public InputStream pdf(RequestOptions options) throws StripeException {
    return pdf((Map<String, Object>) null, options);
  }

  /** Download the PDF for a finalized Quote. */
  public InputStream pdf(Map<String, Object> params) throws StripeException {
    return pdf(params, (RequestOptions) null);
  }

  /** Download the PDF for a finalized Quote. */
  public InputStream pdf(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s",
            Stripe.getApiBase(),
            String.format("/v1/quotes/%s/pdf", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.requestStream(ApiResource.RequestMethod.GET, url, params, options);
  }

  /** Download the PDF for a finalized Quote. */
  public InputStream pdf(QuotePdfParams params) throws StripeException {
    return pdf(params, (RequestOptions) null);
  }

  /** Download the PDF for a finalized Quote. */
  public InputStream pdf(QuotePdfParams params, RequestOptions options) throws StripeException {
    String url =
        String.format(
            "%s%s",
            Stripe.getApiBase(),
            String.format("/v1/quotes/%s/pdf", ApiResource.urlEncodeId(this.getId())));
    return ApiResource.requestStream(ApiResource.RequestMethod.GET, url, params, options);
  }
}
