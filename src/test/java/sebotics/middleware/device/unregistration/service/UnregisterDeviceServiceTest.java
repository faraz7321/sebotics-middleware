package sebotics.middleware.device.unregistration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.registration.dto.RegisterDeviceRequest;
import sebotics.middleware.device.registration.service.RegisterDeviceService;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;
import sebotics.middleware.device.unregistration.dto.UnregisterDeviceRequest;

@DataJpaTest
@Import({RegisterDeviceService.class, UnregisterDeviceService.class})
class UnregisterDeviceServiceTest {

	@Autowired
	private UnregisterDeviceService unregisterDeviceService;

	@Autowired
	private RegisterDeviceService registerDeviceService;

	@Autowired
	private DeviceRegistrationRepository repository;

	private UUID existingDeviceId;

	@BeforeEach
	void setUp() {
		existingDeviceId = registerDeviceService.execute(new RegisterDeviceRequest(
				"Robot-100",
				"11:22:33:44:55:66",
				"Acme Elevators"
		)).deviceId();
	}

	@Test
	@DisplayName("execute should delete existing device")
	void executeDeletesDevice() {
		unregisterDeviceService.execute(new UnregisterDeviceRequest(existingDeviceId));

		assertThat(repository.findById(existingDeviceId)).isEmpty();
	}

	@Test
	@DisplayName("execute should throw when device does not exist")
	void executeMissingDevice() {
		UUID missingId = UUID.randomUUID();

		assertThatThrownBy(() -> unregisterDeviceService.execute(new UnregisterDeviceRequest(missingId)))
				.isInstanceOf(DeviceNotFoundException.class);
	}
}
