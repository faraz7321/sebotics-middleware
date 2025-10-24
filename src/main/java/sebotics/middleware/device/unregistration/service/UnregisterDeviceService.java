package sebotics.middleware.device.unregistration.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;
import sebotics.middleware.device.unregistration.dto.UnregisterDeviceRequest;

@Service
public class UnregisterDeviceService {

	private static final Logger log = LoggerFactory.getLogger(UnregisterDeviceService.class);

	private final DeviceRegistrationRepository repository;

	public UnregisterDeviceService(DeviceRegistrationRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void execute(UnregisterDeviceRequest request) {
		UUID deviceId = request.deviceId();
		log.info("Unregistering device deviceId={}", deviceId);
		DeviceRegistration device = repository.findById(deviceId)
				.orElseThrow(() -> {
					log.warn("Unregister failed - deviceId={} not found", deviceId);
					return new DeviceNotFoundException("Device with id %s not found".formatted(deviceId));
				});

		repository.delete(device);
		log.info("Successfully unregistered deviceId={}", deviceId);
	}
}
