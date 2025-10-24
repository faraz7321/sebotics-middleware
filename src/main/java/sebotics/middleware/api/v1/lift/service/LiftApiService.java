package sebotics.middleware.api.v1.lift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.lift.dto.LiftBindRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCancelRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCallRequest;
import sebotics.middleware.api.v1.lift.dto.LiftReserveRequest;
import sebotics.middleware.api.v1.lift.dto.LiftUnbindRequest;

@Service
public class LiftApiService {

    private static final int NOT_IMPLEMENTED = HttpStatus.NOT_IMPLEMENTED.value();

    public ApiResponse<Void> bind(LiftBindRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Lift bind not implemented yet");
    }

    public ApiResponse<Void> unbind(LiftUnbindRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Lift unbind not implemented yet");
    }

    public ApiResponse<Void> call(LiftCallRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Lift call not implemented yet");
    }

    public ApiResponse<Void> status() {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Lift status not implemented yet");
    }

    public ApiResponse<Void> reserve(LiftReserveRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Lift reserve not implemented yet");
    }

    public ApiResponse<Void> cancel(LiftCancelRequest request) {
        return ApiResponse.failure(NOT_IMPLEMENTED, "Lift cancel not implemented yet");
    }
}
