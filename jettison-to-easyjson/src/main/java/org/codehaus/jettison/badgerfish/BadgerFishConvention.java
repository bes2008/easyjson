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
package org.codehaus.jettison.badgerfish;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.codehaus.jettison.Convention;
import org.codehaus.jettison.Node;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BadgerFishConvention implements Convention {

    public BadgerFishConvention() {
        super();
    }
    
    public void processAttributesAndNamespaces(Node n, JSONObject object) 
        throws JSONException, XMLStreamException {
        // Read in the attributes, and stop when there are no more
        for (Iterator itr = object.keys(); itr.hasNext();) {
            String k = (String) itr.next();
            
            if (k.startsWith("@")) {
                Object o = object.opt(k);
                k = k.substring(1);
                if (k.equals("xmlns")) {
                    // if its a string its a default namespace
                    if (o instanceof JSONObject) {
                        JSONObject jo = (JSONObject) o;
                        for (Iterator pitr = jo.keys(); pitr.hasNext(); ) {
                            String prefix = (String) pitr.next();
                            String uri = jo.getString(prefix);

                            if (prefix.equals("$")) {
                                prefix = "";
                            }
                            
                            n.setNamespace(prefix, uri);
                        }                        
                    }
                } else {
                    String strValue = (String) o; 
                    QName name = null;
                    // note that a non-prefixed attribute name implies NO namespace,
                    // i.e. as opposed to the in-scope default namespace
                    if (k.contains(":")) {
                        name = createQName(k, n);
                    } else {
                        name = new QName(XMLConstants.DEFAULT_NS_PREFIX, k);
                    }                    
                    n.setAttribute(name, strValue);
                }
                itr.remove();
            }
        }
    }
    
    public QName createQName(String rootName, Node node) throws XMLStreamException {
        int idx = rootName.indexOf(':');
        if (idx != -1) {
            String prefix = rootName.substring(0, idx);
            String local = rootName.substring(idx+1);
            
            String uri = (String) node.getNamespaceURI(prefix);
            if (uri == null) {
                throw new XMLStreamException("Invalid prefix " + prefix 
                                             + " on element " + rootName);
            }
            
            return new QName(uri, local, prefix);
        }
        
        String uri = (String) node.getNamespaceURI("");
        if (uri != null) {
            return new QName(uri, rootName);
        }
        
        return new QName(rootName);
    }
}
