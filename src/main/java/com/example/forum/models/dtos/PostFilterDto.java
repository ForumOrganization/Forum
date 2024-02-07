package com.example.forum.models.dtos;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class PostFilterDto {
    private String title;
    private String createdBy;
    private LocalDate creationTime;
    private String sortBy;
    private String sortOrder;

    public PostFilterDto() {
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
