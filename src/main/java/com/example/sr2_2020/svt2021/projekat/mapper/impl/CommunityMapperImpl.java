package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse.CommunityDTOResponseBuilder;
import com.example.sr2_2020.svt2021.projekat.model.Community.CommunityBuilder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommunityMapperImpl implements CommunityMapper {

    static final Logger logger = LogManager.getLogger(CommunityController.class);
    @Override
    public CommunityDTOResponse mapCommunityToDTO(Community community) {

        if (community == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - Community object is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new community response ...");

        CommunityDTOResponseBuilder communityDTO = CommunityDTOResponse.builder();

        communityDTO.communityId(community.getCommunityId());
        communityDTO.name(community.getName());
        communityDTO.description(community.getDescription());
        communityDTO.creationDate(LocalDateTime.now().toString());
        communityDTO.isSuspended(community.getIsSuspended());
        communityDTO.suspendedReason(community.getSuspendedReason());
        communityDTO.flairs(community.getFlair().stream().
                map(Flair::getName).collect(Collectors.toList()));
        communityDTO.numberOfPosts(community.getPosts().size());

        logger.info("LOGGER: " + LocalDateTime.now() + " - New community has been successfully mapped to DTO");

        return communityDTO.build();
    }

    @Override
    public Community mapDTOToCommunity(CommunityDTORequest communityDTORequest, List<Flair> flairs) {

        if(communityDTORequest == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - CommunityDTORequest body is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new community ...");

        CommunityBuilder community = Community.builder();

        community.communityId(communityDTORequest.getCommunityId());
        community.name(communityDTORequest.getName());
        community.description(communityDTORequest.getDescription());
        community.creationDate(LocalDateTime.now());
        community.isSuspended(communityDTORequest.getIsSuspended());
        community.suspendedReason(communityDTORequest.getSuspendedReason());

        Set<Flair> communityFlairs;

        if(!flairs.isEmpty())
            communityFlairs = new HashSet<>(flairs);
        else
            communityFlairs = null;

        community.flair(communityFlairs);

        logger.info("LOGGER: " + LocalDateTime.now() + " - New community has been successfully mapped to object");

        return community.build();
    }
}
