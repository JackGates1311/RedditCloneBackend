package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepository;
import com.example.sr2_2020.svt2021.projekat.exception.CommunityNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Banned;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import com.example.sr2_2020.svt2021.projekat.repository.*;
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
import java.util.*;
import java.util.stream.Collectors;

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

    private final FlairRepository flairRepository;

    private final PostRepository postRepository;

    private final CommunitySearchingRepository communitySearchingRepository;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public ResponseEntity<String> createCommunity(CommunityDTORequest communityDTORequest) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting data for saving community ...");

        communityDTORequest.setIsSuspended(false);

        // TODO while creating new community, community moderator can add (before sending create comunity request)
        //  flairs (with add flair request, after we can select newly added flair) from or use already added flairs...

        List<Flair> flairs = new ArrayList<>();

        Community newCommunity;

        if(Objects.isNull(communityDTORequest.getFlairs())) {

            newCommunity = communityRepository.save(communityMapper.mapDTOToCommunity(communityDTORequest, flairs));

        } else {

            flairs = communityDTORequest.getFlairs().stream().map(
                    flairName -> flairRepository.findByName(flairName).orElseThrow(() ->
                    new SpringRedditCloneException("Flair not found with name: " + flairName))).collect(toList());

            newCommunity = communityRepository.save(communityMapper.mapDTOToCommunity(communityDTORequest, flairs));

            flairs.forEach(flair -> flair.getCommunities().add(newCommunity));
        }

        //

        try {
            communitySearchingRepository.save(new CommunitySearching(newCommunity.getCommunityId().toString(),
                    newCommunity.getName(), newCommunity.getDescription(), 0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.toString());
        }


        //

        logger.info("LOGGER: " + LocalDateTime.now() + " - saving community to database");

        return ResponseEntity.status(HttpStatus.CREATED).body("Community is successfully created");

    }

    @Override
    public List<CommunityDTOResponse> getAllCommunities() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting all communities in database");

        return communityRepository.findCommunitiesByIsSuspended(false).stream().map(
                communityMapper::mapCommunityToDTO).collect(toList());
    }

    @Override
    public CommunityDTOResponse getCommunity(Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community by ID in database");


        Community community = communityRepository.findByCommunityIdAndIsSuspended(id, false).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered ID"));

        return communityMapper.mapCommunityToDTO(community);
    }

    @Override
    public ResponseEntity<CommunityDTORequest> editCommunity(CommunityDTORequest communityDTORequest, Long communityId) {

        communityDTORequest.setFlairs(communityDTORequest.getFlairs().stream().distinct().collect(toList()));

        //TODO Implement rebasing community flairs (this will be done on front side)

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community data for edit ...");

        List<Flair> flairs = new ArrayList<>();

        if(Objects.isNull(communityDTORequest.getFlairs())) {

            communityRepository.save(communityMapper.mapDTOToCommunity(communityDTORequest, flairs));

        } else {

            flairs = communityDTORequest.getFlairs().stream().map(flairName -> flairRepository.findByName(flairName).
                        orElseThrow(() -> new SpringRedditCloneException("Flair not found with name: " + flairName))).
                        collect(toList());

            Community community = communityMapper.mapDTOToCommunity(communityDTORequest, flairs);

            List<String> oldFlairs = communityRepository.findById(communityId).orElseThrow(() -> new
                    CommunityNotFoundException("Community not with id: " + communityId)).getFlair().stream().
                    map(Flair::getName).collect(toList());

            List<String> newFlairs = communityDTORequest.getFlairs();


            List<String> deletedFlairs = oldFlairs.stream().filter(
                    v -> !newFlairs.contains(v)).collect(Collectors.toList());

            List<String> flairsUsedInPosts = flairRepository.findFlairsUsedInPostsByCommunityId(communityId);

            System.out.println("OLD FLAIRS: " + oldFlairs);
            System.out.println("NEW FLAIRS: " + newFlairs);
            System.out.println("DELETED FLAIRS: " + deletedFlairs);
            System.out.println("USED FLAIRS IN POSTS: " + Arrays.toString(flairsUsedInPosts.toArray()));

            for(String flairToDelete: deletedFlairs) {
                for(String usedFlair : flairsUsedInPosts){
                    if(flairToDelete.equals(usedFlair)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                    }
                }
            }

            community.setIsSuspended(false);
            community.setCommunityId(communityId);

            // todo does it line is really needed here?!
            postRepository.findPostsByCommunity(community).forEach(post -> post.setCommunity(community));

            communityRepository.save(community);

            flairs.forEach(flair -> flair.getCommunities().add(community));
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(communityDTORequest);
    }

    @Override
    public ResponseEntity<CommunityDTORequest> suspendCommunityById(CommunityDTORequest communityDTORequest, Long id,
                                                                     HttpServletRequest request) {

        //TODO transfer SpringRedditClone exception to CommunityNotFoundException

        logger.info("LOGGER: " + LocalDateTime.now() + " - Finding community data...");

        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditCloneException("" +
                "Community not found with entered ID"));

        community.setIsSuspended(true);
        community.setSuspendedReason(communityDTORequest.getSuspendedReason());
        community.setFlair(null);

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
    public CommunityDTOResponse getCommunityByName(String name) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community data by name...");

        Community community = communityRepository.findByNameAndIsSuspended(name, false).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered name"));


        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return communityMapper.mapCommunityToDTO(community);
    }

}
