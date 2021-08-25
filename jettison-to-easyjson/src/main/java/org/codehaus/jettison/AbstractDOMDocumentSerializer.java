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
package org.codehaus.jettison;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMSource;

import org.codehaus.jettison.badgerfish.BadgerFishXMLOutputFactory;
import org.w3c.dom.Element;

/**
 * An abstract JSON DOM serializer
 * 
 * @author Thomas.Diesler@jboss.com
 * @author <a href="mailto:dejan@nighttale.net">Dejan Bosanac</a>
 * @since 21-Mar-2008
 */
public class AbstractDOMDocumentSerializer {
	private OutputStream output;
	private AbstractXMLOutputFactory writerFactory;

	public AbstractDOMDocumentSerializer(OutputStream output, AbstractXMLOutputFactory writerFactory) {
		this.output = output;
		this.writerFactory = writerFactory;
	}

	public void serialize(Element el) throws IOException {
		if (output == null)
			throw new IllegalStateException("OutputStream cannot be null");

		try {
			DOMSource source = new DOMSource(el);
			XMLInputFactory readerFactory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = readerFactory
					.createXMLStreamReader(source);
			XMLEventReader eventReader = readerFactory
					.createXMLEventReader(streamReader);

			XMLEventWriter eventWriter = writerFactory
					.createXMLEventWriter(output);
			eventWriter.add(eventReader);
			eventWriter.close();
		} catch (XMLStreamException ex) {
			IOException ioex = new IOException("Cannot serialize: " + el);
			ioex.initCause(ex);
			throw ioex;
		}
	}
}