package dopc.delivery_price_calculator.validation

import dopc.delivery_price_calculator.model.DeliveryRequest
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DeliveryRequestValidationTests {

    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid request passes validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = 1000,
            user_lat = 60.17094,
            user_lon = 24.93087
        )

        val violations = validator.validate(request)
        assertEquals(0, violations.size, "Expected no validation errors for a valid request")
    }

    @Test
    fun `empty venue_slug fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "",
            cart_value = 1000,
            user_lat = 60.17094,
            user_lon = 24.93087
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for empty venue_slug")
        assertEquals("venue_slug is required", violations.first().message)
    }

    @Test
    fun `negative cart_value fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = -100,
            user_lat = 60.17094,
            user_lon = 24.93087
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for negative cart_value")
        assertEquals("cart_value must be positive", violations.first().message)
    }

    @Test
    fun `zero cart_value fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = 0,
            user_lat = 60.17094,
            user_lon = 24.93087
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for zero cart_value")
        assertEquals("cart_value must be positive", violations.first().message)
    }

    @Test
    fun `invalid user_lat below -90 fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = 1000,
            user_lat = -91.0, // Invalid latitude
            user_lon = 24.93087
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for user_lat below -90")
        assertEquals("User Latitude must be >= -90.0", violations.first().message)
    }

    @Test
    fun `invalid user_lat above 90 fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = 1000,
            user_lat = 91.0, // Invalid latitude
            user_lon = 24.93087
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for user_lat above 90")
        assertEquals("User Latitude must be <= 90.0", violations.first().message)

    }

    @Test
    fun `invalid user_lon below -180 fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = 1000,
            user_lat = 60.17094,
            user_lon = -181.0 // Invalid longitude
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for user_lon below -180")
        assertEquals("User Longitude must be >= -180.0", violations.first().message)
    }

    @Test
    fun `invalid user_lon above 180 fails validation`() {
        val request = DeliveryRequest(
            venue_slug = "valid-venue",
            cart_value = 1000,
            user_lat = 60.17094,
            user_lon = 181.0 // Invalid longitude
        )

        val violations = validator.validate(request)
        assertEquals(1, violations.size, "Expected validation error for user_lon above 180")
        assertEquals("User Longitude must be <= 180.0", violations.first().message)
    }
}
