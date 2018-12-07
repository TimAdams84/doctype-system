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
import java.util.List;

import de.fraunhofer.scai.bio.types.text.doc.meta.Abstract;

/**
 * The central principle part of a document, that contains the real content. It
 * may be subdivided hierarchically by the use of chapters and sections. (DoCO)
 */
public class BodyMatter implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -80408256147660520L;

    private List<Chapter> chapters;
    private List<Section> sections;

    @Deprecated /* to be found in {@link FrontMatter} */
    private Abstract docAbstract;
    
    /**
     * @return the section
     */
    public List<Section> getSections() {
	return sections;
    }

    /**
     * @param section the {@link Section} to set
     */
    public void addSection(Section section) {
	if (sections == null)
	    sections = new ArrayList<Section>();
	this.sections.add(section);
    }

    /**
     * @param sections the {@link List} of {@link Section}s to set
     */
    public void setSections(List<Section> sections) {
	this.sections = sections;
    }

    /**
     * @return the {@link List} of {@link Chapter}s
     */
    public List<Chapter> getChapter() {
	return chapters;
    }

    /**
     * @param chapter the {@link Chapter} to add
     */
    public void addChapter(Chapter chapter) {
	if (chapters == null)
	    chapters = new ArrayList<Chapter>();
	this.chapters.add(chapter);
    }
    
    /**
     * @param chapters the {@link List} of {@link Chapter}s to set
     */
    public void setChapters(List<Chapter> chapters) {
	this.chapters = chapters;
    }

    /**
     * @return the document {@link Abstract}
     */
    @Deprecated
    public Abstract getDocAbstract() {
	return docAbstract;
    }

    /**
     * @param docAbstract the document {@link Abstract} to set
     */
    @Deprecated
    public void setDocAbstract(Abstract docAbstract) {
	this.docAbstract = docAbstract;
    }

}
