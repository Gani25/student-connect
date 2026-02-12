package com.sprk.student_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "PageRequest",
        description = "Schema to hold information of pagesize, pageno, sorting and filtering"
)
public class StudentPageRequest {

    @Schema(
            description = "Hold page number",
            example = "1"
    )
    private int pageno;
    @Schema(
            description = "Hold page size for pagination",
            example = "10"
    )
    private int pagesize;

    @Schema(
            description = "Hold column name from which it will sort data",
            example = "percentage"
    )
    private String sortcolumn;

    @Schema(
            description = "Hold order through which sorting will be done",
            example = "DESC"
    )
    private String sortorder;

    @Schema(
            description = "Filter object containing search inputs",
            implementation = Filter.class
    )
    private Filter filter;
}
