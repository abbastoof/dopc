# Delivery Price Calculator Service (DOPC)

This project implements a backend service capable of calculating the total price and price breakdown of a delivery order, adhering to the requirements outlined in the Wolt 2025 Backend Engineering Internship assignment.

## Overview
The Delivery Order Price Calculator (DOPC) service exposes a single endpoint for clients to calculate delivery prices based on venue data and user-provided parameters. It integrates with an external API (Home Assignment API) to fetch necessary venue data.

### Features
- Calculates delivery distance using the Haversine formula.
- Determines delivery fees based on distance ranges.
- Calculates small order surcharges if the cart value is below a certain threshold.
- Returns a detailed JSON response with the total price breakdown.

---

## Project Structure
```
.
├── src
│   ├── main
│   │   ├── kotlin
│   │   │   └── dopc
│   │   │       ├── api
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── exception
│   │   │       ├── model
│   │   │       ├── service
│   │   │       └── validation
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   └── test
│       └── kotlin
│           └── dopc
│               ├── controller
│               ├── service
│               └── validation
```
### Key Directories and Files

#### `api`
- **Files:**
    - `DynamicVenueResponse.kt`: Models dynamic data from the Home Assignment API.
    - `StaticVenueResponse.kt`: Models static data from the Home Assignment API.
    - `ExternalApiClient.kt`: A utility class for fetching data from the Home Assignment API.

#### `config`
- **Files:**
    - `RestTemplateConfig.kt`: Configures a `RestTemplate` bean for making HTTP calls.

#### `controller`
- **Files:**
    - `DeliveryPriceController.kt`: Contains the main REST endpoint `/api/v1/delivery-order-price`. Validates user input, processes the request, and returns the response.

#### `exception`
- **Files:**
    - `DeliveryNotPossibleException.kt`: Custom exception for cases where delivery is not possible.
    - `GlobalExceptionHandler.kt`: Centralized exception handler for translating exceptions into meaningful HTTP responses.

#### `model`
- **Files:**
    - `DeliveryRequest.kt`: Represents incoming request data, with validation annotations.
    - `DeliveryResponse.kt`: Represents the structured response returned by the service.

#### `service`
- **Files:**
    - `DeliveryPriceService.kt`: Core logic for calculating delivery distance, delivery fees, and total prices.

#### `validation`
- **Files:**
    - `DeliveryRequestValidationTests.kt`: Validates the `DeliveryRequest` model's constraints.

---

## Endpoint Documentation

### **GET** `/api/v1/delivery-order-price`
#### Query Parameters
| Name       | Type    | Description                                                |
|------------|---------|------------------------------------------------------------|
| venue_slug | String  | Unique identifier of the venue.                           |
| cart_value | Integer | Total value of items in the shopping cart (in cents).     |
| user_lat   | Double  | Latitude of the user's location.                           |
| user_lon   | Double  | Longitude of the user's location.                          |

#### Response
| Field                  | Type    | Description                                          |
|------------------------|---------|------------------------------------------------------|
| total_price            | Integer | Calculated total price.                             |
| small_order_surcharge  | Integer | Surcharge applied if the cart value is too low.     |
| cart_value             | Integer | Original cart value.                                |
| delivery.fee           | Integer | Calculated delivery fee.                            |
| delivery.distance      | Integer | Calculated delivery distance (meters).              |

---

## Key Classes and Methods

### `DeliveryPriceService`
- **`calculateDeliveryPrice(request: DeliveryRequest): DeliveryResponse`**
    - Orchestrates the calculations by fetching venue data, calculating delivery distance, fees, and total price.

- **`calculateDistance(userLatitude, userLongitude, venueLatitude, venueLongitude): Int`**
    - Uses the Haversine formula to compute the straight-line distance in meters.

- **`calculateDeliveryFee(dynamicData: DynamicVenueData, distance: Int): Int`**
    - Determines the delivery fee based on dynamic venue data and distance ranges.

- **`calculateSmallOrderSurcharge(minimumCartValue: Int, cartValue: Int): Int`**
    - Computes the surcharge for small orders.

### `DeliveryPriceController`
- **`getDeliveryOrderPrice(request: DeliveryRequest): ResponseEntity<DeliveryResponse>`**
    - Validates the incoming request, delegates to the service layer, and returns the calculated response.

### `ExternalApiClient`
- **`fetchVenueCoordinates(venueSlug: String): Pair<Double, Double>`**
    - Retrieves venue coordinates (longitude and latitude).

- **`fetchVenueDynamicData(venueSlug: String): DynamicVenueData`**
    - Fetches dynamic venue data, including pricing specs and delivery specs.

---

## Testing

### Unit Tests
- **`DeliveryPriceServiceTests.kt`**
    - Validates service logic, including:
        - Fee calculations.
        - Distance calculations.
        - Small order surcharges.

### Validation Tests
- **`DeliveryRequestValidationTests.kt`**
    - Ensures `DeliveryRequest` validation rules are correctly enforced, e.g., invalid latitude/longitude values.

### Integration Tests
- **`DeliveryPriceControllerIntegrationTests.kt`**
    - End-to-end tests for the controller, validating proper request handling and response generation.

---

## Installation and Usage

### Prerequisites
- **Java 17**
- **Kotlin 1.7**
- **Gradle 8.0 or higher**

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd delivery-price-calculator
   ```
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### Testing
Run all tests with:
```bash
./gradlew test
```
Test reports are available under `build/reports/tests/test/index.html`.

---

## Notes
- The project follows clean architecture principles to ensure maintainability.
- Exception handling is centralized via the `GlobalExceptionHandler`.
- Validation is handled using annotations in the `DeliveryRequest` class.

---

## Contact
For any questions or issues, feel free to reach out to:
- **Name:** Abbas Toof
- **Email:** abbas.toof@gmail.com
- **GitHub:** https://github.com/abbastoof/
- **LinkedIn:** https://www.linkedin.com/in/abbastoof/

---

Thank you for reviewing my submission!

