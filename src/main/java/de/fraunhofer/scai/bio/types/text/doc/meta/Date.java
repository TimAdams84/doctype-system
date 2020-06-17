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

import lombok.Data;

/**
 * @author klein
 */
@Data public class Date implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8973046289828947316L;
    private int day;
    private int month;
    private int year;

    /**
     * Default constructor.
     */
    public Date() {
    }

    public void setDate(int day, int month, int year) {
      setDay(day);
      setMonth(month);
      setYear(year);
    }

}
