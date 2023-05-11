package com.example.sr2_2020.svt2021.projekat.elasticsearch.services;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommunitySearchingService {
    ResponseEntity<List<CommunitySearching>> searchCommunities(String name, String description, Integer minPosts,
                                                               Integer maxPosts, Boolean isMust, Boolean isPdfIndex,
                                                               Float minKarma, Float maxKarma, String nameSearchMode,
                                                               String descriptionSearchMode);
    String getPdfText(byte[] pdfContent);

    Float calculateCommunityAverageKarma(List<Post> communityPosts);
}
