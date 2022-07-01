package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CommunityService communityService;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @RequestMapping(value= "/createCommunity", method = RequestMethod.POST)
    public ResponseEntity<CommunityDTO> createCommunity(@RequestBody CommunityDTO communityDTO) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Create community method has been called");

        communityService.createCommunity(communityDTO);

        return new ResponseEntity("Community is successfully created", HttpStatus.CREATED);
    }

    @RequestMapping("/getAllCommunities")
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get all communities method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getAllCommunities());
    }

    @RequestMapping("/id={id}")
    public ResponseEntity<CommunityDTO> getCommunity(@PathVariable Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get community method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunity(id));
    }

    @RequestMapping("/name={name}")
    public ResponseEntity<CommunityDTO> getCommunityByName(@PathVariable String name) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get community by name method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunityByName(name));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommunityDTO> editCommunity(@RequestBody CommunityDTO communityDTO, @PathVariable Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Edit community method has been called");

        return communityService.editCommunity(communityDTO, id);
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @RequestMapping(value = "/suspend/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommunityDTO> deleteCommunity(@RequestBody CommunityDTO communityDTO,
                                                        @PathVariable Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Delete community method has been called");

        return communityService.suspendCommunityById(communityDTO, id, request);

    }
}
