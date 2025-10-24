package sebotics.middleware.device.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import sebotics.middleware.device.registration.dto.RegisterDeviceResponse;
import sebotics.middleware.device.service.DeviceQueryService;

@WebMvcTest(DeviceQueryController.class)
class DeviceQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceQueryService deviceQueryService;

    @TestConfiguration
    static class Config {

        @Bean
        DeviceQueryService deviceQueryService() {
            return Mockito.mock(DeviceQueryService.class);
        }
    }

    @Test
    @DisplayName("GET /api/devices/info should return device details")
    void getDeviceInfo() throws Exception {
        RegisterDeviceResponse response = new RegisterDeviceResponse(
                "dev-123",
                "Robot-400",
                "AA:BB:CC:11:22:33",
                "Acme Elevators",
                OffsetDateTime.parse("2025-10-24T18:00:00Z")
        );

        when(deviceQueryService.getDevice(Mockito.eq("Robot-400"), Mockito.isNull())).thenReturn(response);

        mockMvc.perform(get("/api/devices/info")
                        .param("serialNumber", "Robot-400"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("dev-123"))
                .andExpect(jsonPath("$.serialNumber").value("Robot-400"))
                .andExpect(jsonPath("$.macAddress").value("AA:BB:CC:11:22:33"));

        verify(deviceQueryService).getDevice("Robot-400", null);
    }
}
