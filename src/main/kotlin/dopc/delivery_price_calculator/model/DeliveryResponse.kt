package dopc.delivery_price_calculator.model

import com.fasterxml.jackson.annotation.JsonProperty

data class DeliveryResponse(
    @JsonProperty("total_price") val totalPrice: Int,
    @JsonProperty("small_order_surcharge") val smallOrderSurcharge: Int,
    @JsonProperty("cart_value") val cartValue: Int,
    @JsonProperty("delivery") val delivery: DeliveryDetails
) {

    data class DeliveryDetails(
        @JsonProperty("fee") val fee: Int,
        @JsonProperty("distance") val distance: Int
    )
}
