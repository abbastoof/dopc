package dopc.delivery_price_calculator.model

import jakarta.validation.constraints.*

data class DeliveryRequest(
    @field:NotBlank(message = "venue_slug is required")
    val venue_slug: String,

    @field:NotNull(message = "cart_value is required")
    @field:Min(1, message = "cart_value must be positive")
    val cart_value: Int,

    @field:NotNull(message = "user_lat is required")
    @field: DecimalMin(value = "-90.0", inclusive = true, message = "User Latitude must be >= -90.0")
    @field: DecimalMax(value = "90.0", inclusive= true, message = "User Latitude must be <= 90.0")
    val user_lat: Double,

    @field:NotNull(message = "user_lon is required")
    @field: DecimalMin(value = "-180.0", inclusive = true, message = "User Longitude must be >= -180.0")
    @field: DecimalMax(value = "180.0", inclusive= true, message = "User Longitude must be <= 180.0")
    val user_lon: Double
)
