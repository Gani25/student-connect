package com.sprk.student_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(
        name = "Page Response",
        description = "Schema to hold information of get all student with pagination"
)
@Data
public class PageResponse<T> {

    @Schema(
            description = "Hold list of students fetch from database"
    )
    private List<T> content;

    @Schema(
            description = "Hold page number",
            example = "2"
    )
    private int pageNo;

    @Schema(
            description = "Hold page size for pagination",
            example = "10"
    )
    private int pageSize;

    @Schema(
            description = "Hold total students fetch",
            example = "10"
    )
    private long totalElements;

    @Schema(
            description = "Hold total pages with respect to page size",
            example = "3"
    )
    private int totalPages;

    @Schema(
            description = "Hold info if we are on first page or not",
            example = "true"
    )
    private boolean first;

    @Schema(
            description = "Hold info if we are on last page or not",
            example = "true"
    )
    private boolean last;
}
