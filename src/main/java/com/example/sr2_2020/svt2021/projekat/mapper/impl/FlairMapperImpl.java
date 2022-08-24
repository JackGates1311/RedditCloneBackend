package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.FlairDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.FlairMapper;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import org.springframework.stereotype.Component;
import com.example.sr2_2020.svt2021.projekat.model.Flair.FlairBuilder;

@Component
public class FlairMapperImpl implements FlairMapper {

    @Override
    public Flair mapDTOToFlair(FlairDTO flairDTO) {

        if(flairDTO == null)
            return null;

        FlairBuilder flair = Flair.builder();

        flair.flairId(flairDTO.getFlairId());
        flair.name(flairDTO.getName());

        return flair.build();
    }

    @Override
    public FlairDTO mapFlairToDTO(Flair flair) {

        if(flair == null)
            return null;

        FlairDTO flairDTO = new FlairDTO();

        flairDTO.setFlairId(flair.getFlairId());
        flairDTO.setName(flair.getName());

        return flairDTO;
    }
}
