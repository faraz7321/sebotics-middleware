package sebotics.middleware.device.unregistration.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.device.unregistration.dto.UnregisterDeviceRequest;
import sebotics.middleware.device.unregistration.service.UnregisterDeviceService;

@RestController
@RequestMapping("/api/devices")
public class UnregisterDeviceController {

	private final UnregisterDeviceService service;

	public UnregisterDeviceController(UnregisterDeviceService service) {
		this.service = service;
	}

	@DeleteMapping({"/{deviceId}", "/{deviceId}/unregister"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unregister(@PathVariable UUID deviceId) {
		service.execute(new UnregisterDeviceRequest(deviceId));
	}
}
