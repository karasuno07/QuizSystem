package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.mapper.TagMapper;
import com.fsoft.quizsystem.object.dto.request.TagRequest;
import com.fsoft.quizsystem.object.dto.response.DifficultyResponse;
import com.fsoft.quizsystem.object.dto.response.TagResponse;
import com.fsoft.quizsystem.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;


    @GetMapping
    ResponseEntity<?> getAllTags() {
        List<TagResponse> responses = tagService.findAllTags().stream()
                                                       .map(tagMapper::entityToTagResponse)
                                                       .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getTagById(@PathVariable Long id) {
        TagResponse response =
                tagMapper.entityToTagResponse(tagService.findTagById(id));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<?> createTag(@RequestBody @Valid TagRequest requestBody) {
        TagResponse response =
                tagMapper.entityToTagResponse(tagService.createTag(requestBody));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateTag(@PathVariable Long id, @RequestBody @Valid TagRequest requestBody) {
        TagResponse response =
                tagMapper.entityToTagResponse(tagService.updateTag(id, requestBody));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> updateTag(@PathVariable Long id) {
        tagService.deleteTag(id);

        return ResponseEntity.ok().build();
    }
}
