// File generated from our OpenAPI spec
package com.stripe.param;

import com.google.gson.annotations.SerializedName;
import com.stripe.model.SPDigitalWalletProgramType;
import com.stripe.model.SPCurrency;
import com.stripe.model.SPEntryType;
import com.stripe.model.SPOrder;
import com.stripe.net.ApiRequestParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SPChargeCreateParams extends ApiRequestParams {

  /**
   * String with 2 decimal places e.g “25.00”. Length <= 12 characters
   */
  @SerializedName("amount")
  String amount;

  /**
   * Create a capture transaction.
   */
  @SerializedName("capture")
  Boolean capture;

  @SerializedName("currency")
  SPCurrency currency;

  /**
   * Length <= 4 characters
   */
  @SerializedName("cvv")
  String cvv;

  /**
   * Payment Description.
   */
  @SerializedName("description")
  String description;

  /**
   * This field contains the merchant name/product/service to be used in lieu of the DBA name.
   */
  @SerializedName("descriptor")
  String descriptor;

  /**
   * Optional, digital wallet program type.
   */
  @SerializedName("digitalWalletProgramType")
  SPDigitalWalletProgramType digitalWalletProgramType;

  /**
   * See https://docs.seamlesspay.com/2020-08-01#section/Idempotent-Requests
   */
  @SerializedName("idempotencyKey")
  String idempotencyKey;

  /**
   * Optional, for JSON object with up to 8 fields and 64 chars limit.
   */
  @SerializedName("metadata")
  String metadata;

  @SerializedName("poNumber")
  String poNumber;

  /**
   * Surcharge fee amount. Length <= 12 characters
   */
  @SerializedName("surchargeFeeAmount")
  String surchargeFeeAmount;

  /**
   */
  @SerializedName("order")
  SPOrder order;

  /**
   * A number assigned to uniquely reference a transaction.
   */
  @SerializedName("orderID")
  String orderID;

  /**
   * string <= 31 characters. The payment method (token) from pan-vault
   */
  @SerializedName("token")
  String token;

  /**
   * String with 2 decimal places e.g “25.00”.
   */
  @SerializedName("taxAmount")
  String taxAmount;

  /**
   * String with 2 decimal places e.g “25.00”.
   */
  @SerializedName("tip")
  String tip;

  /**
   * Enum: "card_on_file" "keyed" "recurring" "ecommerce"
   */
  @SerializedName("entryType")
  SPEntryType entryType;

  @SerializedName("taxExempt")
  Boolean taxExempt;

}
