package sebotics.middleware.api.v1.device.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sebotics.middleware.api.v1.device.domain.entity.DeviceRegistration;
import sebotics.middleware.api.v1.device.domain.exception.DeviceNotFoundException;
import sebotics.middleware.api.v1.device.domain.repository.DeviceRegistrationRepository;

@Service
public class UnregisterDeviceService {

	private static final Logger log = LoggerFactory.getLogger(UnregisterDeviceService.class);

	private final DeviceRegistrationRepository repository;

	public UnregisterDeviceService(DeviceRegistrationRepository repository) {
		this.repository = repository;
	}

	@Transactional
    public void execute(String deviceId) {
        String normalizedId = deviceId.trim();
        log.info("Unregistering device deviceId={}", deviceId);
        DeviceRegistration device = repository.findByDeviceIdIgnoreCase(normalizedId)
                .orElseThrow(() -> {
                    log.warn("Unregister failed - deviceId={} not found", normalizedId);
                    return new DeviceNotFoundException("Device with id %s not found".formatted(normalizedId));
                });

        repository.delete(device);
        log.info("Successfully unregistered deviceId={}", normalizedId);
    }
}
