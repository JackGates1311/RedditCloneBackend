package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(community.getPosts()))")
    CommunityDTOResponse mapCommunityToDTO(Community community);

    @InheritInverseConfiguration
    @Mapping(target = "communityPosts", ignore = true)
    Community mapDTOToCommunity (CommunityDTORequest communityDTORequest, List<Flair> flairs);


}
