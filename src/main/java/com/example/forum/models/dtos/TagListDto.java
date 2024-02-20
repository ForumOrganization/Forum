package com.example.forum.models.dtos;

import java.util.ArrayList;
import java.util.List;

public class TagListDto {

    private List<String> names;

    public TagListDto() {
        this.names = new ArrayList<>();
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
