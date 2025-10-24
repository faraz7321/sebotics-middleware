package sebotics.middleware.api.v1.device.domain.exception;

public class DeviceNotFoundException extends RuntimeException {

	public DeviceNotFoundException(String message) {
		super(message);
	}
}
