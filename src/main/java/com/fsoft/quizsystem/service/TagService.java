package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.mapper.TagMapper;
import com.fsoft.quizsystem.object.dto.request.TagRequest;
import com.fsoft.quizsystem.object.entity.Tag;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findTagById(long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any tag with id " + id));
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
