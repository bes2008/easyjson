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
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import org.codehaus.jettison.util.FastStack;

public class XsonNamespaceContext implements NamespaceContext {
    private FastStack nodes;
    
    public XsonNamespaceContext(FastStack nodes) {
        super();
        this.nodes = nodes;
    }

    public String getNamespaceURI(String prefix) {
        for (Iterator itr = nodes.iterator(); itr.hasNext();){
            Node node = (Node) itr.next();
            String uri = node.getNamespaceURI(prefix);
            
            if (uri != null) {
                return uri;
            }
        }
        return null;
    }

    public String getPrefix(String namespaceURI) {
        for (Iterator itr = nodes.iterator(); itr.hasNext();){
            Node node = (Node) itr.next();
            String prefix = node.getNamespacePrefix(namespaceURI);
            if (prefix != null) {
                return prefix;
            }
        }
        return null;
    }

    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }
     
}
