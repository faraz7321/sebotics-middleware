package sebotics.middleware.api.v1.device.service;

import org.springframework.stereotype.Service;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.device.application.service.RegisterDeviceService;
import sebotics.middleware.api.v1.device.application.service.DeviceQueryService;
import sebotics.middleware.api.v1.device.application.service.UnregisterDeviceService;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceRequest;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceResponse;

/**
 * Service class for handling device-related API operations.
 */
@Service
public class DeviceApiService {

    private final RegisterDeviceService registerDeviceService;
    private final UnregisterDeviceService unregisterDeviceService;
    private final DeviceQueryService deviceQueryService;

    public DeviceApiService(
            RegisterDeviceService registerDeviceService,
            UnregisterDeviceService unregisterDeviceService,
            DeviceQueryService deviceQueryService
    ) {
        this.registerDeviceService = registerDeviceService;
        this.unregisterDeviceService = unregisterDeviceService;
        this.deviceQueryService = deviceQueryService;
    }

    /**
     * Register a new device
     * @param request
     * @return
     */
    public ApiResponse<RegisterDeviceResponse> registerDevice(RegisterDeviceRequest request) {
        RegisterDeviceResponse response = registerDeviceService.execute(request);
        return ApiResponse.success(201, "Device registered", response);
    }

    /**
     * Unregister a device by its ID
     * @param deviceId
     * @return
     */
    public ApiResponse<Void> unregisterDevice(String deviceId) {
        unregisterDeviceService.execute(deviceId);
        return ApiResponse.success(200, "Device successfully deleted");
    }

    /**
     * Get device information by serial number and MAC address
     * @param serialNumber
     * @param macAddress
     * @return
     */
    public ApiResponse<RegisterDeviceResponse> getDevice(String serialNumber, String macAddress) {
        RegisterDeviceResponse response = deviceQueryService.getDevice(serialNumber, macAddress);
        return ApiResponse.success(200, "Device retrieved", response);
    }
}
