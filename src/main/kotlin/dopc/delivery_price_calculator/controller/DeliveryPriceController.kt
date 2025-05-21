package dopc.delivery_price_calculator.controller

import dopc.delivery_price_calculator.model.DeliveryRequest
import dopc.delivery_price_calculator.model.DeliveryResponse
import dopc.delivery_price_calculator.service.DeliveryPriceService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/v1/"])
class DeliveryPriceController(private val deliveryPriceService: DeliveryPriceService) {

    @GetMapping("delivery-order-price")
    fun getDeliveryOrderPrice(@Valid request: DeliveryRequest): ResponseEntity<DeliveryResponse> {
        val response = deliveryPriceService.calculateDeliveryPrice(request)
        return ResponseEntity.ok(response)
    }
}
