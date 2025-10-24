package sebotics.middleware.device.unregistration.dto;

import java.util.UUID;

public record UnregisterDeviceRequest(UUID deviceId) {
}
