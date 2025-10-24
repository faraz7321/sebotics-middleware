package sebotics.middleware.device.registration.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.device.registration.dto.RegisterDeviceRequest;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.registration.service.RegisterDeviceService;

@RestController
@RequestMapping("/api/devices")
public class RegisterDeviceController {

	private static final Logger log = LoggerFactory.getLogger(RegisterDeviceController.class);

	private final RegisterDeviceService service;

	public RegisterDeviceController(RegisterDeviceService service) {
		this.service = service;
	}

	@PostMapping({"", "/register"})
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterDeviceResponse register(@Valid @RequestBody RegisterDeviceRequest request) {
		log.debug("Received register request serialNumber={} elevatorVendor={}",
				request.serialNumber(),
				request.elevatorVendor());
		return service.execute(request);
	}
}
