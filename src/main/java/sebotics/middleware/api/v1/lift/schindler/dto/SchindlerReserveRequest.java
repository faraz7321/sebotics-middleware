package sebotics.middleware.api.v1.lift.schindler.dto;

public record SchindlerReserveRequest(String deviceId, String level, int holdSeconds) {
}
