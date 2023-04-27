package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.CommunitySearchingService;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/communities")
@AllArgsConstructor
@Slf4j
public class CommunityController {
    private final CommunityService communityService;

    private final CommunitySearchingService communitySearchingService;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @RequestMapping(value= "/createCommunity", method = RequestMethod.POST)
    public ResponseEntity<String> createCommunity(@RequestBody CommunityDTORequest communityDTORequest) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Create community method has been called");

        return communityService.createCommunity(communityDTORequest);
    }

    @RequestMapping("/getAllCommunities")
    public ResponseEntity<List<CommunityDTOResponse>> getAllCommunities() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get all communities method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getAllCommunities());
    }

    @RequestMapping("/id={id}")
    public ResponseEntity<CommunityDTOResponse> getCommunity(@PathVariable Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get community method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunity(id));
    }

    @RequestMapping("/name={name}")
    public ResponseEntity<CommunityDTOResponse> getCommunityByName(@PathVariable String name) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get community by name method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunityByName(name));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommunityDTORequest> editCommunity(@RequestBody CommunityDTORequest communityDTORequest, @PathVariable Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Edit community method has been called");

        return communityService.editCommunity(communityDTORequest, id);
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/suspend/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommunityDTORequest> deleteCommunity(@RequestBody CommunityDTORequest communityDTORequest,
                                                                @PathVariable Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Delete community method has been called");

        return communityService.suspendCommunityById(communityDTORequest, id, request);

    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<CommunitySearching>> searchCommunities(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "min", required = false) Integer minPosts,
            @RequestParam(value = "max", required = false) Integer maxPosts,
            @RequestParam(value = "isMust", required = false) Boolean isMust
    ) {

        if(isMust == null)
            isMust = true;

        return communitySearchingService.searchCommunities(name, description, minPosts, maxPosts, isMust);
    }
}
