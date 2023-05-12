package com.example.sr2_2020.svt2021.projekat.elasticsearch.services.impl;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.PdfService;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PdfServiceImpl implements PdfService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    static final Logger logger = LogManager.getLogger(PdfService.class);

    public PdfServiceImpl(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    @Override
    public void indexPdf(String pdfFilePath, String indexName, Community community, Post post) throws IOException {

        File pdfFile = new File(pdfFilePath);

        byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());

        PdfReader pdfReader = new PdfReader(pdfContent);

        StringBuilder pdfText = getPdfText(pdfReader);

        Map<String, Object> pdfDocument = new HashMap<>();

        if(community != null) {
            pdfDocument.put("id", UUID.randomUUID().toString());
            pdfDocument.put("name", pdfText.toString().split("\r?\n")[0]);
            pdfDocument.put("description", pdfText.toString());
        }

        if(post != null) {
            pdfDocument.put("id", UUID.randomUUID().toString());
            pdfDocument.put("title", pdfText.toString().split("\r?\n")[0]);
            pdfDocument.put("text", pdfText.toString());
        }

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(pdfDocument).build();
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of(indexName));

        pdfReader.close();
    }

    @Override
    public StringBuilder getPdfText(PdfReader pdfReader) throws IOException {

        int pages = pdfReader.getNumberOfPages();

        StringBuilder pdfText = new StringBuilder();

        for (int i = 1; i <= pages; i++) {
            pdfText.append(PdfTextExtractor.getTextFromPage(pdfReader, i));
        }

        return pdfText;
    }

    @Override
    public Boolean createPdfDocument(Community community, Post post, String indexName)
    {
        String pdfDirectory = "documents/";

        File directory = new File(pdfDirectory);
        if (!directory.exists()) {
            if(!directory.mkdirs()) {
                logger.error("Error while creating directory" + pdfDirectory);
                return false;
            }
        }

        String filePath;

        Document document = new Document();

        try {
            BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/font.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 12);

            String documentName = "";

            if(community != null) {
                documentName = community.getName() + "_community";
            }
            if(post != null) {
                documentName = post.getTitle() + "_post";
            }

            filePath = (pdfDirectory + documentName + ".pdf").replaceAll("\\s+", "").toLowerCase();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Paragraph firstParagraph = null;
            Paragraph secondParagraph = null;

            if(community != null) {
                firstParagraph = new Paragraph(community.getName(), font);
                firstParagraph.setSpacingAfter(25f);
                secondParagraph = new Paragraph(community.getDescription(), font);
            }

            if(post != null) {
                firstParagraph = new Paragraph(post.getTitle(), font);
                firstParagraph.setSpacingAfter(25f);
                secondParagraph = new Paragraph(post.getText(), font);
            }

            document.add(firstParagraph);
            document.add(secondParagraph);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            document.close();
        }

        if(community != null) {
            try {
                indexPdf(filePath, indexName, community, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(post != null) {
            try {
                indexPdf(filePath, indexName, null, post);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
