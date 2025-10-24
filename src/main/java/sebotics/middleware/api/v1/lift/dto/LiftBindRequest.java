package sebotics.middleware.api.v1.lift.dto;

import jakarta.validation.constraints.NotBlank;

public record LiftBindRequest(
        @NotBlank(message = "Device id is required")
        String deviceId
) {
}
