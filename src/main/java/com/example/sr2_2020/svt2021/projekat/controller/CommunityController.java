package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@AllArgsConstructor
@Slf4j
public class CommunityController {

    @Autowired

    private CommunityService communityService;

    @RequestMapping(value= "/createCommunity", method = RequestMethod.POST)
    public ResponseEntity<CommunityDTO> createCommunity(@RequestBody CommunityDTO communityDTO) {

       return ResponseEntity.status(HttpStatus.CREATED).body(communityService.createCommunity(communityDTO));
    }

    @RequestMapping("/getAllCommunities")
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {

        return ResponseEntity.status(HttpStatus.OK).body(communityService.getAllCommunities());
    }




}
