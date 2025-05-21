package dopc.delivery_price_calculator.api

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class ExternalApiClient(private val restTemplate: RestTemplate) {

    fun fetchVenueCoordinates(venueSlug: String): Pair<Double, Double> {
        val url = "https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1/venues/$venueSlug/static"

        // Automatically deserialize the response into StaticVenueData
        val response: StaticVenueData = restTemplate.getForObject(url, StaticVenueData::class.java)
            ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST)

        val coordinates = response.venueRaw.location.coordinates

        // Ensure coordinates contain exactly two elements
        if (coordinates.size != 2) {
            throw RuntimeException("Invalid data format: Coordinates must be a list of two doubles")
        }

        return Pair(coordinates[1], coordinates[0]) // [latitude, longitude]
    }

    fun fetchVenueDynamicData(venueSlug: String): DynamicVenueData {
        val url = "https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1/venues/$venueSlug/dynamic"

        return restTemplate.getForObject(url, DynamicVenueData::class.java)
            ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST)
    }
}