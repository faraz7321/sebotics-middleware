package sebotics.middleware.api.v1.lift.schindler.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.lift.dto.LiftBindRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCancelRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCallRequest;
import sebotics.middleware.api.v1.lift.dto.LiftReserveRequest;
import sebotics.middleware.api.v1.lift.dto.LiftUnbindRequest;
import sebotics.middleware.api.v1.lift.vendor.ElevatorVendorClient;

@Service
public class SchindlerLiftService implements ElevatorVendorClient {

    private static final int NOT_IMPLEMENTED = HttpStatus.NOT_IMPLEMENTED.value();
    private static final String VENDOR_KEY = "SCHINDLER";

    @Override
    public String vendorKey() {
        return VENDOR_KEY;
    }

    @Override
    public ApiResponse<Void> bind(LiftBindRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Schindler bind not implemented yet");
    }

    @Override
    public ApiResponse<Void> unbind(LiftUnbindRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Schindler unbind not implemented yet");
    }

    @Override
    public ApiResponse<Void> call(LiftCallRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Schindler call not implemented yet");
    }

    @Override
    public ApiResponse<Void> status(String deviceId) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Schindler status not implemented yet");
    }

    @Override
    public ApiResponse<Void> reserve(LiftReserveRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Schindler reserve not implemented yet");
    }

    @Override
    public ApiResponse<Void> cancel(LiftCancelRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Schindler cancel not implemented yet");
    }
}
