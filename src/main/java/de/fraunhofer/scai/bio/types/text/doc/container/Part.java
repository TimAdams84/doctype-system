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

import de.fraunhofer.scai.bio.types.text.doc.structure.Page;

import lombok.Data;

/**
 * The order is Part, Chapter, Section, SubSection, SubSubSection.
 */
@Data public class Part implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3806436372769367479L;
    private Set<Chapter> chapters;
    private Set<Page> pages;

    /**
     * @param chapter the {@link Chapter} to add
     */
    public void addChapter(Chapter chapter) {
        if (this.chapters == null) {
            this.chapters = new HashSet<>();
        }
        this.chapters.add(chapter);
    }

}
