/*
 * Copyright 2018 Fraunhofer Institute SCAI, St. Augustin, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.fraunhofer.scai.bio.util;

import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import de.fraunhofer.scai.bio.Document;
import de.fraunhofer.scai.bio.types.text.doc.DocumentElement;
import de.fraunhofer.scai.bio.types.text.doc.container.BackMatter;
import de.fraunhofer.scai.bio.types.text.doc.container.BodyMatter;
import de.fraunhofer.scai.bio.types.text.doc.container.FrontMatter;
import de.fraunhofer.scai.bio.types.text.doc.container.Paragraph;
import de.fraunhofer.scai.bio.types.text.doc.container.Section;
import de.fraunhofer.scai.bio.types.text.doc.container.StructureElement;
import de.fraunhofer.scai.bio.types.text.doc.meta.Abstract;
import de.fraunhofer.scai.bio.types.text.doc.meta.Affiliation;
import de.fraunhofer.scai.bio.types.text.doc.meta.Author;
import de.fraunhofer.scai.bio.types.text.doc.meta.Bibliographic;
import de.fraunhofer.scai.bio.types.text.doc.meta.Bibliography;
import de.fraunhofer.scai.bio.types.text.doc.meta.Concept;
import de.fraunhofer.scai.bio.types.text.doc.meta.Date;
import de.fraunhofer.scai.bio.types.text.doc.meta.Keywords;
import de.fraunhofer.scai.bio.types.text.doc.meta.License;
import de.fraunhofer.scai.bio.types.text.doc.meta.MetaElement;
import de.fraunhofer.scai.bio.types.text.doc.meta.Person;
import de.fraunhofer.scai.bio.types.text.doc.meta.PublicationType;
import de.fraunhofer.scai.bio.types.text.doc.meta.Reference;
import de.fraunhofer.scai.bio.types.text.doc.meta.Title;
import de.fraunhofer.scai.bio.types.text.doc.structure.Figure;
import de.fraunhofer.scai.bio.types.text.doc.structure.ImageContent;
import de.fraunhofer.scai.bio.types.text.doc.structure.Sentence;
import de.fraunhofer.scai.bio.types.text.doc.structure.TextElement;

import lombok.extern.slf4j.Slf4j;

/**
 * a general class to compose a document from scratch, allows to create sections, paragraphs,
 * sentences, tables, ...
 * <p>
 * - first: call constructor - second: create and append document elements - finally: call
 * finalizeCAS to set all indices and document text
 *
 * @author marc
 */
/**
 * @author marc
 *
 */
@Slf4j
public class DocumentBuilder {


    private SentenceDetector sentenceDetector;

    public DocumentBuilder() {

        sentenceDetector = new SentenceDetector(0);
    }

    public Author createAuthor(String forename, String surname) {
    	  TextElement forenameTE = createTextElement(forename);
    	  TextElement surnameTE = createTextElement(surname);
    	  Person person = new Person();
    	  person.setForename(forenameTE);
    	  person.setSurname(surnameTE);
    	  Author author = new Author();
    	  author.setAuthor(person);
    	  return author;
    }

    /**
     * @param titleTextElement
     * @return
     */
    public Title createDocumentTitle(TextElement titleTextElement, TextElement subTitleText) {

        Title dTitle = null;

        if (titleTextElement != null && titleTextElement.getText() != null && titleTextElement.getText().length() > 0) {

            dTitle = new Title();
            dTitle.setTitleText(titleTextElement);
        }

        if (subTitleText != null && subTitleText.getText() != null && subTitleText.getText().length() > 0) {

            if (dTitle == null) {
                dTitle = new Title();
            }
            dTitle.setSubTitleText(subTitleText);
        }

        return dTitle;
    }

    /**
     * @param text
     * @return
     */
    public Section createSection(String text) {
        return createSection(text, text);
    }

    /**
     * no spacer after paragraphs, no sentences
     *
     * @param text
     * @param rhetorical
     * @param title
     * @return
     */
    public Section createSimpleSection(String text, String rhetorical, String title) {

        Section dSection = new Section();
        TextElement rhetoricalElement = createTextElement(rhetorical);
        dSection.setRhetorical(rhetoricalElement);
        TextElement titleElement = createTextElement(title);
        dSection.setTitle(titleElement);

        TextElement textElement = createTextElement(text);
        Paragraph paragraph = new Paragraph();
        StructureElement structureElement = new StructureElement();
        structureElement.setTextElement(textElement);

        paragraph.addStructureElement(structureElement);
        dSection.addParagraph(paragraph);
        return dSection;
    }

