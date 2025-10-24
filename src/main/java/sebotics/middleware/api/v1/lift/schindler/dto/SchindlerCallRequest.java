package sebotics.middleware.api.v1.lift.schindler.dto;

public record SchindlerCallRequest(String deviceId, String pickupLevel, String dropoffLevel) {
}
