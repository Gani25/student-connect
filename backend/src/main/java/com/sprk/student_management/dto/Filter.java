package com.sprk.student_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(

        name = "Filter",
        description = "Filter criteria for student search"
)
@Data
public class Filter {

    @Schema(
            description = "Keyword used for searching"
    )
    private String searchText;

    @Schema(
            description = "List of genders to filter",
            example = "[\"Male\", \"Female\"]"
    )
    private List<String> gender;
}
