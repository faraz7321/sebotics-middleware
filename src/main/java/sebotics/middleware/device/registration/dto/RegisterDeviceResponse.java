package sebotics.middleware.device.registration.dto;

import java.time.OffsetDateTime;
import sebotics.middleware.device.entity.DeviceRegistration;

public record RegisterDeviceResponse(
		String deviceId,
		String serialNumber,
		String macAddress,
		String elevatorVendor,
		OffsetDateTime registeredAt
) {

	public static RegisterDeviceResponse from(DeviceRegistration entity) {
		return new RegisterDeviceResponse(
				entity.getDeviceId(),
				entity.getSerialNumber(),
				entity.getMacAddress(),
				entity.getElevatorVendor(),
				entity.getRegisteredAt()
		);
	}
}
