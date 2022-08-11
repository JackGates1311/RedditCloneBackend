package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(community.getPosts()))")
    CommunityDTO mapCommunityToDTO(Community community);

    @InheritInverseConfiguration
    @Mapping(target = "communityPosts", ignore = true)
    Community mapDTOToCommunity (CommunityDTO communityDTO);


}
