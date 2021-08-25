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

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract class AbstractXMLStreamReader implements XMLStreamReader {
    protected int event;
    protected Node node;
    
    public boolean isAttributeSpecified(int index) {
        return false;
    }

    public boolean isCharacters() {
        return event == CHARACTERS;
    }

    public boolean isEndElement() {
        return event == END_ELEMENT;
    }

    public boolean isStandalone() {
        return false;
    }

    public boolean isStartElement() {
        return event == START_ELEMENT;
    }

    public boolean isWhiteSpace() {
        return false;
    }

    public int nextTag() throws XMLStreamException {
        int event = next();
        while (event != START_ELEMENT && event != END_ELEMENT) {
            event = next();
        }
        return event;
    }

    public int getEventType() {
        return event;
    }

    public void require(int arg0, String arg1, String arg2) throws XMLStreamException {
    }

    public int getAttributeCount() {
        return node.getAttributes().size();
    }

    public String getAttributeLocalName(int n) {
        return getAttributeName(n).getLocalPart();
    }

    public QName getAttributeName(int n) {
        Iterator itr = node.getAttributes().keySet().iterator();
        QName name = null;
        for (int i = 0; i <= n; i++) {
            name = (QName) itr.next();
        }
        return name;
    }

    public String getAttributeNamespace(int n) {
        return getAttributeName(n).getNamespaceURI();
    }

    public String getAttributePrefix(int n) {
        return getAttributeName(n).getPrefix();
    }

    public String getAttributeValue(int n) {
        Iterator itr = node.getAttributes().values().iterator();
        String name = null;
        for (int i = 0; i <= n; i++) {
            name = (String) itr.next();
        }
        return name;
    }

    public String getAttributeValue(String ns, String local) {
        return (String) node.getAttributes().get(new QName(ns, local));
    }

    public String getAttributeType(int index) {
        return "CDATA";
    }

    public String getLocalName() {
        return getName().getLocalPart();
    }

    public QName getName() {
        return node.getName();
    }

    public String getNamespaceURI() {
        return getName().getNamespaceURI();
    }

    public int getNamespaceCount() {
        return node.getNamespaceCount();
    }

    public String getNamespacePrefix(int n) {
        return node.getNamespacePrefix(n);
    }

    public String getNamespaceURI(int n) {
        return node.getNamespaceURI(n);
    }

    public String getNamespaceURI(String prefix) {
        return node.getNamespaceURI(prefix);
    }

    public boolean hasName() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean hasNext() throws XMLStreamException {
        return event != END_DOCUMENT;
    }

    public boolean hasText() {
        return event == CHARACTERS;
    }

    public boolean standaloneSet() {
        return false;
    }

    public String getCharacterEncodingScheme() {
        return null;
    }

    public String getEncoding() {
        return null;
    }

    public Location getLocation() {
        return new Location() {

            public int getCharacterOffset() {
                return 0;
            }

            public int getColumnNumber() {
                return 0;
            }

            public int getLineNumber() {
                return -1;
            }

            public String getPublicId() {
                return null;
            }

            public String getSystemId() {
                return null;
            }
            
        };
    }

    public String getPIData() {
        return null;
    }

    public String getPITarget() {
        return null;
    }

    public String getPrefix() {
        return getName().getPrefix();
    }

    public Object getProperty(String arg0) throws IllegalArgumentException {
        return null;
    }

    public String getVersion() {
        return null;
    }

    public char[] getTextCharacters() {
    	String text = getText();
        return text != null ? text.toCharArray() : new char[]{};
    }

    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
    	String text = getText();
    	if (text != null) {
            text.getChars(sourceStart,sourceStart+length,target,targetStart);
            return length;
    	} else {
    		return 0;
    	}
    }

    public int getTextLength() {
    	String text = getText();
        return text != null ? text.length() : 0;
    }

    public int getTextStart() {
        return 0;
    }

}
