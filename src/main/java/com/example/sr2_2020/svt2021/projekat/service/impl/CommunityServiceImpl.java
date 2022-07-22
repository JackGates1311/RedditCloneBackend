package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Banned;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.repository.BannedRepository;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;

    private final CommunityMapper communityMapper;

    private final TokenUtils tokenUtils;

    private final UserRepository userRepository;

    private final BannedRepository bannedRepository;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public CommunityDTO createCommunity(CommunityDTO communityDTO) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting data for saving community ...");

        communityDTO.setIsSuspended(false);

        Community newCommunity = communityRepository.save(communityMapper.mapDTOToCommunity(communityDTO));

        //TODO Fix code below (low priority for now)

        newCommunity.setCommunityId(newCommunity.getCommunityId());
        newCommunity.setCreationDate(newCommunity.getCreationDate());
        newCommunity.setIsSuspended(false);
        newCommunity.setSuspendedReason("");

        // Maybe you want here to send these four parameters from front-end?!

        logger.info("LOGGER: " + LocalDateTime.now() + " - saving community to database");

        return communityDTO;

    }

    @Override
    public List<CommunityDTO> getAllCommunities() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting all communities in database");

        return communityRepository.findCommunitiesByIsSuspended(false).stream().map(
                communityMapper::mapCommunityToDTO).collect(toList());
    }

    @Override
    public CommunityDTO getCommunity(Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community by ID in database");


        Community community = communityRepository.findByCommunityIdAndIsSuspended(id, false).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered ID"));

        return communityMapper.mapCommunityToDTO(community);
    }

    @Override
    public ResponseEntity<CommunityDTO> editCommunity(CommunityDTO communityDTO, Long communityId) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community data for edit ...");

        Community community = communityMapper.mapDTOToCommunity(communityDTO);

        community.setIsSuspended(false);
        community.setCommunityId(communityId);

        communityRepository.save(community);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(communityDTO);
    }

    @Override
    public ResponseEntity<CommunityDTO> suspendCommunityById(CommunityDTO communityDTO, Long id,
                                                             HttpServletRequest request) {

        //TODO transfer SpringRedditClone exception to CommunityNotFoundException

        logger.info("LOGGER: " + LocalDateTime.now() + " - Finding community data...");

        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditCloneException("" +
                "Community not found with entered ID"));

        community.setIsSuspended(true);
        community.setSuspendedReason(communityDTO.getSuspendedReason());

        communityRepository.save(community);

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        Banned banned = new Banned();

        banned.setTimestamp(LocalDateTime.now());
        banned.setCommunity(community);
        banned.setUser(userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found")));

        bannedRepository.save(banned);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public CommunityDTO getCommunityByName(String name) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community data by name...");

        Community community = communityRepository.findByNameAndIsSuspended(name, false).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered name"));


        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return communityMapper.mapCommunityToDTO(community);
    }

}
