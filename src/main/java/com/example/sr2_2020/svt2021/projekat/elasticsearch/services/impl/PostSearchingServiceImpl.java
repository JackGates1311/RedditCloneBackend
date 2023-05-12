package com.example.sr2_2020.svt2021.projekat.elasticsearch.services.impl;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.PostSearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.PostSearchingRepositoryQuery;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.PdfService;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.PostSearchingService;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.repository.CommentRepository;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PostSearchingServiceImpl implements PostSearchingService {

    private final PostSearchingRepositoryQuery postSearchingRepositoryQuery;

    private final PdfService pdfService;

    private final CommentRepository commentRepository;

    public PostSearchingServiceImpl(PostSearchingRepositoryQuery postSearchingRepositoryQuery, PdfService pdfService,
                                    CommentRepository commentRepository) {
        this.postSearchingRepositoryQuery = postSearchingRepositoryQuery;
        this.pdfService = pdfService;
        this.commentRepository = commentRepository;
    }

    @Override
    public ResponseEntity<List<PostSearching>> searchPosts(String title, String text, Integer minKarma,
                                                           Integer maxKarma, Float minComments, Float maxComments,
                                                           String flairs, Boolean isMust, Boolean isPdfIndex,
                                                           String titleSearchMode, String textSearchMode,
                                                           String flairsSearchMode) {
        return postSearchingRepositoryQuery.search(title, text, minKarma, maxKarma, minComments, maxComments, flairs,
                isMust, isPdfIndex, titleSearchMode, textSearchMode, flairsSearchMode);
    }

    @Override
    public String getPdfText(byte[] pdfContent) {
        try {
            return pdfService.getPdfText(new PdfReader(pdfContent)).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getCommentsCount(Post post) {

        List<Comment> comments = commentRepository.findAllByPostAndIsDeleted(post, false);

        return comments.size();
    }
}
