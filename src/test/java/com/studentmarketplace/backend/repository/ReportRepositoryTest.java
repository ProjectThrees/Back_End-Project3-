package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Test
    void findByStatusReporterAndReportedUserReturnMatches() {
        User reporter = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "reporter@example.com"));
        User reported = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "reported@example.com"));
        Listing listing = listingRepository.save(TestDataFactory.listing(UUID.randomUUID(), reporter));
        Report report = TestDataFactory.report(UUID.randomUUID(), reporter, reported, listing);
        report.setStatus("REVIEWED");
        reportRepository.save(report);

        List<Report> byStatus = reportRepository.findByStatus("REVIEWED");
        List<Report> byReporter = reportRepository.findByReporter(reporter);
        List<Report> byReportedUser = reportRepository.findByReportedUser(reported);

        assertEquals(1, byStatus.size());
        assertEquals(report.getReportId(), byReporter.get(0).getReportId());
        assertEquals(report.getReportId(), byReportedUser.get(0).getReportId());
    }
}
