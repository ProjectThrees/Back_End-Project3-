package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.dto.ApiMapper;
import com.studentmarketplace.backend.dto.ReportRequestDto;
import com.studentmarketplace.backend.dto.ReportResponseDto;
import com.studentmarketplace.backend.dto.ReportStatusUpdateDto;
import com.studentmarketplace.backend.exception.NotFoundException;
import com.studentmarketplace.backend.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<ReportResponseDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports().stream().map(ApiMapper::toReportResponse).toList());
    }

    //GET /report/{reportId}
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponseDto> getReportById(@PathVariable UUID reportId) {
        return ResponseEntity.ok(
                reportService.getReportById(reportId)
                        .map(ApiMapper::toReportResponse)
                        .orElseThrow(() -> new NotFoundException("Report not found with id: " + reportId))
        );
    }

    //POST /report
    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto reportRequest) {
        return ResponseEntity.ok(ApiMapper.toReportResponse(reportService.createReport(ApiMapper.toReport(reportRequest))));
    }

    //PATCH /report/{reportId}/status
    @PatchMapping("/{reportId}/status")
    public ResponseEntity<ReportResponseDto> updateReportStatus(@PathVariable UUID reportId, @RequestBody ReportStatusUpdateDto request) {
        return ResponseEntity.ok(ApiMapper.toReportResponse(reportService.updateReportStatus(reportId, request.status())));
    }


    //DELETE /report/{reportId}
    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReportById(@PathVariable UUID reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