    /**
     * @param rhetorical
     * @param title
     * @return
     */
    public Section createSection(String rhetorical, String title) {

        Section dSection = new Section();
        TextElement rhetoricalElement = createTextElement(rhetorical);
        dSection.setRhetorical(rhetoricalElement);
        TextElement titleElement = createTextElement(title);
        dSection.setTitle(titleElement);

        return dSection;
    }

    /**
     * @param text
     * @param createSentences
     * @return
     */
    public Paragraph createParagraph(String text, boolean createSentences) {
    	return createParagraph(text, createSentences, -1);
    }
    
    public Paragraph createParagraph(String text) {
      return createParagraph(text, false);
    }

    /**
     * create a paragraph
     *
     * @param text
     * @param createSentences
     * @param sentence_limit; -1 iff no limit; maximum number of sentences to create
     * @return
     */
    public Paragraph createParagraph(String text, boolean createSentences, int sentence_limit) {

        Paragraph dParagraph = null;

        if (text != null && !text.isEmpty()) {
            if (createSentences) {
                List<Integer> eos = sentenceDetector.findSentencesEndPositions(text);

                List<String> sentences = sentenceDetector.getSentences(eos, text, 5);
                String note = null;
                
                // all of them
                if( sentence_limit<0 || (sentence_limit+10<sentences.size()) ) {
                	sentence_limit = sentences.size();
                } else {
                	note = String.format(" >> Note: skipped %d sentences due to size limit of %d.", sentence_limit-sentences.size(), sentence_limit);                	
                }
                
                if (!sentences.isEmpty()) {
                    dParagraph = new Paragraph();
                    for (int i = 0; i < sentence_limit; i++) {
                        if (i < sentences.size()) {
                            StructureElement sentence = new StructureElement();
                            sentence.setSentence(createSentence(sentences.get(i)));

                            dParagraph.addStructureElement(sentence);
                        }
                    }
                    
                    // add comment if skipped sentences
                    if(note != null) {
                      StructureElement sentence = new StructureElement();
                      sentence.setSentence(createSentence(note));

                      dParagraph.addStructureElement(sentence);
                      
                      log.info(note);
                    }
                }

                log.debug("Created " + sentences.size() + " sentence(s).");

            } else {
                dParagraph = new Paragraph();
                TextElement paragraphText = createTextElement(text);
                StructureElement pText = new StructureElement();
                pText.setTextElement(paragraphText);
                dParagraph.addStructureElement(pText);
            }
        }

        return dParagraph;
    }

    /**
     * Creates a Sentence from the given text
     *
     * @param text
     * @return
     */
    public Sentence createSentence(String text) {
        if (text != null && text.length() > 0) {
            Sentence dsentence = new Sentence();
            dsentence.setText(createTextElement(text));
            return dsentence;
        } else {
            return null;
        }
    }

    /**
     * @param textElement
     * @return
     */
    public List<Sentence> createSentences(TextElement textElement) {

        List<Sentence> dSentences = null;

        if (textElement != null && textElement.getText() != null) {
            dSentences = createSentences(textElement.getText());
        }

        return dSentences;
    }

    public List<Sentence> createSentences(String text) {

        List<Sentence> dSentences = new ArrayList<Sentence>();

        if (text != null && !text.isEmpty()) {

            List<Integer> eos = sentenceDetector.findSentencesEndPositions(text);

            List<String> sentences = sentenceDetector.getSentences(eos, text, 5);
            if (!sentences.isEmpty()) {
                for (int i = 0; i < sentences.size(); i++) {
                    dSentences.add(createSentence(sentences.get(i)));
                }
            }
        }

        return dSentences;
    }


    /**
     * Creates a {@link TextElement} for a given String.
     * The {@link UUID} for the {@link TextElement} is generated from the values of the given String.
     * see UUID.nameUUIDFromBytes(String)
     * @param text Content of the {@link TextElement} to create
     * @return the created {@link TextElement}
     */
    public TextElement createTextElement(String text) {
        TextElement textElement = new TextElement();

        if (text != null && !text.isEmpty()) {
            textElement.setText(text);
        } else {
        	textElement.setText("");
        }
        if (text != null && !text.isEmpty()) {
            textElement.setUuid(UUID.nameUUIDFromBytes(text.getBytes()));
        } else {
            textElement.setUuid(UUID.randomUUID());
        }

        return textElement;
    }
    
