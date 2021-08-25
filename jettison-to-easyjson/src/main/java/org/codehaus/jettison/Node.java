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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class Node {
    
    JSONObject object;
    Map attributes;
    Map namespaces;
    Iterator keys;
    QName name;
    JSONArray array;
    int arrayIndex;
    String currentKey;
    Node parent;
    
    public Node(Node parent, String name, JSONObject object, Convention con) 
        throws JSONException, XMLStreamException {
        this.parent = parent;
        this.object = object;
        
        /* Should really use a _Linked_ HashMap to preserve
         * ordering (insert order) -- regular one has arbitrary ordering.
         * But there are some funky dependencies within unit tests
         * that will fail right now, need to investigate that bit more
         */
        this.namespaces = new LinkedHashMap();
        this.attributes = new LinkedHashMap();
        
        con.processAttributesAndNamespaces(this, object);
        
        keys = object.keys();

        this.name = con.createQName(name, this);
    }

    public Node(String name, Convention con) throws XMLStreamException {
        this.name = con.createQName(name, this);
        this.namespaces = new HashMap();
        this.attributes = new HashMap();
    }

    public Node(JSONObject object) {
        this.object = object;
        this.namespaces = new HashMap();
        this.attributes = new HashMap();
    }

    public int getNamespaceCount() {
        return namespaces.size();
    }

    public String getNamespaceURI(String prefix) {
        String result = (String) namespaces.get(prefix);
        if (result == null && parent != null) {
            result = parent.getNamespaceURI(prefix);
        }
        return result;
    }

    public String getNamespaceURI(int index) {
        if (index < 0 || index >= getNamespaceCount()) {
            throw new IllegalArgumentException("Illegal index: element has "+getNamespaceCount()+" namespace declarations");
        }
        Iterator itr = namespaces.values().iterator();
        while (--index >= 0) {
            itr.next();
        }
        Object ns = itr.next();
        return ns == null ? "" : ns.toString();
    }

    public String getNamespacePrefix(String URI) {
        String result = null;
        for (Iterator nsItr = namespaces.entrySet().iterator(); nsItr.hasNext();) {
            Map.Entry e = (Map.Entry) nsItr.next();
            if (e.getValue().equals(URI)) {
                result = (String) e.getKey();
            }
        }
        if (result == null && parent != null) {
            result = parent.getNamespacePrefix(URI);
        }
        return result;
    }

    public String getNamespacePrefix(int index) {
        if (index < 0 || index >= getNamespaceCount()) {
            throw new IllegalArgumentException("Illegal index: element has "+getNamespaceCount()+" namespace declarations");
        }
        Iterator itr = namespaces.keySet().iterator();
        while (--index >= 0) {
            itr.next();
        }
        return itr.next().toString();
    }

    public void setNamespaces(Map namespaces) {
        this.namespaces = namespaces;
    }

    public void setNamespace(String prefix, String uri) {
        namespaces.put(prefix, uri);
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttribute(QName name, String value) {
        attributes.put(name, value);
    }

    public Iterator getKeys() {
        return keys;
    }

    public QName getName() {
        return name;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }
    
    public String toString() {
		if (this.name != null) {
			return this.name.toString();
		} else {
			return super.toString();
		}
	}
    
}
