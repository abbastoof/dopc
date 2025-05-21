package dopc.delivery_price_calculator.service

import dopc.delivery_price_calculator.api.*
import dopc.delivery_price_calculator.exception.DeliveryNotPossibleException
import dopc.delivery_price_calculator.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class DeliveryPriceServiceUnitTests {

    private lateinit var externalApiClient: ExternalApiClient
    private lateinit var deliveryPriceService: DeliveryPriceService

    @BeforeEach
    fun setUp() {
        val dynamicVenueData = DynamicVenueData(
            VenueRaw(
                deliverySpecs = DeliverySpecs(
                    orderMinimumNoSurcharge = 1000,
                    deliveryPricing = DeliveryPricing(
                        basePrice = 190,
                        distanceRanges = listOf(
                            DistanceRange(min = 0, max = 500, a = 0, b = 0),
                            DistanceRange(min = 500, max = 1000, a = 100, b = 1),
                            DistanceRange(min = 1000, max = 0, a = 0, b = 0)
                        )
                    )
                )
            )
        )
        externalApiClient = Mockito.mock(ExternalApiClient::class.java)
        deliveryPriceService = DeliveryPriceService(externalApiClient)
        Mockito.`when`(externalApiClient.fetchVenueCoordinates("home-assignment-venue-helsinki"))
            .thenReturn(Pair(60.17012143, 24.92813512))

        Mockito.`when`(externalApiClient.fetchVenueDynamicData("home-assignment-venue-helsinki"))
            .thenReturn(dynamicVenueData)

    }

    @Test
    fun `throws exception when delivery distance exceeds maximum range`() {
        val request = DeliveryRequest(
            venue_slug = "home-assignment-venue-helsinki",
            cart_value = 1000,
            user_lat = 90.0, // Far latitude
            user_lon = 180.0 // Far longitude
        )

        val exception = assertThrows<DeliveryNotPossibleException> {
            deliveryPriceService.calculateDeliveryPrice(request)
        }

        assertEquals("Delivery distance 3316931 is out of range.", exception.message)
    }

    @Test
    fun `calculate delivery price when user is at the venue`() {
        val request = DeliveryRequest(
            venue_slug = "home-assignment-venue-helsinki",
            cart_value = 1000,
            user_lat = 60.17012143, // Identical to venue's latitude
            user_lon = 24.92813512  // Identical to venue's longitude
        )

        val actualResult = deliveryPriceService.calculateDeliveryPrice(request)
        val expectedResult = DeliveryResponse(1190, 0, 1000, DeliveryResponse.DeliveryDetails(190, 0))
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `should add a small order surcharge`() {
        val request = DeliveryRequest(
            venue_slug = "home-assignment-venue-helsinki",
            cart_value = 800,
            user_lat = 60.17094,
            user_lon = 24.93087
        )

        val actualResult = deliveryPriceService.calculateDeliveryPrice(request)
        val expectedResult = DeliveryResponse(1190, 200, 800, DeliveryResponse.DeliveryDetails(190, 177))
        assertEquals(expectedResult, actualResult)

    }

    @Test
    fun `does not add small order surcharge for sufficient cart value`() {
        val request = DeliveryRequest(
            venue_slug = "home-assignment-venue-helsinki",
            cart_value = 1500,
            user_lat = 60.17094,
            user_lon = 24.93087
        )

        val actualResult = deliveryPriceService.calculateDeliveryPrice(request)
        val expectedResult = DeliveryResponse(1690, 0, 1500, DeliveryResponse.DeliveryDetails(190, 177))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `calculates delivery price for non-zero distance`() {
        val request = DeliveryRequest(
            venue_slug = "home-assignment-venue-helsinki",
            cart_value = 1000,
            user_lat = 60.16994,
            user_lon = 24.92087
        )

        val actualResult = deliveryPriceService.calculateDeliveryPrice(request)
        val expectedResult = DeliveryResponse(1190, 0, 1000, DeliveryResponse.DeliveryDetails(190, 402))
        assertEquals(expectedResult, actualResult)
    }


}
