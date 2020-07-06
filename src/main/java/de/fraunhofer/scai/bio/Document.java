/*
 * Copyright 2019 Fraunhofer Institute SCAI, St. Augustin, Germany
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
package de.fraunhofer.scai.bio;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.scai.bio.types.text.doc.DocumentElement;
import de.fraunhofer.scai.bio.types.text.doc.container.StructureElement;
import de.fraunhofer.scai.bio.types.text.doc.structure.TextElement;

import lombok.Data;

/**
 * @author marc
 * @author klein
 * <p>
 * Central datastructure of our DMS
 */
@Data public class Document implements Serializable {

    public static final int ABSTRACT_LENGTH = 250;
    public static final String PUBMED_ABSTRACT = "PUBMED_ABSTRACT";
    public static final String PMC_FULLTEXT = "PMC_FULLTEXT";
    /**
     * serial version id.
     */
    private static final long serialVersionUID = 4813140736245257135L;
    protected static Logger logger = LoggerFactory.getLogger(Document.class);

    private Provenance provenance;
    private DocumentElement documentElement;
    private String docType;
    private String originalMimeType;

    @JsonIgnore
    private Map<UUID, TextElement> textElementIndex; // a quick access to all TextElements and their
    // Annotations

    /**
     * Constructor.
     */
    public Document() {
        this.provenance = new Provenance();
        this.setTextElementIndex(new TreeMap<UUID, TextElement>());
    }

    /**
     * Adds a {@link StructureElement} to the index
     *
     * @param key              The key of the {@link StructureElement}
     * @param structureElement the {@link StructureElement}
     */
    public void addToTextElementIndex(UUID key, TextElement structureElement) {
        this.textElementIndex.put(key, structureElement);
    }

}
