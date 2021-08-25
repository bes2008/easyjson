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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;

/**
 * An abstract JSON DOM parser
 * 
 * @author Thomas.Diesler@jboss.com
 * @author <a href="mailto:dejan@nighttale.net">Dejan Bosanac</a>
 * @since 21-Mar-2008
 */
public class AbstractDOMDocumentParser {
	
	private AbstractXMLInputFactory inputFactory;
	
	protected AbstractDOMDocumentParser(AbstractXMLInputFactory inputFactory) {
		this.inputFactory = inputFactory;
	}
	
	public Document parse(InputStream input) throws IOException {
		try {
	         XMLStreamReader streamReader = inputFactory.createXMLStreamReader(input);
	         XMLInputFactory readerFactory = XMLInputFactory.newInstance();
	         XMLEventReader eventReader = readerFactory.createXMLEventReader(streamReader);
	         
	         // Can not create a STaX writer for a DOMResult in woodstox-3.1.1
	         /*XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			 Document nsDom = getDocumentBuilder().newDocument();
			 DOMResult result = new DOMResult(nsDom);
	         XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(result);*/
	         
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	         XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(baos);
	         
	         eventWriter.add(eventReader);
	         eventWriter.close();
	         
	         // This parsing step should not be necessary, if we could output to a DOMResult
	         ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	         return getDocumentBuilder().parse(bais);
	         
	         //return nsDom;
			
		} catch (Exception ex) {
			IOException ioex = new IOException("Cannot parse input stream");
			ioex.initCause(ex);
			throw ioex;
		}
	}

	private DocumentBuilder getDocumentBuilder() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Failed to create DocumentBuilder", e);
		}
	}
}