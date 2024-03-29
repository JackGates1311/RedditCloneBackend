package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.FlairDTO;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import com.example.sr2_2020.svt2021.projekat.service.FlairService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flair")
@AllArgsConstructor
public class FlairController {

    private final FlairService flairService;

    //TODO implement proper user authorization for these services!!

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> addNewFlair(@RequestBody FlairDTO flairDTO){

        return flairService.save(flairDTO);
    }

    @RequestMapping(value="")
    public List<FlairDTO> getAllFlairs(){

        return flairService.getAllFlairs();
    }

    @RequestMapping(value="/{id}")
    public ResponseEntity<FlairDTO> getFlair(@PathVariable("id") Long id){

        return flairService.getFlair(id);
    }

    @RequestMapping(value="/{name}", method = RequestMethod.PUT)
    public ResponseEntity<?> editFlair(@RequestBody FlairDTO flairDTO, @PathVariable("name") String name){

        return flairService.editFlair(flairDTO, name);
    }

    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteFlair(@PathVariable("name") String name){

        return flairService.deleteFlair(name);
    }

}
