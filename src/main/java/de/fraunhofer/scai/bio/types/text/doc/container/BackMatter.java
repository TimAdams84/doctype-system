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
package de.fraunhofer.scai.bio.types.text.doc.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fraunhofer.scai.bio.types.text.doc.meta.Bibliography;

import lombok.Data;

/**
 * The final principle part of a document, in which is usually found the bibliography, index, appendixes, etc. (DoCO)
 */
@Data public class BackMatter implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 242839067331745958L;

    private Bibliography bibliography;
    private Set<Section> sections;

    /**
     * @param section the {@link Section} to set
     */
    public void addSection(Section section) {
        if (sections == null) {
            sections = new HashSet<>();
        }
        this.sections.add(section);
    }


}
