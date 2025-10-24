package sebotics.middleware.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(int errcode, String errmsg, T data) {

	public static <T> ApiResponse<T> success(int code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}

	public static ApiResponse<Void> success(int code, String message) {
		return new ApiResponse<>(code, message, null);
	}

	public static <T> ApiResponse<T> success(String message, T data) {
		return success(0, message, data);
	}

	public static ApiResponse<Void> success(String message) {
		return success(0, message);
	}

	public static <T> ApiResponse<T> failure(int code, String message) {
		return new ApiResponse<>(code, message, null);
	}

	public static <T> ApiResponse<T> failure(int code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}
}
