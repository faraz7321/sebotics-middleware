package sebotics.middleware.common.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
import sebotics.middleware.device.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.device.exception.DeviceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request
	) {
		Map<String, Object> body = errorBody("VALIDATION_FAILED", "Request validation failed");
		List<Map<String, String>> fieldErrors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(this::fieldError)
				.collect(Collectors.toList());
		body.put("errors", fieldErrors);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, Object> body = errorBody("CONSTRAINT_VIOLATION", "Constraint violation encountered");
		body.put("errors", ex.getConstraintViolations()
				.stream()
				.map(violation -> Map.of(
						"property", violation.getPropertyPath().toString(),
						"message", violation.getMessage()
				))
				.collect(Collectors.toList()));
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(DeviceAlreadyRegisteredException.class)
	public ResponseEntity<Map<String, Object>> handleDeviceAlreadyRegistered(DeviceAlreadyRegisteredException ex) {
		return ResponseEntity.status(409).body(errorBody("DEVICE_ALREADY_REGISTERED", ex.getMessage()));
	}

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleDeviceNotFound(DeviceNotFoundException ex) {
		return ResponseEntity.status(404).body(errorBody("DEVICE_NOT_FOUND", ex.getMessage()));
	}

	private Map<String, Object> errorBody(String code, String message) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", OffsetDateTime.now(ZoneOffset.UTC));
		body.put("error", code);
		body.put("message", message);
		return body;
	}

	private Map<String, String> fieldError(FieldError error) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("field", error.getField());
		map.put("message", error.getDefaultMessage());
		return map;
	}
}
