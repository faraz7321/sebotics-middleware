package sebotics.middleware.device.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.device.dto.DeviceRegistrationRequest;
import sebotics.middleware.device.dto.DeviceRegistrationResponse;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.service.DeviceRegistrationService;

@RestController
@RequestMapping("/api/devices")
public class DeviceRegistrationController {

	private final DeviceRegistrationService deviceRegistrationService;

	public DeviceRegistrationController(DeviceRegistrationService deviceRegistrationService) {
		this.deviceRegistrationService = deviceRegistrationService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public DeviceRegistrationResponse register(@Valid @RequestBody DeviceRegistrationRequest request) {
		DeviceRegistration device = deviceRegistrationService.registerDevice(request);
		return DeviceRegistrationResponse.from(device);
	}

	@DeleteMapping("/{deviceId}/unregister")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unregister(@PathVariable UUID deviceId) {
		deviceRegistrationService.unregisterDevice(deviceId);
	}
}
