package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@AllArgsConstructor
@Slf4j
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @RequestMapping(value= "/createCommunity", method = RequestMethod.POST)
    public ResponseEntity<CommunityDTO> createCommunity(@RequestBody CommunityDTO communityDTO) {

        communityService.createCommunity(communityDTO);

        return new ResponseEntity("Community is successfully created", HttpStatus.CREATED);
    }

    @RequestMapping("/getAllCommunities")
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getAllCommunities());
    }

    @RequestMapping("/id={id}")
    public ResponseEntity<CommunityDTO> getCommunity(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunity(id));
    }

    @RequestMapping("/name={name}")
    public ResponseEntity<CommunityDTO> getCommunityByName(@PathVariable String name) {

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunityByName(name));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommunityDTO> editCommunity(@RequestBody CommunityDTO communityDTO, @PathVariable Long id) {

        return communityService.editCommunity(communityDTO, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCommunity(@PathVariable Long id) {

       return communityService.deleteById(id);

    }
}
