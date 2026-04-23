package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST controller for Report endpoints.
 * Delegates all business logic to ReportService.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable UUID id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getReportsByStatus(@PathVariable String status) {
        try {
            return ResponseEntity.ok(reportService.getReportsByStatus(status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<?> getReportsByReporter(@PathVariable UUID reporterId) {
        try {
            return ResponseEntity.ok(reportService.getReportsByReporter(reporterId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reported-user/{reportedUserId}")
    public ResponseEntity<?> getReportsByReportedUser(@PathVariable UUID reportedUserId) {
        try {
            return ResponseEntity.ok(reportService.getReportsByReportedUser(reportedUserId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<?> getReportsByListing(@PathVariable UUID listingId) {
        try {
            return ResponseEntity.ok(reportService.getReportsByListing(listingId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        try {
            Report created = reportService.createReport(report);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateReportStatus(@PathVariable UUID id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(reportService.updateReportStatus(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID id) {
        try {
            reportService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
