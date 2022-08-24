package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface CommunityService {

    @Transactional
    ResponseEntity<String> createCommunity(CommunityDTORequest communityDTORequest);

    @Transactional
    List<CommunityDTOResponse> getAllCommunities();

    @Transactional
    CommunityDTOResponse getCommunity(Long id);

    @Transactional
    ResponseEntity<CommunityDTORequest> editCommunity(CommunityDTORequest communityDTORequest, Long communityId);

    @Transactional
    ResponseEntity<CommunityDTORequest> suspendCommunityById(CommunityDTORequest communityDTORequest, Long id, HttpServletRequest request);

    @Transactional
    CommunityDTOResponse getCommunityByName(String name);
}
