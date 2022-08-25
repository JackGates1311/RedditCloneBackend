package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import java.util.List;

public interface SortService {

    List<Post> hotSortPosts(List<Post> posts);

    List<Comment> hotSortComments(List<Comment> comments);
}
