package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.FlairDTO;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.FlairMapper;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import com.example.sr2_2020.svt2021.projekat.repository.FlairRepository;
import com.example.sr2_2020.svt2021.projekat.service.FlairService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FlairServiceImpl implements FlairService {

    private final FlairRepository flairRepository;

    private final FlairMapper flairMapper;

    @Override
    public ResponseEntity<String> save(FlairDTO flairDTO) {

        flairRepository.save(flairMapper.mapDTOToFlair(flairDTO));

        return new ResponseEntity<>("Flair created successfully", HttpStatus.OK);
    }

    @Override
    public List<FlairDTO> getAllFlairs() {

        return flairRepository.findAll().stream().map(flairMapper::mapFlairToDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<FlairDTO> getFlair(Long id) {

        Flair flair = flairRepository.findById(id).orElseThrow(() -> new SpringRedditCloneException("Flair not found"));

        return new ResponseEntity<>(flairMapper.mapFlairToDTO(flair), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> editFlair(FlairDTO flairDTO, String name) {

        Flair flair = flairRepository.findEditableByName(name).orElseThrow(() ->
                new SpringRedditCloneException("Flair with name: '" + name +"' not found or it is not editable"));

        Flair flairEditData = flairMapper.mapDTOToFlair(flairDTO);

        flair.setFlairId(flair.getFlairId());
        flair.setName(flairEditData.getName());

        flairRepository.save(flair);

        return new ResponseEntity<>("Flair has been successfully edited", HttpStatus.ACCEPTED);


        /* Flair flair = flairRepository.findByName(name).orElseThrow(() ->
                new SpringRedditCloneException("Flair with name: '" + name +"' not found")); */




    }

    @Override
    public ResponseEntity<String> deleteFlair(String name) {

        flairRepository.deleteById(flairRepository.findEditableByName(name).orElseThrow(() ->
                new SpringRedditCloneException("Flair with name: '" + name + "' not found or it is not possible " +
                        "to delete")).getFlairId());

        return new ResponseEntity<>("Flair has been deleted successfully", HttpStatus.ACCEPTED);
    }
}
