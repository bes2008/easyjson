/**
 * Copyright 2006 Envoi Solutions LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.jettison.mapped;

import java.io.Writer;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jettison.AbstractXMLOutputFactory;


public class MappedXMLOutputFactory extends AbstractXMLOutputFactory {

    private MappedNamespaceConvention convention;

    public MappedXMLOutputFactory(Map<?, ?> nstojns) {
        this(new Configuration(nstojns));
    }

    public MappedXMLOutputFactory(Configuration config) {
        this.convention = new MappedNamespaceConvention(config);
    }

    public XMLStreamWriter createXMLStreamWriter(Writer writer) throws XMLStreamException {
        return new MappedXMLStreamWriter(convention, writer);
    }
}
