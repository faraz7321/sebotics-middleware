package sebotics.middleware.api.v1.device;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceRequest;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceResponse;
import sebotics.middleware.api.v1.device.service.DeviceApiService;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceApiService deviceApiService;

    public DeviceController(DeviceApiService deviceApiService) {
        this.deviceApiService = deviceApiService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterDeviceResponse> register(@Valid @RequestBody RegisterDeviceRequest request) {
        log.debug("Received register request serialNumber={} elevatorVendor={}",
                request.serialNumber(),
                request.elevatorVendor());
        return deviceApiService.registerDevice(request);
    }

    @DeleteMapping("/unregister/{deviceId}")
    public ApiResponse<Void> unregister(@PathVariable String deviceId) {
        log.debug("Received unregister request deviceId={}", deviceId);
        return deviceApiService.unregisterDevice(deviceId);
    }

    @GetMapping("/info")
    public ApiResponse<RegisterDeviceResponse> getDevice(
            @RequestParam(value = "serialNumber", required = false) String serialNumber,
            @RequestParam(value = "macAddress", required = false) String macAddress
    ) {
        log.debug("Received device lookup request serialNumber={} macAddress={}", serialNumber, macAddress);
        return deviceApiService.getDevice(serialNumber, macAddress);
    }
}
