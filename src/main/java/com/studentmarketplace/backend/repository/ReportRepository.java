package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Report entity.
 * Provides CRUD operations and custom query methods via Spring Data JPA.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findByReporter(User reporter);

    List<Report> findByReportedUser(User reportedUser);

    List<Report> findByListing(Listing listing);

    List<Report> findByStatus(String status);

    List<Report> findByReporterAndStatus(User reporter, String status);
}
