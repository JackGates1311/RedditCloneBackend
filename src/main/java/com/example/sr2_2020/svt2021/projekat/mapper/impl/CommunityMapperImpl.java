package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO.CommunityDTOBuilder;
import com.example.sr2_2020.svt2021.projekat.model.Community.CommunityBuilder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class CommunityMapperImpl implements CommunityMapper {

    static final Logger logger = LogManager.getLogger(CommunityController.class);
    @Override
    public CommunityDTO mapCommunityToDTO(Community community) {

        if (community == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - Community object is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new community response ...");

        CommunityDTOBuilder communityDTO = CommunityDTO.builder();

        communityDTO.communityId(community.getCommunityId());
        communityDTO.name(community.getName());
        communityDTO.description(community.getDescription());
        communityDTO.creationDate(LocalDateTime.now().toString());
        communityDTO.isSuspended(community.getIsSuspended());
        communityDTO.suspendedReason(community.getSuspendedReason());

        logger.info("LOGGER: " + LocalDateTime.now() + " - New community has been successfully mapped to DTO");

        return communityDTO.build();
    }

    @Override
    public Community mapDTOToCommunity(CommunityDTO communityDTO) {

        if(communityDTO == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - CommunityDTORequest body is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new community ...");

        CommunityBuilder community = Community.builder();

        community.communityId(communityDTO.getCommunityId());
        community.name(communityDTO.getName());
        community.description(communityDTO.getDescription());
        community.creationDate(LocalDateTime.now());
        community.isSuspended(communityDTO.getIsSuspended());
        community.suspendedReason(communityDTO.getSuspendedReason());

        logger.info("LOGGER: " + LocalDateTime.now() + " - New community has been successfully mapped to object");

        return community.build();
    }
}
