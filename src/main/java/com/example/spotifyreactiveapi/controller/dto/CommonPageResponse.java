package com.example.spotifyreactiveapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageResponse<T> {

    /**
     * 현재 페이지의 데이터 리스트
     */
    private Collection<T> content;

    /**
     * 현재 페이지 번호 (1부터 시작)
     */
    private int page;

    /**
     * 페이지당 항목 수
     */
    private int size;

    /**
     * 전체 항목 수
     */
    private long totalElements;

    /**
     * 전체 페이지 수
     */
    private int totalPages;

    /**
     * 첫 페이지 여부
     */
    private boolean isFirstPage;

    /**
     * 마지막 페이지 여부
     */
    private boolean isLastPage;

    public CommonPageResponse(Collection<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.isFirstPage = page == 1;
        this.isLastPage = page >= totalPages - 1;
    }

    public CommonPageResponse(Page<T> page) {
        this(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
