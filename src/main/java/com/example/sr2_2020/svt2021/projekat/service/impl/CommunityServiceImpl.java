package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityMapper communityMapper;

    @Override
    public CommunityDTO createCommunity(CommunityDTO communityDTO) {

        Community newCommunity = communityRepository.save(communityMapper.mapDTOToCommunity(communityDTO));

        newCommunity.setCommunityId(newCommunity.getCommunityId());
        newCommunity.setCreationDate(newCommunity.getCreationDate());
        newCommunity.setIsSuspended(false);
        newCommunity.setSuspendedReason("");

        // Maybe you want here to send these four parameters from front-end?!

        return communityDTO;

    }

    @Override
    public List<CommunityDTO> getAllCommunities() {

        return communityRepository.findAll().stream().map(communityMapper::mapCommunityToDTO).collect(toList());
    }

    @Override
    public CommunityDTO getCommunity(Long id) {

        Community community = communityRepository.findById(id).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered ID"));

        return communityMapper.mapCommunityToDTO(community);
    }

    /* private CommunityDTO mapToDTO(Community community) {

        return CommunityDTO.builder().communityId(community.getCommunityId()).name(community.getName())
                .description(community.getDescription()).creationDate(LocalDateTime.now()).
                isSuspended(community.getIsSuspended()).suspendedReason(community.getSuspendedReason()).build();
    }

    private Community mapCommunityDTO(CommunityDTO communityDTO) {

       return Community.builder().communityId(communityDTO.getCommunityId()).name(communityDTO.getName())
                .description(communityDTO.getDescription()).creationDate(communityDTO.getCreationDate()).
               isSuspended(communityDTO.getIsSuspended()).suspendedReason(communityDTO.getSuspendedReason()).build();

    } */
}
