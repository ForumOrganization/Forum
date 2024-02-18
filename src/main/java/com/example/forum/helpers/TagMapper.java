package com.example.forum.helpers;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.dtos.PostDto;
import com.example.forum.models.dtos.TagDto;
import com.example.forum.models.dtos.TagListDto;
import com.example.forum.services.contracts.TagService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<Tag> fromListDto(TagListDto dtos) {
        List<Tag> tags = new ArrayList<>();
        if (dtos.getNames() == null) {
            return tags;
        }
        for (String name : dtos.getNames()) {
            Tag tag = new Tag();
            tag.setName(name);
            tags.add(tag);
        }
        return tags;
    }


    public TagDto toDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setName(tag.getName());
        return tagDto;
    }
}
