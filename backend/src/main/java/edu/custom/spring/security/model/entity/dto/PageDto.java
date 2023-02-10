package edu.custom.spring.security.model.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PageDto<T> {

    private List<T> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
}
