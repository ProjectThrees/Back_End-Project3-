package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    //GET /report
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    //GET /report/{reportId}
    @GetMapping("/{reportId}")
    public ResponseEntity<Optional<Report>> getReportById(@PathVariable UUID reportId) {
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    //POST /report
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report created = reportService.createReport(report);
        return ResponseEntity.ok(created);
    }

    //PATCH /report/{reportId}/status
    @PatchMapping("/{reportId}/status")
    public ResponseEntity<Report> updateReportStatus(@PathVariable UUID reportId, @RequestBody String status) {
        return ResponseEntity.ok(reportService.updateReportStatus(reportId,status));
    }


    //DELETE /report/{reportId}
    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReportById(@PathVariable UUID reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
