package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.dto.ReportRequestDto;
import com.studentmarketplace.backend.dto.ReportStatusUpdateDto;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    private ReportController reportController;

    @BeforeEach
    void setUp() {
        reportController = new ReportController(reportService);
    }

    @Test
    void getAllReports_returnsOk() {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        User reportedUser = TestDataFactory.user(UUID.randomUUID(), "reported@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), reporter);
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reportedUser, listing);
        when(reportService.getAllReports()).thenReturn(List.of(report));

        ResponseEntity<?> response = reportController.getAllReports();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getReportById_returnsOk() {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        User reportedUser = TestDataFactory.user(UUID.randomUUID(), "reported@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), reporter);
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reportedUser, listing);
        when(reportService.getReportById(report.getReportId())).thenReturn(Optional.of(report));

        ResponseEntity<?> response = reportController.getReportById(report.getReportId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createReport_returnsOk() {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        User reportedUser = TestDataFactory.user(UUID.randomUUID(), "reported@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), reporter);
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reportedUser, listing);
        when(reportService.createReport(any(Report.class))).thenReturn(report);

        ResponseEntity<?> response = reportController.createReport(
                new ReportRequestDto(reporter.getUserId(), reportedUser.getUserId(), listing.getListingId(), "Spam")
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateReportStatus_returnsOk() {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, null, null);
        report.setStatus("RESOLVED");
        when(reportService.updateReportStatus(eq(report.getReportId()), eq("RESOLVED"))).thenReturn(report);

        ResponseEntity<?> response = reportController.updateReportStatus(report.getReportId(), new ReportStatusUpdateDto("RESOLVED"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteReport_returnsNoContent() {
        UUID reportId = UUID.randomUUID();
        doNothing().when(reportService).deleteReport(reportId);

        ResponseEntity<Void> response = reportController.deleteReportById(reportId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
