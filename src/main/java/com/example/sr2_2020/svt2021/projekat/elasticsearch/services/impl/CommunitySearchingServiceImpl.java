package com.example.sr2_2020.svt2021.projekat.elasticsearch.services.impl;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepositoryQuery;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.CommunitySearchingService;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CommunitySearchingServiceImpl implements CommunitySearchingService {
    private final CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery;

    public CommunitySearchingServiceImpl(CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery) {
        this.communitySearchingRepositoryQuery = communitySearchingRepositoryQuery;
    }

    @Override
    public ResponseEntity<List<CommunitySearching>> searchCommunities(String name, String description, Integer minPosts,
                                                                      Integer maxPosts, Boolean isMust, Boolean isPdfIndex) {
        return communitySearchingRepositoryQuery.search(name, description, minPosts, maxPosts, isMust, isPdfIndex);
    }

    @Override
    public String getPdfText(byte[] pdfContent) {
        try {
            return communitySearchingRepositoryQuery.getPdfText(new PdfReader(pdfContent)).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
