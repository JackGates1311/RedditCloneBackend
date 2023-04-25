package com.example.sr2_2020.svt2021.projekat.elasticsearch.handlers;

import com.example.sr2_2020.svt2021.projekat.model.Community;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TextDocHandler extends DocumentHandler {

	@Override
	public Community getIndexUnit(File file) {
		Community community = new Community();
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

			String firstLine = reader.readLine();

			community.setName(firstLine);

			String secondLine = reader.readLine();
			community.setDescription(secondLine);

			StringBuilder fullText = new StringBuilder();
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText.append(" ").append(secondLine);
			}
			community.setDescription(fullText.toString());
			
			community.setName(file.getCanonicalPath());
			
			return community;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException ignored) {
				}
		}
	}

	@Override
	public String getText(File file) {
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, StandardCharsets.UTF_8));
			String secondLine;
			StringBuilder fullText = new StringBuilder();
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText.append(" ").append(secondLine);
			}
			return fullText.toString();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File does not exists");
		} catch (IOException e) {
			throw new IllegalArgumentException("Something went wrong while reading file");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException ignored) {
				}
		}
	}

}