    public Title createTitle(String titleContent) {
        Title title = new Title();
        title.setTitleText(createTextElement(titleContent));
        return title;
    }

    /**
     * creating a figure annotation from begin to end
     *
     * @param rhetorical
     * @param caption
     * @param content
     * @return <code>structure.Figure</code>
     */
    public Figure createFigure(String rhetorical, String caption, String title, ImageContent content) {
        Figure figure = new Figure();

        if (rhetorical != null) {
            TextElement textElement = createTextElement(rhetorical);
            figure.setRhetorical(textElement);
        }

        if (title != null) {
            TextElement textElement = createTextElement(title);
            figure.setTitle(textElement);
        }

        if (content != null) {
            figure.setImage(content);
        }

        if (caption != null) {
            TextElement textElement = createTextElement(caption);
            figure.setCaption(textElement);
        }

        return figure;
    }

    /**
     * creating a table annotation from begin to end
     *
     * @param caption      <code>String</code>
     * @param title       <code>String</code>
     * @param rhetorical <code>String</code>
     * @return <code>structure.List</code>
     */
    public de.fraunhofer.scai.bio.types.text.doc.structure.Table createTable(String rhetorical, String caption,
                                                                             String title) {
        de.fraunhofer.scai.bio.types.text.doc.structure.Table dTable =
            new de.fraunhofer.scai.bio.types.text.doc.structure.Table();

        if (rhetorical != null) {
            TextElement textElement = createTextElement(rhetorical);
            dTable.setRhetorical(textElement);
        }

        if (title != null) {
            TextElement textElement = createTextElement(title);
            dTable.setTitle(textElement);
        }

        if (caption != null) {
            TextElement textElement = createTextElement(caption);
            dTable.setCaption(textElement);
            createParagraph(caption, true);
        }

        return dTable;

    }

    public Paragraph appendParagraph(String text) {
        return appendParagraph(text, false);
    }

    /**
     * @param text           <code>String</code>
     * @param creatSentences <code>boolean</code>
     * @return
     */
    public Paragraph appendParagraph(String text, boolean creatSentences) {

        Paragraph dParagraph = createParagraph(text, creatSentences);
        return dParagraph;
    }

    // /**
    // * @param reference
    // */
    // private void appendBib(ElementInterface node, Reference reference) {
    //
    // List<Reference> tail = bib.getReferences();
    //
    // List<Reference> elem = new ArrayList<Reference>();
    // elem.add(reference);
    //
    // // still empty, just add
    // if (tail == null || tail.isEmpty()) {
    // bib.setReferences(elem);
    //
    // // iterate and append at the end
    // } else {
    // List<Reference> head = bib.getReferences();
    // head.addAll(elem);
    // }
    //
    // elem.addAll(tail);
    // }

    /**
     * get the keywords as StringList
     *
     * @param keywords   <code>util.List</code>
     * @param rhetorical <code>String</code>
     * @param createNNE  <code>boolean</code>
     * @return <code>structure.List</code>
     */
    public Keywords createKeywords(List<String> keywords, String rhetorical, boolean createNNE) {

        if (keywords != null && !keywords.isEmpty()) {

            Keywords keywordList = new Keywords();

            for (int i = 0; i < keywords.size(); i++) {
                TextElement kwElement = createTextElement(keywords.get(i).split("@")[0].trim());
                keywordList.addKeyword(kwElement);

                // TODO add annotations from keywords
                // String identifier = null;
                // try {
                // identifier = keywords.get(i).split("\0")[1];
                // } catch (Exception e) {
                // }
                // String source = null;
                // try {
                // source = keywords.get(i).split("\0")[2];
                // } catch (Exception e) {
                // }
            }
            TextElement rhetoricalElement;
            if (rhetorical != null && !rhetorical.isEmpty()) {
                rhetoricalElement = createTextElement(rhetorical);
            } else {
                rhetoricalElement = createTextElement("keywords");
            }
            keywordList.setRhetorical(rhetoricalElement);
            return keywordList;
        }

        return null;
    }

