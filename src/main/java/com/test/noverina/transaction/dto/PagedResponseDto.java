package com.test.noverina.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponseDto<T> {
    private List<T> content;
    private String debit;
    private String credit;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    @JsonProperty("isLast")
    private boolean isLast;
}
