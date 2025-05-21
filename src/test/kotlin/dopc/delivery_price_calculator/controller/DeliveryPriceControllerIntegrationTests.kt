package dopc.delivery_price_calculator.controller

import com.jayway.jsonpath.JsonPath
import dopc.delivery_price_calculator.model.DeliveryResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryPriceControllerIntegrationTests {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    fun buildUrl(
        venueSlug: String, cartValue: Int?, userLat: Double, userLon: Double,
    ): String {
        return "/api/v1/delivery-order-price?venue_slug=$venueSlug&cart_value=$cartValue&user_lat=$userLat&user_lon=$userLon"
    }

    var venueSlug: String = "home-assignment-venue-helsinki"
    var cartValue: Int = 0
    var userLat: Double = 0.0
    var userLon = 0.0

    @BeforeEach
    fun setup() {
        cartValue = 1000
        userLat = 60.17094
        userLon = 24.93087
    }

    @Test
    fun `getDeliveryOrderPrice returns correct response`() {

        val url = buildUrl(venueSlug, cartValue, userLat, userLon)
        val response = restTemplate.getForEntity(url, DeliveryResponse::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        // Assert: Verify the response
        val expectedResult = DeliveryResponse(1190, 0, 1000, DeliveryResponse.DeliveryDetails(190, 177))
        assertEquals(expectedResult, response.body)
    }

    @Test
    fun `getDeliveryOrderPrice returns 400 BAD_REQUEST for invalid parameters`() {
        val url = buildUrl("", 0, userLat, userLon)
        val response = testRestTemplate.getForEntity(url, String::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

        val documentContext = JsonPath.parse(response.body)
        val venueSlugErrorMessage = documentContext.read<String>("$.venue_slug")
        val cartValueErrorMessage = documentContext.read<String>("$.cart_value")
        assert(venueSlugErrorMessage.contains("venue_slug is required") == true)
        assert(cartValueErrorMessage.contains("cart_value must be positive") == true)
        println(response.body)
    }
}
