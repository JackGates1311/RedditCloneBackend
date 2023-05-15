package com.example.sr2_2020.svt2021.projekat.elasticsearch.services;

import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface PdfService {

    void indexPdf (String pdfFilePath, String indexName, Community community, Post post) throws IOException;

    StringBuilder getPdfText (PdfReader pdfReader) throws IOException;

    void createPdfDocument (Community community, Post post, String indexName);

    byte[] getPdfContent (MultipartFile pdfFile);
}
