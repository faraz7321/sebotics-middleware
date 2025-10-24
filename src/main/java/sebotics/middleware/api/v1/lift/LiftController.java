package sebotics.middleware.api.v1.lift;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.lift.dto.LiftBindRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCallRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCancelRequest;
import sebotics.middleware.api.v1.lift.dto.LiftReserveRequest;
import sebotics.middleware.api.v1.lift.dto.LiftUnbindRequest;
import sebotics.middleware.api.v1.lift.service.LiftApiService;

@RestController
@RequestMapping("/api/v1/lift")
public class LiftController {

    private final LiftApiService liftApiService;

    public LiftController(LiftApiService liftApiService) {
        this.liftApiService = liftApiService;
    }

    @PostMapping("/bind")
    public ApiResponse<Void> bind(@RequestBody(required = false) LiftBindRequest request) {
        return liftApiService.bind(request);
    }

    @PostMapping("/unbind")
    public ApiResponse<Void> unbind(@RequestBody(required = false) LiftUnbindRequest request) {
        return liftApiService.unbind(request);
    }

    @PostMapping("/call")
    public ApiResponse<Void> call(@RequestBody(required = false) LiftCallRequest request) {
        return liftApiService.call(request);
    }

    @GetMapping("/status")
    public ApiResponse<Void> status() {
        return liftApiService.status();
    }

    @PostMapping("/reserve")
    public ApiResponse<Void> reserve(@RequestBody(required = false) LiftReserveRequest request) {
        return liftApiService.reserve(request);
    }

    @PostMapping("/cancel")
    public ApiResponse<Void> cancel(@RequestBody(required = false) LiftCancelRequest request) {
        return liftApiService.cancel(request);
    }
}