    /**
     * @param textElement
     * @return
     */
    public StructureElement createStructureElement(TextElement textElement) {
        if (textElement == null) {
            return null;
        }

        StructureElement structureElement = new StructureElement();
        structureElement.setTextElement(textElement);

        return structureElement;
    }

    /**
     * @param text
     * @return
     */
    public StructureElement createStructureElement(String text) {
        if (text != null) {
            StructureElement se = new StructureElement();
            se.setTextElement(createTextElement(text));
            return se;
        }
        return null;
    }

    public void setTitle(Document document, String title, String subtitle) {

        if (title == null || title.length() <= 0) {
            title = "no title provided";
        }

        TextElement titleTextElement = createTextElement(title);
        TextElement subtitleTextElement = createTextElement(subtitle);

        getFrontMatter(document).setTitleText(titleTextElement);

        getBibliographic(document).setTitle(
            createDocumentTitle(titleTextElement, subtitleTextElement)
        );

    }

    public void setDocType(Document document, String docType) {
      if (docType == null || docType.length() <= 0) {
          docType = "unspecified";
      }
      document.setDocType(docType);
  }

    
    public void setLanguage(Document document, String language) {
        if (language == null || language.length() <= 0) {
            language = "unspecified";
        }

        getBibliographic(document).setLanguage(createTextElement(language));
    }

    public void setAbstract(Document document, String dAbstract) {
        Abstract documentAbstract = new Abstract();

        if (dAbstract == null || dAbstract.length() <= 0) {
            dAbstract = "no abstract provided";
        }

        documentAbstract.addAbstractSection(createSimpleSection(dAbstract, "Abstract", "Abstract"));
        getFrontMatter(document).setDocumentAbstract(documentAbstract);
        getBibliographic(document).setDocumentAbstract(documentAbstract);
    }

    public DocumentElement getDocumentElement(Document document) {
        DocumentElement docElem = document.getDocumentElement();

        if (docElem == null) {
            docElem = new DocumentElement();
            document.setDocumentElement(docElem);
        }

        return docElem;
    }

    public FrontMatter getFrontMatter(Document document) {
        FrontMatter frontMatter = getDocumentElement(document).getFrontMatter();

        if (frontMatter == null) {
            frontMatter = new FrontMatter();
            getDocumentElement(document).setFrontMatter(frontMatter);
        }

        return frontMatter;
    }

    public Bibliography getBibliography(Document document) {
        Bibliography bib = getBackMatter(document).getBibliography();

        if (bib == null) {
            bib = new Bibliography();
            getBackMatter(document).setBibliography(bib);
        }

        return bib;
    }
    
    public void addReference(Document document, String id, String referenceSource, String publicationId, 
            String publicationType, String title, List<Author> authors, java.time.LocalDate docDate) {

        Bibliography bib = getBibliography(document);

        Reference reference = new Reference();

        if (authors != null) {
            for (Author author : authors) {
                reference.addAuthor(author);
            }
        }
        if (publicationId != null) {
            reference.addPublicationId(createTextElement(publicationId));
        }
        //reference.setLanguage(language);
        if (publicationType != null) {
            reference.setPublicationType(createTextElement(publicationType));
        }
        if (referenceSource != null) {
            reference.setReferenceSource(createTextElement(referenceSource));
        }
        if (title != null) {
            reference.setTitle(createDocumentTitle(createTextElement(title), null));
        }
        if (docDate != null && docDate.isSupported(ChronoField.YEAR_OF_ERA)
            && docDate.isSupported(ChronoField.MONTH_OF_YEAR)) {
            int dayOfMonth = 1;
            if (docDate.isSupported(ChronoField.DAY_OF_MONTH)) {
                dayOfMonth = docDate.getDayOfMonth();
            }
            Date date = new Date();
            date.setDate(dayOfMonth, docDate.getMonthValue(), docDate.getYear());
            reference.setDate(date);
        }

        bib.addReference(id, reference);

    }

    public Bibliographic getBibliographic(Document document) {
        Bibliographic bib = getMetaElement(document).getBibliographic();

        if (bib == null) {
            bib = new Bibliographic();
            getMetaElement(document).setBibliographic(bib);
        }

        return bib;
    }

