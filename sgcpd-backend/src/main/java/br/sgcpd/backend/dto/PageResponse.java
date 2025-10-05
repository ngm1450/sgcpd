package br.sgcpd.backend.dto;

import java.util.List;

// DTO genérico para respostas paginadas
public record PageResponse<T>(
    List<T> content,
    int number,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last,
    int numberOfElements,
    String sort
) {
    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> page) {
        var sort = page.getSort().toString(); // "nome: ASC" etc.
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.getNumberOfElements(),
            sort
        );
    }
}
