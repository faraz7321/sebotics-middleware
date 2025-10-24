package sebotics.middleware.device.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sebotics.middleware.device.entity.DeviceRegistration;
import sebotics.middleware.device.exception.DeviceNotFoundException;
import sebotics.middleware.device.exception.InvalidDeviceQueryException;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.repository.DeviceRegistrationRepository;

@Service
public class DeviceQueryService {

    private static final Logger log = LoggerFactory.getLogger(DeviceQueryService.class);

    private final DeviceRegistrationRepository repository;

    public DeviceQueryService(DeviceRegistrationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public RegisterDeviceResponse getDevice(String serialNumber, String macAddress) {
        boolean hasSerial = StringUtils.hasText(serialNumber);
        boolean hasMac = StringUtils.hasText(macAddress);

        if (!hasSerial && !hasMac) {
            throw new InvalidDeviceQueryException("Either serialNumber or macAddress must be provided");
        }

        String normalizedSerial = hasSerial ? serialNumber.trim() : null;
        String normalizedMac = hasMac ? normalizeMacAddress(macAddress) : null;

        Optional<DeviceRegistration> result;

        if (hasSerial && hasMac) {
            log.debug("Fetching device by serialNumber={} and macAddress={}", normalizedSerial, normalizedMac);
            result = repository.findBySerialNumberIgnoreCaseAndMacAddressIgnoreCase(normalizedSerial, normalizedMac);
        } else if (hasSerial) {
            log.debug("Fetching device by serialNumber={}", normalizedSerial);
            result = repository.findBySerialNumberIgnoreCase(normalizedSerial);
        } else {
            log.debug("Fetching device by macAddress={}", normalizedMac);
            result = repository.findTopByMacAddressIgnoreCaseOrderByRegisteredAtDesc(normalizedMac);
        }

        DeviceRegistration device = result.orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        return RegisterDeviceResponse.from(device);
    }

    private String normalizeMacAddress(String macAddress) {
        String trimmed = macAddress.trim();
        return trimmed.replace('-', ':').toUpperCase();
    }
}
