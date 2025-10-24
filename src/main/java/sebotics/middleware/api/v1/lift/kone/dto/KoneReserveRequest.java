package sebotics.middleware.api.v1.lift.kone.dto;

public record KoneReserveRequest(String deviceId, String floor, int durationSeconds) {
}
