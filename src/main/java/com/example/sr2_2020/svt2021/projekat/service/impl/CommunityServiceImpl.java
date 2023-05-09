package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTOResponse;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.CommunitySearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepository;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepositoryQuery;
import com.example.sr2_2020.svt2021.projekat.exception.CommunityNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.model.Banned;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Flair;
import com.example.sr2_2020.svt2021.projekat.repository.*;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;

    private final CommunityMapper communityMapper;

    private final TokenUtils tokenUtils;

    private final UserRepository userRepository;

    private final BannedRepository bannedRepository;

    private final FlairRepository flairRepository;

    private final PostRepository postRepository;

    private final CommunitySearchingRepository communitySearchingRepository;

    private final CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public ResponseEntity<String> createCommunity(CommunityDTORequest communityDTORequest) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting data for saving community ...");

        communityDTORequest.setIsSuspended(false);

        // TODO while creating new community, community moderator can add (before sending create community request)
        //  flairs (with add flair request, after we can select newly added flair) from or use already added flairs...

        List<Flair> flairs = new ArrayList<>();

        Community newCommunity;

        if(Objects.isNull(communityDTORequest.getFlairs())) {

            newCommunity = communityRepository.save(communityMapper.mapDTOToCommunity(communityDTORequest, flairs));

        } else {

            flairs = communityDTORequest.getFlairs().stream().map(
                    flairName -> flairRepository.findByName(flairName).orElseThrow(() ->
                    new SpringRedditCloneException("Flair not found with name: " + flairName))).collect(toList());

            newCommunity = communityRepository.save(communityMapper.mapDTOToCommunity(communityDTORequest, flairs));

            flairs.forEach(flair -> flair.getCommunities().add(newCommunity));
        }

        try {
            communitySearchingRepository.save(new CommunitySearching(newCommunity.getCommunityId().toString(),
                    newCommunity.getName(), newCommunity.getDescription(), 0, null));

            createCommunityPdfDocument(newCommunity);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.toString());
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - saving community to database");

        return ResponseEntity.status(HttpStatus.CREATED).body("Community is successfully created");

    }

    @Override
    public List<CommunityDTOResponse> getAllCommunities() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting all communities in database");

        return communityRepository.findCommunitiesByIsSuspended(false).stream().map(
                communityMapper::mapCommunityToDTO).collect(toList());
    }

    @Override
    public CommunityDTOResponse getCommunity(Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community by ID in database");


        Community community = communityRepository.findByCommunityIdAndIsSuspended(id, false).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered ID"));

        return communityMapper.mapCommunityToDTO(community);
    }

    @Override
    public ResponseEntity<CommunityDTORequest> editCommunity(CommunityDTORequest communityDTORequest, Long communityId) {

        communityDTORequest.setFlairs(communityDTORequest.getFlairs().stream().distinct().collect(toList()));

        //TODO Implement rebasing community flairs (this will be done on front side)

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community data for edit ...");

        List<Flair> flairs = new ArrayList<>();

        if(Objects.isNull(communityDTORequest.getFlairs())) {

            communityRepository.save(communityMapper.mapDTOToCommunity(communityDTORequest, flairs));

        } else {

            flairs = communityDTORequest.getFlairs().stream().map(flairName -> flairRepository.findByName(flairName).
                        orElseThrow(() -> new SpringRedditCloneException("Flair not found with name: " + flairName))).
                        collect(toList());

            Community community = communityMapper.mapDTOToCommunity(communityDTORequest, flairs);

            List<String> oldFlairs = communityRepository.findById(communityId).orElseThrow(() -> new
                    CommunityNotFoundException("Community not with id: " + communityId)).getFlair().stream().
                    map(Flair::getName).collect(toList());

            List<String> newFlairs = communityDTORequest.getFlairs();


            List<String> deletedFlairs = oldFlairs.stream().filter(
                    v -> !newFlairs.contains(v)).collect(Collectors.toList());

            List<String> flairsUsedInPosts = flairRepository.findFlairsUsedInPostsByCommunityId(communityId);

            System.out.println("OLD FLAIRS: " + oldFlairs);
            System.out.println("NEW FLAIRS: " + newFlairs);
            System.out.println("DELETED FLAIRS: " + deletedFlairs);
            System.out.println("USED FLAIRS IN POSTS: " + Arrays.toString(flairsUsedInPosts.toArray()));

            for(String flairToDelete: deletedFlairs) {
                for(String usedFlair : flairsUsedInPosts){
                    if(flairToDelete.equals(usedFlair)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                    }
                }
            }

            community.setIsSuspended(false);
            community.setCommunityId(communityId);

            // todo does it line is really needed here?!
            postRepository.findPostsByCommunity(community).forEach(post -> post.setCommunity(community));

            communityRepository.save(community);

            flairs.forEach(flair -> flair.getCommunities().add(community));
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(communityDTORequest);
    }

    @Override
    public ResponseEntity<CommunityDTORequest> suspendCommunityById(CommunityDTORequest communityDTORequest, Long id,
                                                                     HttpServletRequest request) {

        //TODO transfer SpringRedditClone exception to CommunityNotFoundException

        logger.info("LOGGER: " + LocalDateTime.now() + " - Finding community data...");

        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditCloneException("" +
                "Community not found with entered ID"));

        community.setIsSuspended(true);
        community.setSuspendedReason(communityDTORequest.getSuspendedReason());
        community.setFlair(null);

        communityRepository.save(community);

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        Banned banned = new Banned();

        banned.setTimestamp(LocalDateTime.now());
        banned.setCommunity(community);
        banned.setUser(userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found")));

        bannedRepository.save(banned);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public CommunityDTOResponse getCommunityByName(String name) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting community data by name...");

        Community community = communityRepository.findByNameAndIsSuspended(name, false).
                orElseThrow(() -> new SpringRedditCloneException("Community not found with entered name"));


        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving community data to database...");

        return communityMapper.mapCommunityToDTO(community);
    }

    @Override
    public void createCommunityPdfDocument(Community community) {

        String pdfDirectory = "documents/";

        File directory = new File(pdfDirectory);
        if (!directory.exists()) {
            if(!directory.mkdirs())
                logger.error("Error while creating directory" + pdfDirectory);

        }

        String filePath = null;

        Document document = new Document();

        try {

            filePath = (pdfDirectory + community.getName() + ".pdf").replaceAll("\\s+", "").
                    toLowerCase();

            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();
            BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/font.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 12);

            Paragraph nameParagraph = new Paragraph(community.getName(), font);
            nameParagraph.setSpacingAfter(25f);

            document.add(nameParagraph);

            Paragraph descriptionParagraph = new Paragraph(community.getDescription(), font);

            document.add(descriptionParagraph);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        try {
            communitySearchingRepositoryQuery.indexPdf(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
