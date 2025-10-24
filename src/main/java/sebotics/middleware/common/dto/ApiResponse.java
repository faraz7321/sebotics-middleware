package sebotics.middleware.common.dto;

public record ApiResponse<T>(String code, String message, T data) {

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>("SUCCESS", message, data);
	}

	public static ApiResponse<Void> success(String message) {
		return new ApiResponse<>("SUCCESS", message, null);
	}

	public static <T> ApiResponse<T> failure(String code, String message) {
		return new ApiResponse<>(code, message, null);
	}

	public static <T> ApiResponse<T> failure(String code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}
}
