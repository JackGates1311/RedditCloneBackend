package com.example.sr2_2020.svt2021.projekat.elasticsearch.repository;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunitySearchingRepository extends ElasticsearchRepository<CommunitySearching, String> {

}
