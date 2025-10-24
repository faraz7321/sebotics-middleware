package sebotics.middleware.device.unregistration.controller;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import sebotics.middleware.device.unregistration.dto.UnregisterDeviceRequest;
import sebotics.middleware.device.unregistration.service.UnregisterDeviceService;

@WebMvcTest(UnregisterDeviceController.class)
class UnregisterDeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UnregisterDeviceService unregisterDeviceService;

    @TestConfiguration
    static class Config {

        @Bean
        UnregisterDeviceService unregisterDeviceService() {
            return Mockito.mock(UnregisterDeviceService.class);
        }
    }

	@Test
	@DisplayName("DELETE /api/devices/{deviceId}/unregister should delegate to service")
	void unregisterDeletesDevice() throws Exception {
	String deviceId = "device-123";

	doNothing().when(unregisterDeviceService).execute(argThat(request ->
			request.deviceId().equals(deviceId)
	));

	mockMvc.perform(delete("/api/devices/{deviceId}/unregister", deviceId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.errcode").value(200))
		.andExpect(jsonPath("$.errmsg").value("Device successfully deleted"))
		.andExpect(jsonPath("$.data").doesNotExist());

		verify(unregisterDeviceService).execute(argThat(request ->
				request.deviceId().equals(deviceId)
		));
	}
}
