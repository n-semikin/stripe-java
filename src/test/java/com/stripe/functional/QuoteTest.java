package com.stripe.functional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.stripe.BaseStripeTest;
import com.stripe.exception.StripeException;
import com.stripe.model.Quote;
import com.stripe.net.ApiResource;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

public class QuoteTest extends BaseStripeTest {
  public static final String QUOTE_ID = "dp_123";

  private Quote getQuoteFixture() throws StripeException {
    final Quote quote = Quote.retrieve(QUOTE_ID);
    resetNetworkSpy();
    return quote;
  }

  @Test
  public void testPdf() throws StripeException {
    final Quote quote = getQuoteFixture();

    final InputStream pdf = quote.pdf();

    assertNotNull(pdf);
    verifyRequestStream(
        ApiResource.RequestMethod.GET, String.format("/v1/quotes/%s/pdf", quote.getId()));
  }
}
