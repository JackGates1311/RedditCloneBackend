package com.example.sr2_2020.svt2021.projekat.elasticsearch.services.impl;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepositoryQuery;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.CommunitySearchingService;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.PdfService;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CommunitySearchingServiceImpl implements CommunitySearchingService {
    private final CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery;

    private final PdfService pdfService;

    public CommunitySearchingServiceImpl(CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery,
                                         PdfService pdfService) {
        this.communitySearchingRepositoryQuery = communitySearchingRepositoryQuery;
        this.pdfService = pdfService;
    }

    @Override
    public ResponseEntity<List<CommunitySearching>> searchCommunities(
            String name, String description, Integer minPosts, Integer maxPosts, Boolean isMust, Boolean isPdfIndex,
            Float minKarma, Float maxKarma, String nameSearchMode, String descriptionSearchMode) {

        return communitySearchingRepositoryQuery.search(name, description, minPosts, maxPosts, isMust, isPdfIndex,
                minKarma, maxKarma, nameSearchMode, descriptionSearchMode);
    }

    @Override
    public String getPdfText(byte[] pdfContent) {
        try {
            return pdfService.getPdfText(new PdfReader(pdfContent)).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Float calculateCommunityAverageKarma(List<Post> communityPosts) {
        Float sum = 0.0F;

        for(Post postData : communityPosts) sum += postData.getReactionCount();

        if(communityPosts.size() != 0) {
            return sum / (float) communityPosts.size();
        } else {
            return 0.0F;
        }
    }
}
