package com.example.sr2_2020.svt2021.projekat.elasticsearch.repository;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class CommunitySearchingRepositoryQuery {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchRestTemplate elasticsearchTemplate;
    
    public CommunitySearchingRepositoryQuery(ElasticsearchOperations elasticsearchOperations,
                                             ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Community findById(String id) {
        return elasticsearchTemplate.get(id, Community.class);
    }

    public void deleteById(String id) {
        elasticsearchTemplate.delete(id, Community.class);
    }

    public void deleteAll() {
        IndexOperations indexOps = elasticsearchTemplate.indexOps(Community.class);

        if (indexOps.exists()) {
            indexOps.delete();
            indexOps.create();
            indexOps.refresh();
        }

    }

    public ResponseEntity<List<CommunitySearching>> search(String query) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(query)).build();

        SearchHits<CommunitySearching> searchHits = elasticsearchOperations
                .search(searchQuery, CommunitySearching.class, IndexCoordinates.of("communities"));

        List<CommunitySearching> communities = new ArrayList<>();
        searchHits.forEach(hit -> communities.add(hit.getContent()));

        return new ResponseEntity<>(communities, HttpStatus.OK);
    }
}
