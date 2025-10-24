package sebotics.middleware.device.unregistration.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UnregisterDeviceRequest(
		@NotNull(message = "Device id is required")
		UUID deviceId
) {
}
