package br.sgcpd.backend.controller;

import br.sgcpd.backend.entity.Tag;
import br.sgcpd.backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<Tag> findAll(@RequestParam(required = false) Long idCategoria) {
        return tagService.findAll(idCategoria);
    }

}
