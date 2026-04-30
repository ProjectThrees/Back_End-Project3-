package com.studentmarketplace.backend.dto;

import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.Report;
import com.studentmarketplace.backend.model.User;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static User toUser(UserCreateRequestDto request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setStatus(request.status());
        return user;
    }

    public static User toUser(UserUpdateRequestDto request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setStatus(request.status());
        return user;
    }

    public static UserResponseDto toUserResponse(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }

    public static Listing toListing(ListingRequestDto request) {
        Listing listing = new Listing();
        if (request.userId() != null) {
            User user = new User();
            user.setUserId(request.userId());
            listing.setUser(user);
        }
        listing.setTitle(request.title());
        listing.setDescription(request.description());
        listing.setPrice(request.price());
        listing.setCategory(request.category());
        listing.setCondition(request.condition());
        listing.setImageUrl(request.imageUrl());
        listing.setIsSold(request.isSold());
        return listing;
    }

    public static ListingResponseDto toListingResponse(Listing listing) {
        return new ListingResponseDto(
                listing.getListingId(),
                listing.getUser() != null ? listing.getUser().getUserId() : null,
                listing.getTitle(),
                listing.getDescription(),
                listing.getPrice(),
                listing.getCategory(),
                listing.getCondition(),
                listing.getImageUrl(),
                listing.getIsSold(),
                listing.getCreatedAt()
        );
    }

    public static Message toMessage(MessageRequestDto request) {
        Message message = new Message();
        if (request.senderId() != null) {
            User sender = new User();
            sender.setUserId(request.senderId());
            message.setSender(sender);
        }
        if (request.receiverId() != null) {
            User receiver = new User();
            receiver.setUserId(request.receiverId());
            message.setReceiver(receiver);
        }
        if (request.listingId() != null) {
            Listing listing = new Listing();
            listing.setListingId(request.listingId());
            message.setListing(listing);
        }
        message.setContent(request.content());
        return message;
    }

    public static MessageResponseDto toMessageResponse(Message message) {
        return new MessageResponseDto(
                message.getMessageId(),
                message.getSender() != null ? message.getSender().getUserId() : null,
                message.getReceiver() != null ? message.getReceiver().getUserId() : null,
                message.getListing() != null ? message.getListing().getListingId() : null,
                message.getContent(),
                message.getSentAt()
        );
    }

    public static FavoriteResponseDto toFavoriteResponse(Favorite favorite) {
        return new FavoriteResponseDto(
                favorite.getFavoriteId(),
                favorite.getUser() != null ? favorite.getUser().getUserId() : null,
                favorite.getListing() != null ? favorite.getListing().getListingId() : null,
                favorite.getCreatedAt()
        );
    }

    public static Report toReport(ReportRequestDto request) {
        Report report = new Report();
        if (request.reporterId() != null) {
            User reporter = new User();
            reporter.setUserId(request.reporterId());
            report.setReporter(reporter);
        }
        if (request.reportedUserId() != null) {
            User reportedUser = new User();
            reportedUser.setUserId(request.reportedUserId());
            report.setReportedUser(reportedUser);
        }
        if (request.listingId() != null) {
            Listing listing = new Listing();
            listing.setListingId(request.listingId());
            report.setListing(listing);
        }
        report.setReason(request.reason());
        return report;
    }

    public static ReportResponseDto toReportResponse(Report report) {
        return new ReportResponseDto(
                report.getReportId(),
                report.getReporter() != null ? report.getReporter().getUserId() : null,
                report.getReportedUser() != null ? report.getReportedUser().getUserId() : null,
                report.getListing() != null ? report.getListing().getListingId() : null,
                report.getReason(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}
