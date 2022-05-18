package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    CommunityRepository communityRepository;

    @Override
    public CommunityDTO createCommunity(CommunityDTO communityDTO) {

        Community newCommunity = communityRepository.save(mapCommunityDTO(communityDTO));

        newCommunity.setCommunityId(newCommunity.getCommunityId());
        newCommunity.setCreationDate(newCommunity.getCreationDate());
        newCommunity.setIsSuspended(false);
        newCommunity.setSuspendedReason("");

        return communityDTO;

    }

    @Override
    public List<CommunityDTO> getAllCommunities() {

        return communityRepository.findAll().stream().map(this::mapToDTO).collect(toList());
    }

    private CommunityDTO mapToDTO(Community community) {

        return CommunityDTO.builder().communityId(community.getCommunityId()).name(community.getName())
                .description(community.getDescription()).creationDate(LocalDateTime.now()).
                isSuspended(community.getIsSuspended()).suspendedReason(community.getSuspendedReason()).build();
    }

    private Community mapCommunityDTO(CommunityDTO communityDTO) {

       return Community.builder().communityId(communityDTO.getCommunityId()).name(communityDTO.getName())
                .description(communityDTO.getDescription()).creationDate(communityDTO.getCreationDate()).
               isSuspended(communityDTO.getIsSuspended()).suspendedReason(communityDTO.getSuspendedReason()).build();


    }
}