    public BodyMatter getBodyMatter(Document document) {
        BodyMatter bodyMatter = getDocumentElement(document).getBodyMatter();

        if (bodyMatter == null) {
            bodyMatter = new BodyMatter();
            getDocumentElement(document).setBodyMatter(bodyMatter);
        }

        return bodyMatter;
    }

    public BackMatter getBackMatter(Document document) {
        BackMatter backMatter = getDocumentElement(document).getBackMatter();

        if (backMatter == null) {
            backMatter = new BackMatter();
            getDocumentElement(document).setBackMatter(backMatter);
        }

        return backMatter;
    }

    public MetaElement getMetaElement(Document document) {

        MetaElement meta = getDocumentElement(document).getMetaElement();

        if (meta == null) {
            meta = new MetaElement();
            getDocumentElement(document).setMetaElement(meta);
        }

        return meta;
    }

    public List<License> createLicense(String value) {
        List<License> license = new ArrayList<>();
        License lic = new License();
        lic.setLicenseName(createTextElement(value));
        license.add(lic);
        return license;
    }
    
    public Section createMainSection(Document document, String text) {

        Section section = createSection("Main Section", "Main");

        Paragraph mainParagraph = createParagraph(text, true);

        section.addParagraph(mainParagraph);

        return section;
    }

    public void setSource(Document document, String source) {
      if (source == null || source.length() <= 0) source = "unspecified";
      getBibliographic(document).setSource(createTextElement(source));      
    }
    
    public Concept setDocumentId(Document document, String source, String id, String altlabel) {
        Concept concept = getConcept(document);

        if (source == null || source.length() <= 0) source = "unspecified";
        if (id == null || id.length() <= 0) id = "unspecified";
        if (altlabel == null || altlabel.length() <= 0) altlabel = "unspecified";

        concept.setIdentifierSource(createTextElement(source));
        concept.setIdentifier(createTextElement(id));
        concept.setPrefLabel(createTextElement(source + ":" + id));
        concept.setAltLabel(createTextElement(altlabel));

        return concept;
    }

    public Concept setDocumentIdasHash(Document document, String content, String altlabel) {

        String id = DigestUtils.sha512Hex(content);
        String source = "sha512Hex";

        return setDocumentId(document, source, id, altlabel);
    }

    public Concept getConcept(Document document) {
        Concept concept = getMetaElement(document).getConcept();

        if (concept == null) {
            concept = new Concept();
            getMetaElement(document).setConcept(concept);
        }

        return concept;
    }

    public void addPerson(Document document, String forename, String surname) {

        Person person = new Person();
        person.setForename(createTextElement(forename));
        person.setSurname(createTextElement(surname));

        Author author = new Author();
        author.setAuthor(person);

        getBibliographic(document).addAuthor(author);

    }
    
    public void addOrganization(Document document, String name, String forename, String surname) {
        Person person = new Person();
        person.setForename(createTextElement(forename));
        person.setSurname(createTextElement(surname));

    	
    	Affiliation organization = new Affiliation();
        organization.setOrganization(createTextElement(name));

        Author author = new Author();
        author.setAuthor(person);
        author.setOrganization(organization);

        getBibliographic(document).addAuthor(author);

    }

    @Deprecated	// creates empty author!?!
    public void addOrganization(Document document, String name) {
        Affiliation organization = new Affiliation();
        organization.setOrganization(createTextElement(name));

        Author author = new Author();
        author.setOrganization(organization);

        getBibliographic(document).addAuthor(author);

    }

    public void addOtherDocumentId(Document document, String id) {
        Concept concept = getConcept(document);

        concept.addAltLabel(createTextElement(id));

    }

    public void addKeywords(Document document, String rhetorical, List<String> keywords) {

        Keywords kws = createKeywords(keywords, rhetorical, false);
        document.getDocumentElement().getMetaElement().addKeywords(kws);

    }
    
    public void setPublicationDate(Document document, int day, int month, int year) {
    	Date pubDate = new Date();
    	pubDate.setDate(day, month, year);
    	getBibliographic(document).setPubDate(pubDate);
    }

	public void addPublicationType(Document document, String id, String type) {
        PublicationType publicationType = new PublicationType();
        publicationType.setIdentifier(createTextElement(id));
        publicationType.setPublicationType(createTextElement(type));
        getBibliographic(document).addPublicationType(publicationType);		
	}

}
