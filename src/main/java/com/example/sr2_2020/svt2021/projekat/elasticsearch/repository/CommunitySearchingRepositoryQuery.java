package com.example.sr2_2020.svt2021.projekat.elasticsearch.repository;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

public class CommunitySearchingRepositoryQuery {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    
    public CommunitySearchingRepositoryQuery(ElasticsearchOperations elasticsearchOperations,
                                             ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public void update(CommunitySearching communitySearching) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(communitySearching.getId())
                .withObject(communitySearching)
                .build();

        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("communities"));
    }

    public ResponseEntity<List<CommunitySearching>> search(String name, String description, Integer minPosts,
                                                                   Integer maxPosts, Boolean isMust) {

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

        SearchHits<CommunitySearching> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder().withQuery(boolQuery).build(),
                CommunitySearching.class, IndexCoordinates.of("communities"));

        List<CommunitySearching> communities = new ArrayList<>();
        searchHits.forEach(hit -> communities.add(hit.getContent()));

        return new ResponseEntity<>(communities, HttpStatus.OK);
    }
}
