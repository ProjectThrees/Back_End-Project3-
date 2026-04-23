package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.ReportRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service class for Report business logic.
 * Acts as an intermediary between ReportController and ReportRepository.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Service
public class ReportService {

    private static final Set<String> VALID_STATUSES = Set.of("PENDING", "REVIEWED", "RESOLVED");

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public ReportService(ReportRepository reportRepository,
                         UserRepository userRepository,
                         ListingRepository listingRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(UUID id) {
        return reportRepository.findById(id);
    }

    public List<Report> getReportsByStatus(String status) {
        validateStatus(status);
        return reportRepository.findByStatus(status);
    }

    public List<Report> getReportsByReporter(UUID reporterId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + reporterId));
        return reportRepository.findByReporter(reporter);
    }

    public List<Report> getReportsByReportedUser(UUID reportedUserId) {
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + reportedUserId));
        return reportRepository.findByReportedUser(reportedUser);
    }

    public List<Report> getReportsByListing(UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + listingId));
        return reportRepository.findByListing(listing);
    }

    public Report createReport(Report report) {
        if (report.getReporter() == null || report.getReporter().getUserId() == null) {
            throw new IllegalArgumentException("Report must have a valid reporter.");
        }

        User reporter = userRepository.findById(report.getReporter().getUserId())
                .orElseThrow(() -> new NoSuchElementException("Reporter not found with id: " + report.getReporter().getUserId()));
        report.setReporter(reporter);

        if (report.getReportedUser() != null && report.getReportedUser().getUserId() != null) {
            User reportedUser = userRepository.findById(report.getReportedUser().getUserId())
                    .orElseThrow(() -> new NoSuchElementException("Reported user not found with id: " + report.getReportedUser().getUserId()));
            report.setReportedUser(reportedUser);
        }

        if (report.getListing() != null && report.getListing().getListingId() != null) {
            Listing listing = listingRepository.findById(report.getListing().getListingId())
                    .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + report.getListing().getListingId()));
            report.setListing(listing);
        }

        report.setStatus("PENDING");
        report.setCreatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public Report updateReportStatus(UUID id, String status) {
        validateStatus(status);
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Report not found with id: " + id));
        report.setStatus(status);
        return reportRepository.save(report);
    }

    public void deleteReport(UUID id) {
        if (!reportRepository.existsById(id)) {
            throw new NoSuchElementException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Must be one of: PENDING, REVIEWED, RESOLVED.");
        }
    }
}
