package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

public interface CommunityService {

    @Transactional
    public CommunityDTO createCommunity(CommunityDTO communityDTO);

    @Transactional
    public List<CommunityDTO> getAllCommunities();

    @Transactional
    public CommunityDTO getCommunity(Long id);

    @Transactional
    public ResponseEntity<CommunityDTO> editCommunity(CommunityDTO communityDTO, Long communityId);

    @Transactional
    public ResponseEntity<?> deleteById(Long id);

    @Transactional
    public CommunityDTO getCommunityByName(String name);
}
