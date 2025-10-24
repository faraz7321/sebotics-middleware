package sebotics.middleware.api.v1.device.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sebotics.middleware.api.v1.device.domain.exception.DeviceNotFoundException;
import sebotics.middleware.api.v1.device.domain.repository.DeviceRegistrationRepository;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceRequest;

@DataJpaTest
@Import({RegisterDeviceService.class, UnregisterDeviceService.class})
class UnregisterDeviceServiceTest {

	@Autowired
	private UnregisterDeviceService unregisterDeviceService;

	@Autowired
	private RegisterDeviceService registerDeviceService;

	@Autowired
	private DeviceRegistrationRepository repository;

	private String existingDeviceId;

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
		unregisterDeviceService.execute(existingDeviceId);

		assertThat(repository.findByDeviceIdIgnoreCase(existingDeviceId)).isEmpty();
	}

	@Test
	@DisplayName("execute should throw when device does not exist")
	void executeMissingDevice() {
		String missingId = "missing-device";

		assertThatThrownBy(() -> unregisterDeviceService.execute(missingId))
				.isInstanceOf(DeviceNotFoundException.class);
	}
}
