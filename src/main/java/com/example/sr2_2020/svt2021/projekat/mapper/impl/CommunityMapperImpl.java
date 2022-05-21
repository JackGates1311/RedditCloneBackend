package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO.CommunityDTOBuilder;
import com.example.sr2_2020.svt2021.projekat.model.Community.CommunityBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Component
public class CommunityMapperImpl implements CommunityMapper {

    @Override
    public CommunityDTO mapCommunityToDTO(Community community) {

        if (community == null)
            return null;

        CommunityDTOBuilder communityDTO = CommunityDTO.builder();

        communityDTO.communityId(community.getCommunityId());
        communityDTO.name(community.getName());
        communityDTO.description(community.getDescription());
        communityDTO.creationDate(LocalDateTime.now());
        communityDTO.isSuspended(community.getIsSuspended());
        communityDTO.suspendedReason(community.getSuspendedReason());

        return communityDTO.build();
    }

    @Override
    public Community mapDTOToCommunity(CommunityDTO communityDTO) {

        if(communityDTO == null)
            return null;

        CommunityBuilder community = Community.builder();

        community.communityId(communityDTO.getCommunityId());
        community.name(communityDTO.getName());
        community.description(communityDTO.getDescription());
        community.creationDate(LocalDateTime.now());
        community.isSuspended(communityDTO.getIsSuspended());
        community.suspendedReason(communityDTO.getSuspendedReason());

        return community.build();
    }
}
