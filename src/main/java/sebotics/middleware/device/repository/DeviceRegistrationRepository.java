package sebotics.middleware.device.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import sebotics.middleware.device.entity.DeviceRegistration;

public interface DeviceRegistrationRepository extends JpaRepository<DeviceRegistration, UUID> {

	Optional<DeviceRegistration> findBySerialNumberIgnoreCase(String serialNumber);

	boolean existsBySerialNumberIgnoreCase(String serialNumber);

	boolean existsBySerialNumberIgnoreCaseAndMacAddressIgnoreCase(String serialNumber, String macAddress);

	boolean existsByDeviceIdIgnoreCase(String deviceId);

	Optional<DeviceRegistration> findByDeviceIdIgnoreCase(String deviceId);

	Optional<DeviceRegistration> findBySerialNumberIgnoreCaseAndMacAddressIgnoreCase(String serialNumber, String macAddress);

	Optional<DeviceRegistration> findTopByMacAddressIgnoreCaseOrderByRegisteredAtDesc(String macAddress);
}
