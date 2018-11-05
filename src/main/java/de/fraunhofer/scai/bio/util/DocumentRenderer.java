/*
 * Copyright 2018 Fraunhofer Institute SCAI, St. Augustin, Germany
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fraunhofer.scai.bio.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fraunhofer.scai.bio.Document;
import de.fraunhofer.scai.bio.types.text.doc.container.Paragraph;
import de.fraunhofer.scai.bio.types.text.doc.container.Section;
import de.fraunhofer.scai.bio.types.text.doc.container.StructureElement;
import de.fraunhofer.scai.bio.types.text.doc.meta.Abstract;
import de.fraunhofer.scai.bio.types.text.doc.meta.Bibliographic;
import de.fraunhofer.scai.bio.types.text.doc.structure.Sentence;

/**
 * @author tadams
 * 
 *         This class provides functionality to render document contents in
 *         different formats by traversing the {@link Document} structure
 *
 */
public class DocumentRenderer {

    private static final Pattern PUNCTUATION = Pattern.compile("[.!?\\;]");

    /**
     * Assemble all document contents which can be written as String, return them as
     * plain text list seperated by blanks. Used for indexiatin in Solr.
     * 
     * @param doc <code>Document</code>
     * @return String of conent words seperated by blanks
     */
    public static String renderTextContents(Document doc) {

	StringBuilder sb = new StringBuilder();
	HashSet<String> contents = new HashSet<String>();
	Map<String, StructureElement> index = doc.getStructureElementIndex();

	for (StructureElement se : index.values()) {
	    Matcher unwantedMatcher = PUNCTUATION.matcher(getText(se));
	    contents.add(unwantedMatcher.replaceAll(""));
//	    FIXME
//	    for (Annotation anno : se.getAnnotations()) {
//		contents.add(anno.getAnnotationText());
//	    }
	}
	for (String word : contents) {
	    sb.append(word + " ");
	}
	return sb.toString();
    }

    /**
     * Get the <code>Document</code> {@link Abstract} in plain text format
     * 
     * @param doc input <code>Document</code>
     * @return Abstract String
     */
    public static String getDocumentAbstract(Document doc) {
	StringBuilder sb = new StringBuilder();
	Bibliographic bib = doc.getDocumentElement().getMetaElement().getBibliographic();
	List<Section> abstractSections = bib.getDocumentAbstract().getAbstractSections();
	for (Section section : abstractSections) {
	    List<Paragraph> paragraphs = section.getParagraphs();
	    for (Paragraph para : paragraphs) {
		// either a sentence or a structure element is set
		if (para.getSentences() != null) {
		    List<Sentence> sentences = para.getSentences();
		    for (Sentence sentence : sentences) {
			sb.append(sentence.getText().getText());
		    }
		} else {
		    List<StructureElement> structureElements = para.getStructureElements();
		    for (StructureElement se : structureElements) {
			sb.append(getText(se));
		    }
		}
	    }
	}
	return sb.toString();
    }

    /**
     * go through all fields and extract from the non-empty one the text
     * 
     * @return <code>String</code>
     */
    public static String getText(StructureElement se) {
	if (se.getCaptionedBox() != null)
	    return se.getCaptionedBox().toString();
	if (se.getCode() != null)
	    return se.getCode().toString();
	if (se.getDataTable() != null)
	    return se.getDataTable().toString();
	if (se.getFigure() != null)
	    return se.getFigure().toString();
	if (se.getFormula() != null)
	    return se.getFormula().toString();
	if (se.getImageContent() != null)
	    return se.getImageContent().toString();
	if (se.getOutline() != null)
	    return se.getOutline().toString();
	if (se.getQuotation() != null)
	    return se.getQuotation().toString();
	if (se.getTable() != null)
	    return se.getTable().toString();
	if (se.getTextElement() != null)
	    return se.getTextElement().toString();

	return null;
    }
}
