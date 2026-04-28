package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.ReportRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ReportService reportService;

    private UUID reporterId;
    private UUID reportedUserId;
    private UUID listingId;
    private User reporter;
    private User reportedUser;
    private Listing listing;
    private Report report;

    @BeforeEach
    void setUp() {
        reporterId = UUID.randomUUID();
        reportedUserId = UUID.randomUUID();
        listingId = UUID.randomUUID();
        reporter = TestDataFactory.user(reporterId, "reporter@example.com");
        reportedUser = TestDataFactory.user(reportedUserId, "reported@example.com");
        listing = TestDataFactory.listing(listingId, reporter);
        report = TestDataFactory.report(UUID.randomUUID(), reporter, reportedUser, listing);
    }

    @Test
    void createReportRequiresReporter() {
        Report invalid = new Report();

        assertThrows(IllegalArgumentException.class, () -> reportService.createReport(invalid));
    }

    @Test
    void createReportResolvesRelationsAndSetsDefaults() {
        when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
        when(userRepository.findById(reportedUserId)).thenReturn(Optional.of(reportedUser));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Report created = reportService.createReport(report);

        assertEquals("PENDING", created.getStatus());
        assertNotNull(created.getCreatedAt());
        assertEquals(reporterId, created.getReporter().getUserId());
    }

    @Test
    void updateReportStatusRejectsInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> reportService.updateReportStatus(UUID.randomUUID(), "INVALID"));
    }

    @Test
    void updateReportStatusUpdatesExistingReport() {
        UUID reportId = UUID.randomUUID();
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Report updated = reportService.updateReportStatus(reportId, "RESOLVED");

        assertEquals("RESOLVED", updated.getStatus());
    }

    @Test
    void deleteReportThrowsWhenMissing() {
        UUID reportId = UUID.randomUUID();
        when(reportRepository.existsById(reportId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> reportService.deleteReport(reportId));
    }
}
