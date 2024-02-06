package com.example.forum.helpers;

import com.example.forum.models.Tag;
import com.example.forum.models.dtos.TagDto;
import com.example.forum.services.contracts.TagService;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    private final TagService tagService;

    public TagMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public Tag fromDto(int id, TagDto dto) {
        Tag tag = fromDto(dto);
        tag.setId(id);
        return tag;
    }

    public Tag fromDto(TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        return tag;
    }
}
