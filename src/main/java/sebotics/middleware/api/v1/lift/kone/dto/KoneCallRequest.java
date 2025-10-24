package sebotics.middleware.api.v1.lift.kone.dto;

public record KoneCallRequest(String deviceId, String originFloor, String destinationFloor) {
}
