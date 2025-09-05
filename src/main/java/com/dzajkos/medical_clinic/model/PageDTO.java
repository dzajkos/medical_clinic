package com.dzajkos.medical_clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;

    public PageDTO(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public static <A, B> PageDTO<B> from(Page<A> page, Function<A, B> map) {
        List<B> content = page.getContent().stream()
                .map(map)
                .toList();
        return new PageDTO<>(content, page.getTotalPages(), page.getTotalElements());
    }
}
