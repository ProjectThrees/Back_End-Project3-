package com.studentmarketplace.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @Test
    void getAllReportsReturnsMappedDtos() throws Exception {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        User reportedUser = TestDataFactory.user(UUID.randomUUID(), "reported@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), reporter);
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reportedUser, listing);
        when(reportService.getAllReports()).thenReturn(List.of(report));

        mockMvc.perform(get("/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reason").value("Spam"));
    }

    @Test
    void createReportReturnsMappedDto() throws Exception {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        User reportedUser = TestDataFactory.user(UUID.randomUUID(), "reported@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), reporter);
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reportedUser, listing);
        when(reportService.createReport(any(Report.class))).thenReturn(report);

        mockMvc.perform(post("/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReportBody(reporter.getUserId(), reportedUser.getUserId(), listing.getListingId(), "Spam"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporterId").value(reporter.getUserId().toString()));
    }

    @Test
    void updateReportStatusReturnsMappedDto() throws Exception {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, null, null);
        report.setStatus("RESOLVED");
        when(reportService.updateReportStatus(eq(report.getReportId()), eq("RESOLVED"))).thenReturn(report);

        mockMvc.perform(patch("/report/{reportId}/status", report.getReportId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusBody("RESOLVED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    void deleteReportReturnsNoContent() throws Exception {
        UUID reportId = UUID.randomUUID();
        doNothing().when(reportService).deleteReport(reportId);

        mockMvc.perform(delete("/report/{reportId}", reportId))
                .andExpect(status().isNoContent());
    }

    private record ReportBody(UUID reporterId, UUID reportedUserId, UUID listingId, String reason) {
    }

    private record StatusBody(String status) {
    }
}
