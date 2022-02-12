package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.mapper.TagMapper;
import com.fsoft.quizsystem.object.dto.request.TagRequest;
import com.fsoft.quizsystem.object.entity.Tag;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @PostConstruct
    private void init() {
        if (tagRepository.count() == 0) {
            List<Tag> initialTags = Arrays.asList(
                new Tag("HTML5/CSS3"),
                new Tag("Javascript"),
                new Tag("Java"),
                new Tag("Python"),
                new Tag("DevOps"),
                new Tag("Kernel/OS")
            );

            tagRepository.saveAll(initialTags);
        }
    }

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findTagById(long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any tag with id " + id));
    }

    public Tag findTagByName(String name) {
        return tagRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Not found any tag with name " + name));
    }

    public Tag createTag(TagRequest requestBody) {
        Tag tag = tagMapper.tagReqeustToEntity(requestBody);

        return tagRepository.save(tag);
    }

    public Tag updateTag(long id, TagRequest requestBody) {
        Tag tag = this.findTagById(id);
        tagMapper.updateEntity(tag, requestBody);

        return tagRepository.save(tag);
    }

    public void deleteTag(long id) {
        Tag tag = this.findTagById(id);

        tagRepository.delete(tag);
    }
}
