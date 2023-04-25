package com.example.sr2_2020.svt2021.projekat.elasticsearch.handlers;

import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WordHandler extends DocumentHandler {

	public Community getIndexUnit(File file) {
		Community community = new Community();
		InputStream is;

		try {
			is = new FileInputStream(file);
			WordExtractor we = new WordExtractor(is);
			String name = we.getText();
			community.setName(name);
			
			SummaryInformation si = we.getSummaryInformation();
			String description = si.getTitle();
			community.setDescription(description);

			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Document does not exists");
		} catch (Exception e) {
			System.out.println("Error while parsing document file");
		}

		return community;
	}

	@Override
	public String getText(File file) {
		String text = null;
		try {
			WordExtractor we = new WordExtractor(new FileInputStream(file));
			text = we.getText();
			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Document does not exists");
		} catch (Exception e) {
			System.out.println("Error while parsing document file");
		}
		return text;
	}

}
