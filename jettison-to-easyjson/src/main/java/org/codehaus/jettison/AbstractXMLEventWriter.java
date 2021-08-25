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

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * An XMLEventWriter that delegates to an XMLStreamWriter.
 * 
 * @author Thomas.Diesler@jboss.com
 * @since 21-Mar-2008
 */
public class AbstractXMLEventWriter implements XMLEventWriter {
	private XMLStreamWriter streamWriter;

	public AbstractXMLEventWriter(XMLStreamWriter streamWriter) {
		this.streamWriter = streamWriter;
	}

	public void add(XMLEvent event) throws XMLStreamException {
		if (event.isStartDocument()) {
			streamWriter.writeStartDocument();
		} else if (event.isStartElement()) {
			StartElement element = event.asStartElement();
			QName elQName = element.getName();
			if (elQName.getPrefix().length() > 0
					&& elQName.getNamespaceURI().length() > 0)
				streamWriter.writeStartElement(elQName.getPrefix(), elQName
						.getLocalPart(), elQName.getNamespaceURI());
			else if (elQName.getNamespaceURI().length() > 0)
				streamWriter.writeStartElement(elQName.getNamespaceURI(),
						elQName.getLocalPart());
			else
				streamWriter.writeStartElement(elQName.getLocalPart());

			// Add element namespaces
			Iterator namespaces = element.getNamespaces();
			while (namespaces.hasNext()) {
				Namespace ns = (Namespace) namespaces.next();
				String prefix = ns.getPrefix();
				String nsURI = ns.getNamespaceURI();
				streamWriter.writeNamespace(prefix, nsURI);
			}

			// Add element attributes
			Iterator attris = element.getAttributes();
			while (attris.hasNext()) {
				Attribute attr = (Attribute) attris.next();
				QName atQName = attr.getName();
				String value = attr.getValue();
				if (atQName.getPrefix().length() > 0
						&& atQName.getNamespaceURI().length() > 0)
					streamWriter.writeAttribute(atQName.getPrefix(), atQName
							.getNamespaceURI(), atQName.getLocalPart(), value);
				else if (atQName.getNamespaceURI().length() > 0)
					streamWriter.writeAttribute(atQName.getNamespaceURI(),
							atQName.getLocalPart(), value);
				else
					streamWriter.writeAttribute(atQName.getLocalPart(), value);
			}
		} else if (event.isCharacters()) {
			Characters chars = event.asCharacters();
			streamWriter.writeCharacters(chars.getData());
		} else if (event.isEndElement()) {
			streamWriter.writeEndElement();
		} else if (event.isEndDocument()) {
			streamWriter.writeEndDocument();
		} else {
			throw new XMLStreamException("Unsupported event type: " + event);
		}
	}

	public void add(XMLEventReader eventReader) throws XMLStreamException {
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			add(event);
		}
		close();
	}

	public void close() throws XMLStreamException {
		streamWriter.close();
	}

	public void flush() throws XMLStreamException {
		streamWriter.flush();
	}

	public NamespaceContext getNamespaceContext() {
		return streamWriter.getNamespaceContext();
	}

	public String getPrefix(String prefix) throws XMLStreamException {
		return streamWriter.getPrefix(prefix);
	}

	public void setDefaultNamespace(String namespace) throws XMLStreamException {
		streamWriter.setDefaultNamespace(namespace);
	}

	public void setNamespaceContext(NamespaceContext nsContext)
			throws XMLStreamException {
		streamWriter.setNamespaceContext(nsContext);
	}

	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		streamWriter.setPrefix(prefix, uri);
	}
}