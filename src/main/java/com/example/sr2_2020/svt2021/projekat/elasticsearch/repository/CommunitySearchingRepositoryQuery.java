package com.example.sr2_2020.svt2021.projekat.elasticsearch.repository;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class CommunitySearchingRepositoryQuery {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    
    public CommunitySearchingRepositoryQuery(ElasticsearchOperations elasticsearchOperations,
                                             ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public void update(CommunitySearching communitySearching, String indexName) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(communitySearching.getId())
                .withObject(communitySearching)
                .build();
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of(indexName));
    }

    public ResponseEntity<List<CommunitySearching>> search(String name, String description, Integer minPosts,
                                                           Integer maxPosts, Boolean isMust, Boolean isPdfIndex,
                                                           Float minKarma, Float maxKarma) {
        String indexName;

        if(isPdfIndex)
            indexName="communities-pdf";
        else
            indexName="communities";

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        if (name != null) {
            QueryBuilder nameQuery = QueryBuilders.queryStringQuery("name: " + name);

            if(isMust)
                boolQuery.must(nameQuery);
            else
                boolQuery.should(nameQuery);
        }

        if (description != null) {
            QueryBuilder descriptionQuery = QueryBuilders.queryStringQuery("description: " + description);

            if(isMust)
                boolQuery.must(descriptionQuery);
            else
                boolQuery.should(descriptionQuery);
        }

        if (minPosts != null || maxPosts != null) {
            QueryBuilder postCountQuery;
            if (minPosts == null) {
                postCountQuery = QueryBuilders.rangeQuery("postCount").to(maxPosts);
            } else if (maxPosts == null) {
                postCountQuery = QueryBuilders.rangeQuery("postCount").from(minPosts);
            } else {
                postCountQuery = QueryBuilders.rangeQuery("postCount").from(minPosts).to(maxPosts);
            }
            if(isMust)
                boolQuery.must(postCountQuery);
            else
                boolQuery.should(postCountQuery);
        }

        if (minKarma != null || maxKarma != null) {
            QueryBuilder averageKarmaQuery;
            if (minKarma == null) {
                averageKarmaQuery = QueryBuilders.rangeQuery("averageKarma").to(maxKarma);
            } else if (maxKarma == null) {
                averageKarmaQuery = QueryBuilders.rangeQuery("averageKarma").from(minKarma);
            } else {
                averageKarmaQuery = QueryBuilders.rangeQuery("averageKarma").from(minKarma).to(maxKarma);
            }
            if(isMust)
                boolQuery.must(averageKarmaQuery);
            else
                boolQuery.should(averageKarmaQuery);
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("name").field("description").field("description.highlighted");

        SearchHits<CommunitySearching> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder().withQuery(boolQuery).withHighlightBuilder(highlightBuilder).build(),
                CommunitySearching.class, IndexCoordinates.of(indexName));

        List<CommunitySearching> communities = new ArrayList<>();

        searchHits.forEach(hit -> {
            CommunitySearching community = hit.getContent();
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("name")) {
                community.setHighlighterText(highlightFields.get("name").get(0));
            }
            if (highlightFields.containsKey("description")) {
                List<String> highlightedDescriptions = highlightFields.get("description");
                community.setHighlighterText(highlightedDescriptions.get(0));
            }
            communities.add(community);
        });

        return new ResponseEntity<>(communities, HttpStatus.OK);
    }

    public void indexPdf(String pdfFilePath) throws IOException {

        File pdfFile = new File(pdfFilePath);

        byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());

        PdfReader pdfReader = new PdfReader(pdfContent);

        StringBuilder pdfText = getPdfText(pdfReader);

        Map<String, Object> pdfDocument = new HashMap<>();
        pdfDocument.put("id", UUID.randomUUID().toString());
        pdfDocument.put("name", pdfText.toString().split("\r?\n")[0]);
        pdfDocument.put("description", pdfText.toString());

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(pdfDocument).build();
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("communities-pdf"));

        pdfReader.close();
    }

    public StringBuilder getPdfText(PdfReader pdfReader) throws IOException {
        int pages = pdfReader.getNumberOfPages();

        StringBuilder pdfText = new StringBuilder();

        for (int i = 1; i <= pages; i++) {
            pdfText.append(PdfTextExtractor.getTextFromPage(pdfReader, i));
        }
        return pdfText;
    }
}
