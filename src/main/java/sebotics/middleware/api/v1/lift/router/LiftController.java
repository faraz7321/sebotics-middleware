package sebotics.middleware.api.v1.lift.router;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.lift.dto.LiftBindRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCallRequest;
import sebotics.middleware.api.v1.lift.dto.LiftCancelRequest;
import sebotics.middleware.api.v1.lift.dto.LiftReserveRequest;
import sebotics.middleware.api.v1.lift.dto.LiftUnbindRequest;
import sebotics.middleware.api.v1.lift.router.service.LiftRoutingService;

@RestController
@RequestMapping("/api/v1/lift")
public class LiftController {

    private final LiftRoutingService liftRoutingService;

    public LiftController(LiftRoutingService liftRoutingService) {
        this.liftRoutingService = liftRoutingService;
    }

    @PostMapping("/bind")
    public ApiResponse<Void> bind(@Valid @RequestBody LiftBindRequest request) {
        return liftRoutingService.bind(request);
    }

    @PostMapping("/unbind")
    public ApiResponse<Void> unbind(@Valid @RequestBody LiftUnbindRequest request) {
        return liftRoutingService.unbind(request);
    }

    @PostMapping("/call")
    public ApiResponse<Void> call(@Valid @RequestBody LiftCallRequest request) {
        return liftRoutingService.call(request);
    }

    @GetMapping("/status")
    public ApiResponse<Void> status(@RequestParam String deviceId) {
        return liftRoutingService.status(deviceId);
    }

    @PostMapping("/reserve")
    public ApiResponse<Void> reserve(@Valid @RequestBody LiftReserveRequest request) {
        return liftRoutingService.reserve(request);
    }

    @PostMapping("/cancel")
    public ApiResponse<Void> cancel(@Valid @RequestBody LiftCancelRequest request) {
        return liftRoutingService.cancel(request);
    }
}
