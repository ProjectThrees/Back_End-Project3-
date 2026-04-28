package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportRepositoryTest {

    @Mock
    private ReportRepository reportRepository;

    @Test
    void findByStatusReporterAndReportedUserReturnMatches() {
        User reporter = TestDataFactory.user(UUID.randomUUID(), "reporter@example.com");
        User reported = TestDataFactory.user(UUID.randomUUID(), "reported@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), reporter);
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reported, listing);
        report.setStatus("REVIEWED");
        when(reportRepository.findByStatus("REVIEWED")).thenReturn(List.of(report));
        when(reportRepository.findByReporter(reporter)).thenReturn(List.of(report));
        when(reportRepository.findByReportedUser(reported)).thenReturn(List.of(report));

        List<Report> byStatus = reportRepository.findByStatus("REVIEWED");
        List<Report> byReporter = reportRepository.findByReporter(reporter);
        List<Report> byReportedUser = reportRepository.findByReportedUser(reported);

        assertEquals(1, byStatus.size());
        assertEquals(report.getReportId(), byReporter.get(0).getReportId());
        assertEquals(report.getReportId(), byReportedUser.get(0).getReportId());

        verify(reportRepository).findByStatus("REVIEWED");
        verify(reportRepository).findByReporter(reporter);
        verify(reportRepository).findByReportedUser(reported);
    }
}
