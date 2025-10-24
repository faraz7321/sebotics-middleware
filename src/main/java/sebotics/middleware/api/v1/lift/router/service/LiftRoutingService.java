package sebotics.middleware.api.v1.lift.router.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.lift.dto.LiftBindRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCancelRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCallRequest;
import sebotics.middleware.api.v1.lift.dto.LiftReserveRequest;
import sebotics.middleware.api.v1.lift.dto.LiftUnbindRequest;
import sebotics.middleware.api.v1.lift.vendor.ElevatorVendorClient;
import sebotics.middleware.api.v1.device.domain.entity.DeviceRegistration;
import sebotics.middleware.api.v1.device.domain.repository.DeviceRegistrationRepository;

@Service
public class LiftRoutingService {

    private static final Logger log = LoggerFactory.getLogger(LiftRoutingService.class);

    private static final int NOT_IMPLEMENTED = HttpStatus.NOT_IMPLEMENTED.value();

    private final DeviceRegistrationRepository deviceRegistrationRepository;
    private final Map<String, ElevatorVendorClient> vendorClients;

    public LiftRoutingService(
            DeviceRegistrationRepository deviceRegistrationRepository,
            List<ElevatorVendorClient> vendorClients
    ) {
        this.deviceRegistrationRepository = deviceRegistrationRepository;
        this.vendorClients = vendorClients.stream()
                .collect(Collectors.toUnmodifiableMap(client -> client.vendorKey().toUpperCase(Locale.ROOT), Function.identity()));
    }

    public ApiResponse<Void> bind(LiftBindRequest request) {
        return resolveVendor(request.deviceId())
                .map(client -> client.bind(request))
                .orElseGet(() -> deviceNotFoundResponse(request.deviceId()));
    }

    public ApiResponse<Void> unbind(LiftUnbindRequest request) {
        return resolveVendor(request.deviceId())
                .map(client -> client.unbind(request))
                .orElseGet(() -> deviceNotFoundResponse(request.deviceId()));
    }

    public ApiResponse<Void> call(LiftCallRequest request) {
        return resolveVendor(request.deviceId())
                .map(client -> client.call(request))
                .orElseGet(() -> deviceNotFoundResponse(request.deviceId()));
    }

    public ApiResponse<Void> status(String deviceId) {
        return resolveVendor(deviceId)
                .map(client -> client.status(deviceId))
                .orElseGet(() -> deviceNotFoundResponse(deviceId));
    }

    public ApiResponse<Void> reserve(LiftReserveRequest request) {
        return resolveVendor(request.deviceId())
                .map(client -> client.reserve(request))
                .orElseGet(() -> deviceNotFoundResponse(request.deviceId()));
    }

    public ApiResponse<Void> cancel(LiftCancelRequest request) {
        return resolveVendor(request.deviceId())
                .map(client -> client.cancel(request))
                .orElseGet(() -> deviceNotFoundResponse(request.deviceId()));
    }

    private Optional<ElevatorVendorClient> resolveVendor(String deviceId) {
        return deviceRegistrationRepository.findByDeviceIdIgnoreCase(deviceId)
                .map(DeviceRegistration::getElevatorVendor)
                .map(vendor -> vendorClients.get(vendor.toUpperCase(Locale.ROOT)))
                .filter(Objects::nonNull);
    }

    private ApiResponse<Void> deviceNotFoundResponse(String deviceId) {
        log.warn("No registered device or vendor for deviceId={}", deviceId);
        return ApiResponse.failure(HttpStatus.NOT_FOUND.value(), "Device or elevator vendor not registered");
    }
}
