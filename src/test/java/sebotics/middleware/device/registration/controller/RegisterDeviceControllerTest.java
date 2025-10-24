package sebotics.middleware.device.registration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import sebotics.middleware.device.registration.dto.RegisterDeviceRequest;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.registration.service.RegisterDeviceService;

@WebMvcTest(RegisterDeviceController.class)
class RegisterDeviceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RegisterDeviceService registerDeviceService;

	@TestConfiguration
	static class Config {

		@Bean
		RegisterDeviceService registerDeviceService() {
			return Mockito.mock(RegisterDeviceService.class);
		}
	}

	@Test
	@DisplayName("POST /api/devices/register should create device and return payload")
	void registerCreatesDevice() throws Exception {
		RegisterDeviceRequest request = new RegisterDeviceRequest(
				"Robot-CLI-001",
				"AA:BB:CC:11:22:33",
				"Acme Elevators"
		);

	RegisterDeviceResponse response = new RegisterDeviceResponse(
			"dev-123",
			request.serialNumber(),
			request.macAddress(),
			request.elevatorVendor(),
			OffsetDateTime.parse("2025-10-24T17:30:00Z")
	);

		when(registerDeviceService.execute(any(RegisterDeviceRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/devices/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errcode").value(201))
				.andExpect(jsonPath("$.errmsg").value("Device registered"))
				.andExpect(jsonPath("$.data.deviceId").value(response.deviceId()))
				.andExpect(jsonPath("$.data.serialNumber").value(request.serialNumber()))
				.andExpect(jsonPath("$.data.macAddress").value(request.macAddress()))
				.andExpect(jsonPath("$.data.elevatorVendor").value(request.elevatorVendor()));

		verify(registerDeviceService).execute(any(RegisterDeviceRequest.class));
	}
}
