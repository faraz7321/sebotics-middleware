package sebotics.middleware.api.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterDeviceRequest(
		@NotBlank(message = "Serial number is required")
		String serialNumber,

		@NotBlank(message = "MAC address is required")
		@Pattern(
				regexp = "^[0-9A-Fa-f]{2}([-:])[0-9A-Fa-f]{2}(\\1[0-9A-Fa-f]{2}){4}$",
				message = "MAC address must be in format XX:XX:XX:XX:XX:XX"
		)
		String macAddress,

		@NotBlank(message = "Elevator vendor is required")
		String elevatorVendor
) {
}
