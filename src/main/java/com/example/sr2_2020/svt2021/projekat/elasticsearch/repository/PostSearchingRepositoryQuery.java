package com.example.sr2_2020.svt2021.projekat.elasticsearch.repository;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.PostSearching;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

public class PostSearchingRepositoryQuery {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public PostSearchingRepositoryQuery(ElasticsearchOperations elasticsearchOperations,
                                        ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public void update(PostSearching postSearching, String indexName) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(postSearching.getId())
                .withObject(postSearching)
                .build();
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of(indexName));
    }

    public ResponseEntity<List<PostSearching>> search(String title, String text, Integer minKarma, Integer maxKarma,
                                                      Float minComments, Float maxComments, String flairs,
                                                      Boolean isMust, Boolean isPdfIndex, String titleSearchMode,
                                                      String textSearchMode, String flairsSearchMode) {

        String indexName;

        if(isPdfIndex)
            indexName="posts-pdf";
        else
            indexName="posts";

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        if (title != null) {

            QueryBuilder titleQuery;

            if(Objects.equals(titleSearchMode, "fuzzy")) {
                titleQuery = QueryBuilders.matchQuery("title", title).fuzziness(Fuzziness.AUTO);
            } else if (Objects.equals(titleSearchMode, "phrase")) {
                titleQuery = QueryBuilders.matchPhraseQuery("title", title);
            } else {
                titleQuery = QueryBuilders.queryStringQuery("title: " + title);
            }

            if(isMust)
                boolQuery.must(titleQuery);
            else
                boolQuery.should(titleQuery);
        }

        if (text != null) {

            QueryBuilder textQuery;

            if(Objects.equals(textSearchMode, "fuzzy")) {
                textQuery = QueryBuilders.matchQuery("text", text).fuzziness(Fuzziness.AUTO);
            } else if (Objects.equals(textSearchMode, "phrase")) {
                textQuery = QueryBuilders.matchPhraseQuery("text", text);
            } else {
                textQuery = QueryBuilders.queryStringQuery("text: " + text);
            }

            if(isMust)
                boolQuery.must(textQuery);
            else
                boolQuery.should(textQuery);
        }

        if (minKarma != null || maxKarma != null) {

            QueryBuilder karmaQuery;

            if (minKarma == null) {
                karmaQuery = QueryBuilders.rangeQuery("karma").to(maxKarma);
            } else if (maxKarma == null) {
                karmaQuery = QueryBuilders.rangeQuery("karma").from(minKarma);
            } else {
                karmaQuery = QueryBuilders.rangeQuery("karma").from(minKarma).to(maxKarma);
            }

            if(isMust)
                boolQuery.must(karmaQuery);
            else
                boolQuery.should(karmaQuery);
        }

        if (minComments != null || maxComments != null) {

            QueryBuilder commentCountQuery;

            if (minComments == null) {
                commentCountQuery = QueryBuilders.rangeQuery("commentCount").to(maxComments);
            } else if (maxComments == null) {
                commentCountQuery = QueryBuilders.rangeQuery("commentCount").from(minComments);
            } else {
                commentCountQuery = QueryBuilders.rangeQuery("commentCount").from(minComments).to(maxComments);
            }

            if(isMust)
                boolQuery.must(commentCountQuery);
            else
                boolQuery.should(commentCountQuery);
        }

        if (flairs != null) {

            QueryBuilder flairsQuery;

            if(Objects.equals(flairsSearchMode, "fuzzy")) {
                flairsQuery = QueryBuilders.matchQuery("flairs", flairs).fuzziness(Fuzziness.AUTO);
            } else if (Objects.equals(flairsSearchMode, "phrase")) {
                flairsQuery = QueryBuilders.matchPhraseQuery("flairs", flairs);
            } else {
                flairsQuery = QueryBuilders.queryStringQuery("flairs: " + flairs);
            }

            if(isMust)
                boolQuery.must(flairsQuery);
            else
                boolQuery.should(flairsQuery);
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("title").field("text").field("flairs").field("description.highlighted");

        SearchHits<PostSearching> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder().withQuery(boolQuery).withHighlightBuilder(highlightBuilder).build(),
                PostSearching.class, IndexCoordinates.of(indexName));

        List<PostSearching> posts = new ArrayList<>();

        searchHits.forEach(hit -> {
            PostSearching post = hit.getContent();
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("flairs")) {
                List<String> highlightedDescriptions = highlightFields.get("flairs");
                post.setHighlighterText(highlightedDescriptions.get(0));
            }
            if (highlightFields.containsKey("title")) {
                post.setHighlighterText(highlightFields.get("title").get(0));
            }
            if (highlightFields.containsKey("text")) {
                List<String> highlightedDescriptions = highlightFields.get("text");
                post.setHighlighterText(highlightedDescriptions.get(0));
            }
            posts.add(post);
        });

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
