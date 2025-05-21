package dopc.delivery_price_calculator.service

import dopc.delivery_price_calculator.api.ExternalApiClient
import dopc.delivery_price_calculator.exception.DeliveryNotPossibleException
import dopc.delivery_price_calculator.model.DeliveryRequest
import dopc.delivery_price_calculator.model.DeliveryResponse
import dopc.delivery_price_calculator.api.DynamicVenueData
import org.springframework.stereotype.Service
import kotlin.math.*

@Service
class DeliveryPriceService(
    private val externalApiClient: ExternalApiClient
) {

    fun calculateDeliveryPrice(request: DeliveryRequest): DeliveryResponse {
        // Fetch venue data
        val venueCoordinates = externalApiClient.fetchVenueCoordinates(request.venue_slug)
        val dynamicData = externalApiClient.fetchVenueDynamicData(request.venue_slug)

        // Calculate delivery distance
        val deliveryDistance = calculateDistance(
            userLatitude = request.user_lat,
            userLongitude = request.user_lon,
            venueLatitude = venueCoordinates.first,
            venueLongitude = venueCoordinates.second
        )

        // Calculate fees
        val deliveryFee = calculateDeliveryFee(dynamicData, deliveryDistance)
        val smallOrderSurcharge = calculateSmallOrderSurcharge(dynamicData.venueRaw.deliverySpecs.orderMinimumNoSurcharge, request.cart_value)

        // Calculate total price
        val totalPrice = request.cart_value + deliveryFee + smallOrderSurcharge

        return DeliveryResponse(
            totalPrice = totalPrice,
            smallOrderSurcharge = smallOrderSurcharge,
            cartValue = request.cart_value,
            delivery = DeliveryResponse.DeliveryDetails(
                fee = deliveryFee,
                distance = deliveryDistance
            )
        )
    }

    private fun calculateDistance(userLatitude: Double, userLongitude: Double,
                                  venueLatitude: Double, venueLongitude: Double): Int {
        // Haversine formula for distance calculation
        val earthRadiusInMeters = 6371e3 // Earth's radius in meters

        val latitudeDifference = Math.toRadians(userLatitude - venueLatitude)
        val longitudeDifference = Math.toRadians(userLongitude - venueLongitude)

        val haversineFormulaComponent = sin(latitudeDifference / 2).pow(2) +
                cos(Math.toRadians(venueLatitude)) * cos(Math.toRadians(userLatitude)) *
                sin(longitudeDifference / 2).pow(2)

        val angularDistance = 2 * atan2(sqrt(haversineFormulaComponent), sqrt(1 - haversineFormulaComponent))

        return (earthRadiusInMeters * angularDistance).roundToInt()
    }


    fun calculateDeliveryFee(dynamicData: DynamicVenueData, distance: Int): Int {
        val distanceRanges = dynamicData.venueRaw.deliverySpecs.deliveryPricing.distanceRanges
        val basePrice = dynamicData.venueRaw.deliverySpecs.deliveryPricing.basePrice

        for (range in distanceRanges) {
            if (range.max == 0 && distance >= range.min) {
                throw DeliveryNotPossibleException("Delivery distance $distance is out of range.")
            }

            if (distance in range.min until range.max) {
                return (basePrice + range.a + range.b * distance.toDouble() / 10).roundToInt()
            }
        }

        throw DeliveryNotPossibleException("Delivery distance $distance is out of range.")
    }


    private fun calculateSmallOrderSurcharge(minimumCartValue: Int, cartValue: Int): Int {
        return if (cartValue < minimumCartValue) minimumCartValue - cartValue else 0
    }
}
