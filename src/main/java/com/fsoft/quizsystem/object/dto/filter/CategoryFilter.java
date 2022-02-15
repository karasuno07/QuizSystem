package com.fsoft.quizsystem.object.dto.filter;

import com.fsoft.quizsystem.object.model.Pagination;
import lombok.Data;

@Data
public class CategoryFilter {
    private String name;
    private Pagination pagination = new Pagination(5);
}
