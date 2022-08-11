package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface CommunityService {

    @Transactional
    CommunityDTO createCommunity(CommunityDTO communityDTO);

    @Transactional
    List<CommunityDTO> getAllCommunities();

    @Transactional
    CommunityDTO getCommunity(Long id);

    @Transactional
    ResponseEntity<CommunityDTO> editCommunity(CommunityDTO communityDTO, Long communityId);

    @Transactional
    ResponseEntity<CommunityDTO> suspendCommunityById(CommunityDTO communityDTO, Long id, HttpServletRequest request);

    @Transactional
    CommunityDTO getCommunityByName(String name);
}
