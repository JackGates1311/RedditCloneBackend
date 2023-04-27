package com.example.sr2_2020.svt2021.projekat.elasticsearch.services;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommunitySearchingService {
    ResponseEntity<List<CommunitySearching>> searchCommunities(String name, String description, Integer minPosts,
                                                               Integer maxPosts, Boolean isMust);
}
