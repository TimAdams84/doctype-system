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
package de.fraunhofer.scai.bio.types.text.doc.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.scai.bio.types.text.doc.structure.TextElement;

import lombok.Data;

/**
 * Bibliographic informations about the document itself. Like titleText, author,
 * etc.
 */
@Data public class Bibliographic implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7449243668495438139L;
    private Abstract documentAbstract;
    private List<Author> authors;
    private Date pubDate;
    private List<License> licenses;
    private Title title;
    private TextElement language;
    private TextElement source;
    private List<TextElement> issns;
    private List<PublicationType> publicationTypes;

    /**
     * @param author the {@link Author} to add to the {@link List} of authors
     */
    public void addAuthor(Author author) {
        if (this.authors == null) {
            this.authors = new ArrayList<Author>();
        }
        this.authors.add(author);
    }

    /**
     * @param license the {@link License} to add
     */
    public void addLicense(License license) {
        if (this.licenses == null) {
            this.licenses = new ArrayList<License>();
        }
        this.licenses.add(license);
    }

    /**
     * @param issn the issn to set
     */
    public void addIssn(TextElement issn) {
        if (this.issns == null) {
            this.issns = new ArrayList<TextElement>();
        }
        this.issns.add(issn);
    }

    /**
     * @param publicationType the {@link PublicationType} to set
     */
    public void addPublicationType(PublicationType publicationType) {
        if (this.publicationTypes == null) {
            this.publicationTypes = new ArrayList<PublicationType>();
        }
        this.publicationTypes.add(publicationType);
    }

}
