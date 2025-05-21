package dopc.delivery_price_calculator.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class DistanceRange(
    val min: Int,
    val max: Int,
    val a: Int,
    val b: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DynamicVenueData(
    @JsonProperty("venue_raw") val venueRaw: VenueRaw
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VenueRaw(
    @JsonProperty("delivery_specs") val deliverySpecs: DeliverySpecs
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeliverySpecs(
    @JsonProperty("order_minimum_no_surcharge") val orderMinimumNoSurcharge: Int,
    @JsonProperty("delivery_pricing") val deliveryPricing: DeliveryPricing
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeliveryPricing(
    @JsonProperty("base_price") val basePrice: Int,
    @JsonProperty("distance_ranges") val distanceRanges: List<DistanceRange>
)