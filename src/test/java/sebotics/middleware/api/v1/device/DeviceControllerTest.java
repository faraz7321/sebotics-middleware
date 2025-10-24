package sebotics.middleware.api.v1.device;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sebotics.middleware.api.v1.common.ApiResponse;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceRequest;
import sebotics.middleware.api.v1.device.dto.RegisterDeviceResponse;
import sebotics.middleware.api.v1.device.service.DeviceApiService;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DeviceApiService deviceApiService;

	@TestConfiguration
	static class Config {

		@Bean
		DeviceApiService deviceApiService() {
			return Mockito.mock(DeviceApiService.class);
		}
	}

	@Test
	@DisplayName("POST /api/v1/device/register returns success envelope")
	void register() throws Exception {
		RegisterDeviceRequest request = new RegisterDeviceRequest(
				"Robot-CLI-001",
				"AA:BB:CC:11:22:33",
				"Acme Elevators"
		);

		RegisterDeviceResponse response = new RegisterDeviceResponse(
				"dev-123",
				request.serialNumber(),
				"AA:BB:CC:11:22:33",
				request.elevatorVendor(),
				OffsetDateTime.parse("2025-10-24T17:30:00Z")
		);

		when(deviceApiService.registerDevice(any(RegisterDeviceRequest.class)))
				.thenReturn(ApiResponse.success(201, "Device registered", response));

		mockMvc.perform(post("/api/v1/device/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errcode").value(201))
				.andExpect(jsonPath("$.errmsg").value("Device registered"))
				.andExpect(jsonPath("$.data.deviceId").value("dev-123"));

		verify(deviceApiService).registerDevice(any(RegisterDeviceRequest.class));
	}

	@Test
	@DisplayName("DELETE /api/v1/device/unregister/{deviceId} returns success envelope")
	void unregister() throws Exception {
		when(deviceApiService.unregisterDevice("dev-123"))
				.thenReturn(ApiResponse.success(200, "Device successfully deleted"));

		mockMvc.perform(delete("/api/v1/device/unregister/{deviceId}", "dev-123"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errcode").value(200))
				.andExpect(jsonPath("$.errmsg").value("Device successfully deleted"))
				.andExpect(jsonPath("$.data").doesNotExist());

		verify(deviceApiService).unregisterDevice("dev-123");
	}

	@Test
	@DisplayName("GET /api/v1/device/info returns device details")
	void getDevice() throws Exception {
		RegisterDeviceResponse response = new RegisterDeviceResponse(
				"dev-123",
				"Robot-CLI-001",
				"AA:BB:CC:11:22:33",
				"Acme Elevators",
				OffsetDateTime.parse("2025-10-24T17:30:00Z")
		);

		when(deviceApiService.getDevice(eq("Robot-CLI-001"), Mockito.isNull()))
				.thenReturn(ApiResponse.success(200, "Device retrieved", response));

		mockMvc.perform(get("/api/v1/device/info")
						.param("serialNumber", "Robot-CLI-001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errcode").value(200))
				.andExpect(jsonPath("$.data.deviceId").value("dev-123"));

		verify(deviceApiService).getDevice("Robot-CLI-001", null);
	}
}
