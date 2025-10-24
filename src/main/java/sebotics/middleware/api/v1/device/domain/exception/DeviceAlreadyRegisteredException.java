package sebotics.middleware.api.v1.device.domain.exception;

public class DeviceAlreadyRegisteredException extends RuntimeException {

	public DeviceAlreadyRegisteredException(String message) {
		super(message);
	}
}
