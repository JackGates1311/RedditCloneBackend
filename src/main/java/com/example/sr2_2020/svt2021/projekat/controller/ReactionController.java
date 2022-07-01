package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.model.ReactionType;
import com.example.sr2_2020.svt2021.projekat.service.ReactionService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/reactions")
@AllArgsConstructor
public class ReactionController {

    @Autowired
    ReactionService reactionService;

    static final Logger logger = LogManager.getLogger(CommunityController.class);
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity reaction(@RequestBody ReactionDTO reactionDTO, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Send reaction method has been called");

        reactionService.reaction(reactionDTO, request);

        return new ResponseEntity("Your reaction has been saved", HttpStatus.OK);
    }

    
    @RequestMapping(value = "")
    public ResponseEntity<List<ReactionDTO>> getReactionsByUsername(HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get reactions by username method has been called");

        return new ResponseEntity<>(reactionService.getReactionsByUsername(request), HttpStatus.OK);
    }

}
