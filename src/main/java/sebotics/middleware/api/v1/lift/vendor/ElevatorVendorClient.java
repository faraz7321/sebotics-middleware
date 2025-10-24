package sebotics.middleware.api.v1.lift.vendor;

import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.lift.dto.LiftBindRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCancelRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCallRequest;
import sebotics.middleware.api.v1.lift.dto.LiftReserveRequest;
import sebotics.middleware.api.v1.lift.dto.LiftUnbindRequest;

public interface ElevatorVendorClient {

    String vendorKey();

    ApiResponse<Void> bind(LiftBindRequest request);

    ApiResponse<Void> unbind(LiftUnbindRequest request);

    ApiResponse<Void> call(LiftCallRequest request);

    ApiResponse<Void> status(String deviceId);

    ApiResponse<Void> reserve(LiftReserveRequest request);

    ApiResponse<Void> cancel(LiftCancelRequest request);
}
