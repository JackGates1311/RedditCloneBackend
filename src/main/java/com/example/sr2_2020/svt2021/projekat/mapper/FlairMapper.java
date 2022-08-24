package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.FlairDTO;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import com.example.sr2_2020.svt2021.projekat.model.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlairMapper {

    Flair mapDTOToFlair(FlairDTO flairDTO);

    FlairDTO mapFlairToDTO(Flair flair);

}
