package sebotics.middleware.device.unregistration.dto;

import jakarta.validation.constraints.NotBlank;

public record UnregisterDeviceRequest(
		@NotBlank(message = "Device id is required")
		String deviceId
) {
}
