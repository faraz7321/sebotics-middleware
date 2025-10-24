package sebotics.middleware.device.unregistration.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;
import sebotics.middleware.device.unregistration.dto.UnregisterDeviceRequest;

@Service
public class UnregisterDeviceService {

	private final DeviceRegistrationRepository repository;

	public UnregisterDeviceService(DeviceRegistrationRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void execute(UnregisterDeviceRequest request) {
		UUID deviceId = request.deviceId();
		DeviceRegistration device = repository.findById(deviceId)
				.orElseThrow(() -> new DeviceNotFoundException("Device with id %s not found".formatted(deviceId)));

		repository.delete(device);
	}
}
