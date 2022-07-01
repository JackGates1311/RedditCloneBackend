package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.model.ReactionType;
import com.example.sr2_2020.svt2021.projekat.service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/reactions")
@AllArgsConstructor
public class ReactionController {

    @Autowired
    ReactionService reactionService;

    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity reaction(@RequestBody ReactionDTO reactionDTO, HttpServletRequest request) {

        reactionService.reaction(reactionDTO, request);

        return new ResponseEntity("Your reaction has been saved", HttpStatus.OK);
    }

    
    @RequestMapping(value = "")
    public ResponseEntity<List<ReactionDTO>> getReactionsByUsername(HttpServletRequest request) {

        return new ResponseEntity<>(reactionService.getReactionsByUsername(request), HttpStatus.OK);
    }

}
