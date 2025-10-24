package sebotics.middleware.device.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import sebotics.middleware.device.entity.DeviceRegistration;

public interface DeviceRegistrationRepository extends JpaRepository<DeviceRegistration, UUID> {

	Optional<DeviceRegistration> findBySerialNumberIgnoreCase(String serialNumber);

	Optional<DeviceRegistration> findByMacAddressIgnoreCase(String macAddress);

	boolean existsBySerialNumberIgnoreCaseOrMacAddressIgnoreCase(String serialNumber, String macAddress);
}
