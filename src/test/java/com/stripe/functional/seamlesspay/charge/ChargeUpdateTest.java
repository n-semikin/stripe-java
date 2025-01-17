package com.stripe.functional.seamlesspay.charge;

import com.seamlesspay.SPAPI;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.SPCharge;
import com.stripe.net.RequestOptions;
import com.stripe.param.SPChargeUpdateParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class ChargeUpdateTest {

  public static final String DEV_API_KEY = "sk_01EWB3GM26X5FE81HQDJ01YK0Y";
  public static final String VALID_TOKEN = "TKN_01EXMB975XXG1XA3MATBNBR4QF";

  @InjectMocks
  private SPAPI api;

  private SPCharge existingCharge;

  @BeforeEach
  void setUp() {
    SPAPI.overrideApiBase(SPAPI.DEV_API_BASE);
  }

  @Test
  void testAuthenticationExceptionIfApiKeyNotSpecified() {
    //given
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(null)
      .build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId("123");
    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("No API key provided"));
  }

  @Test
  void testInvalidRequestExceptionIfNoId() {
    //given
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder().build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId(null);
    InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertTrue(exception.getMessage().startsWith("Invalid null ID found"));
  }

  @Test
  void testReturns401OnInvalidApiKey() {
    //given
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().build();
    RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey(DEV_API_KEY + "123")
      .build();

    //when
    SPCharge spCharge = new SPCharge();
    spCharge.setId("123");
    ApiException ex = assertThrows(ApiException.class, () -> spCharge.update(params, requestOptions));

    //then
    assertEquals(401, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Not authenticated error"));
  }

  @Test
  void testReturns422IfMissingRequiredField() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPCharge existingCharge = SPCharge.retrieve("TR_01FVFJ0XX7KMBYB64KRMFPQN6W", requestOptions);
    log.info("got existing charge={}", existingCharge);

    //when
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().amount("123.00").build();
    ApiException ex = assertThrows(ApiException.class, () -> existingCharge.update(params, requestOptions)); // TOD replace with uadpte

    //then
    assertEquals(422, ex.getStatusCode());
    assertTrue(ex.getMessage().startsWith("Unprocessable error"));
  }

  @Test
  void testUpdatesChargeIfSuccess() throws StripeException {
    //given
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(DEV_API_KEY).build();
    SPCharge existingCharge = SPCharge.retrieve("TR_01FVFJ0XX7KMBYB64KRMFPQN6W", requestOptions);

    double currentAmount = Double.parseDouble(existingCharge.getAmount());
    double newAmount = currentAmount + 1;
    log.info("current amount={} new={}", currentAmount, newAmount);

    //when
    SPChargeUpdateParams params = SPChargeUpdateParams.builder().amount("" + currentAmount).build();
    SPCharge updatedCharge = existingCharge.update(params, requestOptions);

    //then
    assertEquals("" + newAmount, updatedCharge.getAmount());
  }
}
