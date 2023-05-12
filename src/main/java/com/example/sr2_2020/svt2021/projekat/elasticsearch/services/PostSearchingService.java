package com.example.sr2_2020.svt2021.projekat.elasticsearch.services;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.PostSearching;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostSearchingService {

    ResponseEntity<List<PostSearching>> searchPosts(String title, String text, Integer minKarma, Integer maxKarma,
                                                    Float minComments, Float maxComments, String flairs,
                                                    Boolean isMust, Boolean isPdfIndex, String titleSearchMode,
                                                    String textSearchMode, String flairsSearchMode);

    String getPdfText(byte[] pdfContent);

    Integer getCommentsCount(Post post);
}
