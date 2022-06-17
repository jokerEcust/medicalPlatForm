package com.ecust.search.Impl;

import com.ecust.search.Search;

import java.util.List;

public class FileSearch implements Search {
    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("文件搜索 "+keyword);
        return null;
    }
}

