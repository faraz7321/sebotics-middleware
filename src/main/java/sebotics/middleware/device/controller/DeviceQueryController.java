package sebotics.middleware.device.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.service.DeviceQueryService;

@RestController
@RequestMapping("/api/devices")
public class DeviceQueryController {

    private static final Logger log = LoggerFactory.getLogger(DeviceQueryController.class);

    private final DeviceQueryService deviceQueryService;

    public DeviceQueryController(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping("/info")
    public RegisterDeviceResponse getDevice(
            @RequestParam(value = "serialNumber", required = false) String serialNumber,
            @RequestParam(value = "macAddress", required = false) String macAddress
    ) {
        log.debug("Received device lookup request serialNumber={} macAddress={}", serialNumber, macAddress);
        return deviceQueryService.getDevice(serialNumber, macAddress);
    }
}
