package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface CommunityService {

    @Transactional
    public CommunityDTO createCommunity(CommunityDTO communityDTO);

    @Transactional
    public List<CommunityDTO> getAllCommunities();

    public CommunityDTO getCommunity(Long id);
}
