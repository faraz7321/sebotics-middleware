package sebotics.middleware.device.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import sebotics.middleware.device.entity.DeviceRegistration;

public record DeviceRegistrationResponse(
		UUID id,
		String serialNumber,
		String macAddress,
		String elevatorVendor,
		OffsetDateTime registeredAt
) {

	public static DeviceRegistrationResponse from(DeviceRegistration device) {
		return new DeviceRegistrationResponse(
				device.getId(),
				device.getSerialNumber(),
				device.getMacAddress(),
				device.getElevatorVendor(),
				device.getRegisteredAt()
		);
	}
}
