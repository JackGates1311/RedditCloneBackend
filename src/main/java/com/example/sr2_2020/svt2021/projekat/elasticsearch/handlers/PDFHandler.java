package com.example.sr2_2020.svt2021.projekat.elasticsearch.handlers;

import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class PDFHandler extends DocumentHandler {

	@Override
	public Community getIndexUnit(File file) {
		Community community = new Community();
		try {
			String text = getText(file);
			community.setDescription(text);

			// metadata extraction
			PDDocument pdf = PDDocument.load(file);
			PDDocumentInformation info = pdf.getDocumentInformation();

			String name = ""+info.getTitle();
			community.setName(name);

			pdf.close();
		} catch (IOException e) {
			System.out.println("Error while converting data to PDF file");
		}

		return community;
	}

	@Override
	public String getText(File file) {
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(parser.getPDDocument());
		} catch (IOException e) {
			System.out.println("Error while converting data to PDF file");
		}
		return null;
	}
	
	public String getText(PDFParser parser) {
		try {
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(parser.getPDDocument());
		} catch (IOException e) {
			System.out.println("Error while converting data to PDF file");
		}
		return null;
	}

}
