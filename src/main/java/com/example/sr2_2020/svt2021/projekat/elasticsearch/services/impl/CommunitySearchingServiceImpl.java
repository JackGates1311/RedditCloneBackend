package com.example.sr2_2020.svt2021.projekat.elasticsearch.services.impl;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepositoryQuery;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.CommunitySearchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunitySearchingServiceImpl implements CommunitySearchingService {
    private final CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery;

    public CommunitySearchingServiceImpl(CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery) {
        this.communitySearchingRepositoryQuery = communitySearchingRepositoryQuery;
    }

    @Override
    public ResponseEntity<List<CommunitySearching>> searchCommunities(String query) {
        return communitySearchingRepositoryQuery.search(query);
    }
}
