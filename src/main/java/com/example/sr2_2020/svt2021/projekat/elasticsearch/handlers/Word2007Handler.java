package com.example.sr2_2020.svt2021.projekat.elasticsearch.handlers;

import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;

public class Word2007Handler extends DocumentHandler {

	public Community getIndexUnit(File file) {

		Community community = new Community();

		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);

			String name = we.getText();
			community.setName(name);

			POIXMLProperties props = wordDoc.getProperties();

			String description = props.getCoreProperties().getTitle();
			community.setDescription(description);
			
			we.close();

		} catch (Exception e) {
			System.out.println("Error occurred while parsing document file");
		}

		return community;
	}

	@Override
	public String getText(File file) {
		String text = null;
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
			text = we.getText();
			we.close();
		}catch (Exception e) {
			System.out.println("Error occurred while parsing document file");
		}
		return text;
	}

}
