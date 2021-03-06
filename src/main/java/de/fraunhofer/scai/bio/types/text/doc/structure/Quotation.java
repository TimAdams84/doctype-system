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
package de.fraunhofer.scai.bio.types.text.doc.structure;

import java.io.Serializable;

import de.fraunhofer.scai.bio.types.text.doc.meta.Reference;

import lombok.Data;

/**
 * A quotation block which cites another document.
 */
@Data public class Quotation implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8147702454907782570L;
    @Deprecated // should point to a reference in the bibliography
    private Reference reference;

    private TextElement label;
    private String referenceId; // points to the key in the bibliography

}
