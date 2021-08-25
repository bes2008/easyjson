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

import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;

import org.codehaus.jettison.AbstractXMLStreamReader;
import org.codehaus.jettison.Node;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.util.FastStack;

public class MappedXMLStreamReader extends AbstractXMLStreamReader {
    private FastStack nodes;
    private String currentValue;
    private MappedNamespaceConvention convention;
    private String valueKey = "$";
    private NamespaceContext ctx;
    private int popArrayNodes;
    public MappedXMLStreamReader(JSONObject obj)
            throws JSONException, XMLStreamException {
        this(obj, new MappedNamespaceConvention());
    }

    public MappedXMLStreamReader(JSONObject obj, MappedNamespaceConvention con)
            throws JSONException, XMLStreamException {
        String rootName = (String) obj.keys().next();

        this.convention = con;
        this.nodes = new FastStack();
        this.ctx = con;
        Object top = obj.get(rootName);
        if (top instanceof JSONObject) {
            this.node = new Node(null, rootName, (JSONObject)top, convention);
        } else if (top instanceof JSONArray && !(((JSONArray)top).length() == 1 && ((JSONArray)top).get(0).equals(""))) {
            this.node = new Node(null, rootName, obj, convention);
        } else {
            node = new Node(rootName, convention);
            convention.processAttributesAndNamespaces(node, obj);
            currentValue = JSONObject.NULL.equals(top) ? null : top.toString();
        }
        nodes.push(node);
        event = START_DOCUMENT;
    }


    public int next() throws XMLStreamException {
        if (event == START_DOCUMENT) {
            event = START_ELEMENT;
        } else if (event == CHARACTERS) {
            event = END_ELEMENT;
            node = (Node) nodes.pop();
            currentValue = null;
        } else if (event == START_ELEMENT || event == END_ELEMENT) {
            if (event == END_ELEMENT && nodes.size() > 0) {
                node = (Node) nodes.peek();
                if (popArrayNodes > 0) {
                	nodes.pop();
                	if (node.getArray() != null) {
                		popArrayNodes--;
                		event = END_ELEMENT;
                		return event;
                	}
                }
            }
            if (currentValue != null) {
                event = CHARACTERS;
            } else if ((node.getKeys() != null && node.getKeys().hasNext()) || node.getArray() != null) {
                processElement();
            } else {
                if (nodes.size() > 0) {
                    event = END_ELEMENT;
                    node = (Node) nodes.pop();
                } else {
                    event = END_DOCUMENT;
                }
            }
        }
        // handle value in nodes with attributes
        if (nodes.size() > 0) {
        	Node next = (Node)nodes.peek();
        	if (event == START_ELEMENT && next.getName().getLocalPart().equals(valueKey)) {
        		event = CHARACTERS;
        		node = (Node)nodes.pop();
        	}
        }
        return event;
    }

    private void processElement() throws XMLStreamException {
        try {
            Object newObj = null;
            String nextKey = null;
            if (node.getArray() != null) {
                int index = node.getArrayIndex();
                if (index >= node.getArray().length()) {
                	
            		nodes.pop();

                	node = (Node) nodes.peek();

                	if (node == null)
                	{
                		event = END_DOCUMENT;
                		return;
                	}
                    
                    if ((node.getKeys() != null && node.getKeys().hasNext()) || node.getArray() != null) {
                        if (popArrayNodes > 0) {
                        	node = (Node) nodes.pop();
                        }
                    	processElement();
                    }
                    else {
                    	    event = END_ELEMENT;
                            node = (Node) nodes.pop();
                    }
                    return;
                }
                newObj = node.getArray().get(index++);
                nextKey = node.getName().getLocalPart();
                if (!"".equals(node.getName().getNamespaceURI())) {
                    nextKey = this.convention.getPrefix(node.getName().getNamespaceURI()) 
                    		+ this.getConvention().getNamespaceSeparator() + nextKey;
                }
                node.setArrayIndex(index);
            } else {
                nextKey = (String) node.getKeys().next();
                newObj = node.getObject().get(nextKey);
            }
            if (newObj instanceof String) {
                node = new Node(nextKey, convention);
                nodes.push(node);
                currentValue = (String) newObj;
                event = START_ELEMENT;
                return;
            } else if (newObj instanceof JSONArray) {
            	JSONArray array = (JSONArray) newObj;
                if (!processUniformArrayIfPossible(nextKey, array)) {
	            	node = new Node(nextKey, convention);
	                node.setArray(array);
	                node.setArrayIndex(0);
	                nodes.push(node);
                    processElement(); 
                }
                return;
            } else if (newObj instanceof JSONObject) {
                node = new Node((Node)nodes.peek(), nextKey, (JSONObject) newObj, convention);
                nodes.push(node);
                event = START_ELEMENT;
                return;
            } else {
                node = new Node(nextKey, convention);
                nodes.push(node);
                currentValue = JSONObject.NULL.equals(newObj) ? null : newObj.toString();
                event = START_ELEMENT;
                return;
            }
        } catch (JSONException e) {
            throw new XMLStreamException(e);
        }
    }

    private boolean processUniformArrayIfPossible(String arrayKey, JSONArray array) throws JSONException, XMLStreamException {
    	
    	if (!isAvoidArraySpecificEvents(arrayKey)) {
    		return false;
    	}
    	
    	int arrayLength = array.length();
    	int depth = 0;
    	String lastKey = null;
    	int parentIndex = nodes.size();
    	boolean isRoot = ((Node)nodes.get(0)).getName().getLocalPart().equals(arrayKey);
    	Node parent = !isRoot ? new Node(arrayKey, convention) : node;
        
    	for (int i = arrayLength - 1; i >= 0; i--) {
    		Object object = array.get(i);
    		if (object instanceof JSONObject) {
    			JSONObject jsonObject = (JSONObject)object;
    			// lets limit to single key JSONObjects for now
    			if (jsonObject.length() == 1) { 
    				String theKey = jsonObject.keys().next().toString();
    				if (lastKey == null || lastKey.equals(theKey)) {
    					lastKey = theKey;
    					depth++;
    					Node theNode = new Node(parent, theKey, jsonObject, convention);
    	                nodes.push(theNode);
    	            } else {
    					lastKey = null;
    					break;
    				}
    			}
    		}
    	}
    	if (lastKey == null) {
    		for (int i = 0; i < depth; i++) {
    			nodes.pop();
    		}
    		return false;
    	}
    	
    	parent.setArray(array);
        parent.setArrayIndex(arrayLength);
        if (!isRoot) {
            nodes.add(parentIndex, parent);
            nodes.push(parent);
            node = parent;
            event = START_ELEMENT;
        } else {
        	node = (Node)nodes.pop();
        	processElement();
        }
        popArrayNodes++;
        return true;
    }
    
    public void close() throws XMLStreamException {
    }

    public String getElementText() throws XMLStreamException {
        event = CHARACTERS;
        return currentValue;
    }

    public NamespaceContext getNamespaceContext() {
        return ctx;
    }

    public String getText() {
    	if (currentValue != null 
    		&& "null".equals(currentValue)
    		&& !convention.isReadNullAsString()) {
    		return null;
    	}
        return currentValue;
    }
    
	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public boolean isAvoidArraySpecificEvents(String key) {
		Set<?> keys = convention.getPrimitiveArrayKeys();
		return keys != null && keys.contains(key);
	}
	public MappedNamespaceConvention getConvention() {
		return convention;
	}
}
