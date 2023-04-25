package com.example.sr2_2020.svt2021.projekat.elasticsearch.handlers;

import com.example.sr2_2020.svt2021.projekat.model.Community;

import java.io.File;

public abstract class DocumentHandler {
	/**
	 * Od prosledjene datoteke se konstruise Lucene Document
	 * 
	 * @param file
	 *            datoteka u kojoj se nalaze informacije
	 * @return Lucene Document
	 */
	public abstract Community getIndexUnit(File file);
	public abstract String getText(File file);
}
