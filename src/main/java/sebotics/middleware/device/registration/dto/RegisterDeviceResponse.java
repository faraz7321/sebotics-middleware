package sebotics.middleware.device.registration.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import sebotics.middleware.device.entity.DeviceRegistration;

public record RegisterDeviceResponse(
		UUID id,
		String serialNumber,
		String macAddress,
		String elevatorVendor,
		OffsetDateTime registeredAt
) {

	public static RegisterDeviceResponse from(DeviceRegistration entity) {
		return new RegisterDeviceResponse(
				entity.getId(),
				entity.getSerialNumber(),
				entity.getMacAddress(),
				entity.getElevatorVendor(),
				entity.getRegisteredAt()
		);
	}
}
