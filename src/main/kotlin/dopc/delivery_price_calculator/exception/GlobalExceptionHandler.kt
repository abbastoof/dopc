package dopc.delivery_price_calculator.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import com.jayway.jsonpath.PathNotFoundException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.client.HttpClientErrorException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, String>> {
        val error = mapOf(
            "error" to "Invalid parameter type",
            "parameter" to ex.name,
            "message" to ex.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(DeliveryNotPossibleException::class)
    fun handleDeliveryNotPossibleException(ex: DeliveryNotPossibleException): ResponseEntity<Map<String, String>> {
        val error = mapOf("error" to ex.message.orEmpty())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(PathNotFoundException::class)
    fun handlePathNotFoundException(ex: PathNotFoundException): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf(
            "error" to "Invalid JSON path",
            "message" to (ex.message ?: "No results found for the specified path")
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf("error" to ex.message.orEmpty())
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientErrorException(ex: HttpClientErrorException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(ex.responseBodyAsString)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(err: MethodArgumentNotValidException)
            : ResponseEntity<Map<String, String>> {
        val errors = err.bindingResult.fieldErrors.associate { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Invalid value"
            fieldName to errorMessage
        }
        return ResponseEntity.badRequest().body(errors)
    }

}
