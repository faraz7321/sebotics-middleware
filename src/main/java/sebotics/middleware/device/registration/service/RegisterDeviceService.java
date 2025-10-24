package sebotics.middleware.device.registration.service;

import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.device.registration.dto.RegisterDeviceRequest;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;

@Service
public class RegisterDeviceService {

	private final DeviceRegistrationRepository repository;

	public RegisterDeviceService(DeviceRegistrationRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public RegisterDeviceResponse execute(RegisterDeviceRequest request) {
		String serialNumber = request.serialNumber().trim();
		String macAddress = normalizeMacAddress(request.macAddress());
		String elevatorVendor = request.elevatorVendor().trim();

		boolean alreadyRegistered = repository.existsBySerialNumberIgnoreCaseOrMacAddressIgnoreCase(
				serialNumber,
				macAddress
		);

		if (alreadyRegistered) {
			throw new DeviceAlreadyRegisteredException(
					"Device with provided serial number or MAC address is already registered"
			);
		}

		DeviceRegistration entity = repository.save(
				DeviceRegistration.create(serialNumber, macAddress, elevatorVendor)
		);

		return RegisterDeviceResponse.from(entity);
	}

	private String normalizeMacAddress(String macAddress) {
		String trimmed = macAddress.trim();
		return trimmed.replace('-', ':').toUpperCase(Locale.ROOT);
	}
}
