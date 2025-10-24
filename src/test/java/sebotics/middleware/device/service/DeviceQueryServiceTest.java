package sebotics.middleware.device.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.exception.InvalidDeviceQueryException;
import sebotics.middleware.api.device.dto.RegisterDeviceRequest;
import sebotics.middleware.api.device.dto.RegisterDeviceResponse;
import sebotics.middleware.device.registration.service.RegisterDeviceService;

@DataJpaTest
@Import({RegisterDeviceService.class, DeviceQueryService.class})
class DeviceQueryServiceTest {

    @Autowired
    private RegisterDeviceService registerDeviceService;

    @Autowired
    private DeviceQueryService deviceQueryService;

    private RegisterDeviceResponse firstDevice;
    private RegisterDeviceResponse secondDevice;

    @BeforeEach
    void setUp() {
        firstDevice = registerDeviceService.execute(new RegisterDeviceRequest(
                "Robot-300",
                "AA:BB:CC:11:22:33",
                "Acme Elevators"
        ));

        secondDevice = registerDeviceService.execute(new RegisterDeviceRequest(
                "Robot-301",
                "AA:BB:CC:11:22:33",
                "Acme Elevators"
        ));

        registerDeviceService.execute(new RegisterDeviceRequest(
                "Robot-302",
                "DD:EE:FF:11:22:33",
                "Global Lifts"
        ));
    }

    @Test
    @DisplayName("getDevice should return device by serial number")
    void getDeviceBySerial() {
        RegisterDeviceResponse response = deviceQueryService.getDevice("Robot-300", null);

        assertThat(response.deviceId()).isEqualTo(firstDevice.deviceId());
        assertThat(response.macAddress()).isEqualTo("AA:BB:CC:11:22:33");
    }

    @Test
    @DisplayName("getDevice should return most recent device by mac address")
    void getDeviceByMac() {
        RegisterDeviceResponse response = deviceQueryService.getDevice(null, "aa-bb-cc-11-22-33");

        assertThat(response.deviceId()).isEqualTo(secondDevice.deviceId());
        assertThat(response.serialNumber()).isEqualTo("Robot-301");
    }

    @Test
    @DisplayName("getDevice should use both identifiers when provided")
    void getDeviceBySerialAndMac() {
        RegisterDeviceResponse response = deviceQueryService.getDevice("Robot-300", "AA:BB:CC:11:22:33");

        assertThat(response.deviceId()).isEqualTo(firstDevice.deviceId());
    }

    @Test
    @DisplayName("getDevice should throw when no identifier is provided")
    void getDeviceNoIdentifier() {
        assertThatThrownBy(() -> deviceQueryService.getDevice(null, null))
                .isInstanceOf(InvalidDeviceQueryException.class);
    }

    @Test
    @DisplayName("getDevice should throw when device not found")
    void getDeviceNotFound() {
        assertThatThrownBy(() -> deviceQueryService.getDevice("Unknown", null))
                .isInstanceOf(DeviceNotFoundException.class);
    }
}
