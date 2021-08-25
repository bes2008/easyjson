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

import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class AbstractXMLStreamWriter implements XMLStreamWriter {
	
	private ArrayList<String> serializedAsArrays = new ArrayList<String>();
	private boolean arrayKeysWithSlashAvailable;

    public void writeCData(String text) throws XMLStreamException {
        writeCharacters(text);
    }

    public void writeCharacters(char[] arg0, int arg1, int arg2) throws XMLStreamException {
        writeCharacters(new String(arg0, arg1, arg2));
    }
    
    public void writeEmptyElement(String prefix, String local, String ns) throws XMLStreamException {
        writeStartElement(prefix, local, ns);
        writeEndElement();
    }

    public void writeEmptyElement(String ns, String local) throws XMLStreamException {
        writeStartElement(local, ns);
        writeEndElement();
    }

    public void writeEmptyElement(String local) throws XMLStreamException {
        writeStartElement(local);
        writeEndElement();
    }

    public void writeStartDocument(String arg0, String arg1) throws XMLStreamException {
        writeStartDocument();
    }

    public void writeStartDocument(String arg0) throws XMLStreamException {
        writeStartDocument();
    }

    public void writeStartElement(String ns, String local) throws XMLStreamException {
        writeStartElement("", local, ns);
    }

    public void writeStartElement(String local) throws XMLStreamException {
        writeStartElement("", local, "");
    }

    public void writeComment(String arg0) throws XMLStreamException {
    }

    public void writeDTD(String arg0) throws XMLStreamException {
    }

    public void writeEndDocument() throws XMLStreamException {
    }
    
    public void serializeAsArray(String name) {
    	serializedAsArrays.add(name);
    	if (!arrayKeysWithSlashAvailable) {
    	    arrayKeysWithSlashAvailable = name.contains("/");
    	}
    }
    
    /**
     * @deprecated since 1.2 because of misspelling. Use serializeAsArray(String name) instead.
     * @param name the name
     */
    @Deprecated
    public void seriliazeAsArray(String name) {
    	serializedAsArrays.add(name);
    }
    
    public ArrayList<String> getSerializedAsArrays() {
    	return serializedAsArrays;
    }

	public boolean isArrayKeysWithSlashAvailable() {
		return arrayKeysWithSlashAvailable;
	}

	

}
