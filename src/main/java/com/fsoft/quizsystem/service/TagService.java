package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.mapper.TagMapper;
import com.fsoft.quizsystem.object.dto.request.TagRequest;
import com.fsoft.quizsystem.object.entity.es.TagES;
import com.fsoft.quizsystem.object.entity.jpa.Tag;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.es.TagEsRepository;
import com.fsoft.quizsystem.repository.jpa.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Log4j2
public class TagService {

    private final TagRepository tagRepository;
    private final TagEsRepository tagEsRepository;
    private final TagMapper tagMapper;

    public List<Tag> findAllTags() {
        List<Tag> tags;

        if (tagEsRepository.count() > 0) {
            tags = StreamSupport.stream(tagEsRepository.findAll().spliterator(), false)
                                .map(tagMapper::esEntityToJpa)
                                .collect(Collectors.toList());
        } else {
            tags = tagRepository.findAll();
        }


        return tags;
    }

    public Tag findTagById(long id) {
        Optional<TagES> optional = tagEsRepository.findById(id);

        if (optional.isPresent()) {
            return tagMapper.esEntityToJpa(optional.get());
        } else {
            return tagRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any tag with id " + id));
        }
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
