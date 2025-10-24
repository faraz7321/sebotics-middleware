package sebotics.middleware.device.service;

import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sebotics.middleware.device.dto.DeviceRegistrationRequest;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;

@Service
public class DeviceRegistrationService {

	private final DeviceRegistrationRepository repository;

	public DeviceRegistrationService(DeviceRegistrationRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public DeviceRegistration registerDevice(DeviceRegistrationRequest request) {
		String serial = request.serialNumber().trim();
		String macAddress = normalizeMacAddress(request.macAddress());
		String elevatorVendor = request.elevatorVendor().trim();

		boolean alreadyRegistered = repository.existsBySerialNumberIgnoreCaseOrMacAddressIgnoreCase(
				serial,
				macAddress
		);

		if (alreadyRegistered) {
			throw new DeviceAlreadyRegisteredException(
					"Device with provided serial number or MAC address is already registered"
			);
		}

		DeviceRegistration device = new DeviceRegistration(serial, macAddress, elevatorVendor);
		return repository.save(device);
	}

	@Transactional
	public void unregisterDevice(UUID deviceId) {
		DeviceRegistration device = repository.findById(deviceId)
				.orElseThrow(() -> new DeviceNotFoundException("Device with id %s not found".formatted(deviceId)));
		repository.delete(device);
	}

	private String normalizeMacAddress(String macAddress) {
		String trimmed = macAddress.trim();
		String normalized = trimmed.replace('-', ':').toUpperCase(Locale.ROOT);
		return normalized;
	}
}
