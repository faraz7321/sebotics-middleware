package sebotics.middleware.api.v1.device.application.service;

import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sebotics.middleware.api.v1.device.domain.entity.DeviceRegistration;
import sebotics.middleware.api.v1.device.domain.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.api.v1.device.domain.repository.DeviceRegistrationRepository;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceRequest;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceResponse;

@Service
public class RegisterDeviceService {

	private static final Logger log = LoggerFactory.getLogger(RegisterDeviceService.class);

	private final DeviceRegistrationRepository repository;

	public RegisterDeviceService(DeviceRegistrationRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public RegisterDeviceResponse execute(RegisterDeviceRequest request) {
		String serialNumber = request.serialNumber().trim();
		String macAddress = normalizeMacAddress(request.macAddress());
        String elevatorVendor = request.elevatorVendor().trim().toUpperCase(Locale.ROOT);

		log.info("Registering device serialNumber={} elevatorVendor={}", serialNumber, elevatorVendor);

		boolean serialExists = repository.existsBySerialNumberIgnoreCase(serialNumber);
		boolean combinationExists = repository.existsBySerialNumberIgnoreCaseAndMacAddressIgnoreCase(serialNumber, macAddress);

		if (serialExists || combinationExists) {
			log.warn("Attempt to register duplicate device serialNumber={} macAddress={}", serialNumber, macAddress);
			throw new DeviceAlreadyRegisteredException(
					"Device with provided serial number and MAC address is already registered"
			);
		}

		String deviceId = generateUniqueDeviceId();

		DeviceRegistration entity = repository.save(
				DeviceRegistration.create(deviceId, serialNumber, macAddress, elevatorVendor)
		);

		log.info("Successfully registered deviceId={} serialNumber={}", entity.getDeviceId(), serialNumber);

		return RegisterDeviceResponse.from(entity);
	}

	private String generateUniqueDeviceId() {
		String candidate;
		do {
			candidate = "dev-" + UUID.randomUUID();
		} while (repository.existsByDeviceIdIgnoreCase(candidate));
		return candidate;
	}

	private String normalizeMacAddress(String macAddress) {
		String trimmed = macAddress.trim();
		return trimmed.replace('-', ':').toUpperCase(Locale.ROOT);
	}
}
