package dopc.delivery_price_calculator.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StaticVenueData(
    @JsonProperty("venue_raw") val venueRaw: StaticVenueRaw
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class StaticVenueRaw(
    @JsonProperty("location") val location: Location
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
    @JsonProperty("coordinates") val coordinates: List<Double>
)
