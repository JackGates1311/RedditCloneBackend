package com.example.sr2_2020.svt2021.projekat.elasticsearch.model;

import com.example.sr2_2020.svt2021.projekat.model.Flair;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "posts")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class PostSearching {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String text;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
    @Field(type = FieldType.Integer)
    private Integer karma;
    @Field(type = FieldType.Text)
    private String highlighterText;
    @Field(type = FieldType.Nested)
    private Set<Flair> flair = new HashSet<>();
}
