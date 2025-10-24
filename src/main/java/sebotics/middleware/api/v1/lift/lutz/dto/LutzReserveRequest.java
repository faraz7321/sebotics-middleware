package sebotics.middleware.api.v1.lift.lutz.dto;

public record LutzReserveRequest(String deviceId, String floorId, Integer ttlSeconds) {
}
