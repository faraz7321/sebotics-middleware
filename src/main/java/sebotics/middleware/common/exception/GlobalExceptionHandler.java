package sebotics.middleware.common.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sebotics.middleware.common.dto.ApiResponse;
import sebotics.middleware.device.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.exception.InvalidDeviceQueryException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request
	) {
		List<Map<String, String>> fieldErrors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(this::fieldError)
				.collect(Collectors.toList());
		return ResponseEntity.badRequest()
				.body(ApiResponse.failure("VALIDATION_FAILED", "Request validation failed", fieldErrors));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<List<Map<String, String>>>> handleConstraintViolation(ConstraintViolationException ex) {
		List<Map<String, String>> errors = ex.getConstraintViolations()
				.stream()
				.map(violation -> Map.of(
						"property", violation.getPropertyPath().toString(),
						"message", violation.getMessage()
				))
				.collect(Collectors.toList());
		return ResponseEntity.badRequest()
				.body(ApiResponse.failure("CONSTRAINT_VIOLATION", "Constraint violation encountered", errors));
	}

	@ExceptionHandler(DeviceAlreadyRegisteredException.class)
	public ResponseEntity<ApiResponse<Void>> handleDeviceAlreadyRegistered(DeviceAlreadyRegisteredException ex) {
		return ResponseEntity.status(409)
				.body(ApiResponse.failure("DEVICE_ALREADY_REGISTERED", ex.getMessage()));
	}

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleDeviceNotFound(DeviceNotFoundException ex) {
		return ResponseEntity.status(404)
				.body(ApiResponse.failure("DEVICE_NOT_FOUND", ex.getMessage()));
	}

	@ExceptionHandler(InvalidDeviceQueryException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidDeviceQuery(InvalidDeviceQueryException ex) {
		return ResponseEntity.badRequest()
				.body(ApiResponse.failure("INVALID_DEVICE_QUERY", ex.getMessage()));
	}

	private Map<String, String> fieldError(FieldError error) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("field", error.getField());
		map.put("message", error.getDefaultMessage());
		return map;
	}
}
