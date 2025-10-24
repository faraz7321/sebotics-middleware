package sebotics.middleware.device.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.device.registration.dto.RegisterDeviceRequest;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;

@DataJpaTest
@Import(RegisterDeviceService.class)
class RegisterDeviceServiceTest {

	@Autowired
	private RegisterDeviceService service;

	@Autowired
	private DeviceRegistrationRepository repository;

	@Test
	@DisplayName("execute should persist new device and normalize identifiers")
	void executePersistsDevice() {
		RegisterDeviceRequest request = new RegisterDeviceRequest(
				"Robot-001",
				"aa-bb-cc-11-22-33",
				"Acme Elevators"
		);

		RegisterDeviceResponse response = service.execute(request);

		assertThat(response.id()).isNotNull();
		assertThat(response.serialNumber()).isEqualTo("Robot-001");
		assertThat(response.macAddress()).isEqualTo("AA:BB:CC:11:22:33");

		assertThat(repository.findById(response.id()))
				.map(DeviceRegistration::getMacAddress)
				.contains("AA:BB:CC:11:22:33");
	}

	@Test
	@DisplayName("execute should reject duplicates by serial number or MAC address")
	void executeRejectsDuplicates() {
		service.execute(new RegisterDeviceRequest(
				"Robot-001",
				"AA:BB:CC:11:22:33",
				"Acme Elevators"
		));

		RegisterDeviceRequest duplicateSerial = new RegisterDeviceRequest(
				"robot-001",
				"FF:EE:DD:44:55:66",
				"Acme Elevators"
		);

		assertThatThrownBy(() -> service.execute(duplicateSerial))
				.isInstanceOf(DeviceAlreadyRegisteredException.class);

		RegisterDeviceRequest duplicateMac = new RegisterDeviceRequest(
				"Robot-002",
				"aa:bb:cc:11:22:33",
				"Acme Elevators"
		);

		assertThatThrownBy(() -> service.execute(duplicateMac))
				.isInstanceOf(DeviceAlreadyRegisteredException.class);
	}
}
