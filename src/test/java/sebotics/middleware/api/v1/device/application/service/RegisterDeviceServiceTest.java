package sebotics.middleware.api.v1.device.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sebotics.middleware.api.v1.device.domain.entity.DeviceRegistration;
import sebotics.middleware.api.v1.device.domain.exception.DeviceAlreadyRegisteredException;
import sebotics.middleware.api.v1.device.domain.repository.DeviceRegistrationRepository;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceRequest;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceResponse;

@DataJpaTest
@Import(RegisterDeviceService.class)
class RegisterDeviceServiceTest {

	@Autowired
	private RegisterDeviceService service;

	@Autowired
	private DeviceRegistrationRepository repository;

	@Test
	@DisplayName("execute should persist new device, generate device id, and normalize identifiers")
	void executePersistsDevice() {
		RegisterDeviceRequest request = new RegisterDeviceRequest(
				"Robot-001",
				"aa-bb-cc-11-22-33",
				"Acme Elevators"
		);

		RegisterDeviceResponse response = service.execute(request);

		assertThat(response.deviceId()).isNotBlank();
		assertThat(response.serialNumber()).isEqualTo("Robot-001");
		assertThat(response.macAddress()).isEqualTo("AA:BB:CC:11:22:33");

		assertThat(repository.findByDeviceIdIgnoreCase(response.deviceId()))
				.map(DeviceRegistration::getMacAddress)
				.contains("AA:BB:CC:11:22:33");
	}

	@Test
	@DisplayName("execute should reject duplicates by serial number and MAC address combination")
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

		RegisterDeviceResponse secondDevice = service.execute(duplicateMac);
		assertThat(secondDevice.deviceId()).isNotBlank();
	}

	@Test
	@DisplayName("execute should reject duplicate pairing of serial and MAC even with case differences")
	void executeRejectsDuplicateCombinationCaseInsensitive() {
		service.execute(new RegisterDeviceRequest(
				"Robot-200",
				"AA:BB:CC:11:22:33",
				"Acme Elevators"
		));

		RegisterDeviceRequest duplicateCombination = new RegisterDeviceRequest(
				"robot-200",
				"aa:bb:cc:11:22:33",
				"Acme Elevators"
		);

		assertThatThrownBy(() -> service.execute(duplicateCombination))
				.isInstanceOf(DeviceAlreadyRegisteredException.class);
	}
}
