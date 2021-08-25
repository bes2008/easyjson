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

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;

import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MappedXMLStreamWriter extends AbstractXMLStreamWriter {
	private static final String MIXED_CONTENT_VALUE_KEY = "$";
	private MappedNamespaceConvention convention;
	protected Writer writer;
	private NamespaceContext namespaceContext;
	/**
	 * What key is used for text content, when an element has both text and
	 * other content?
	 */
	private String valueKey = MIXED_CONTENT_VALUE_KEY;
	/** Stack of open elements. */
	private Stack<JSONProperty> stack = new Stack<JSONProperty>();
	/** Element currently being processed. */
	private JSONProperty current;

	/**
	 * JSON property currently being constructed. For efficiency, this is
	 * concretely represented as either a property with a String value or an
	 * Object value.
	 */
	private abstract class JSONProperty {
		private String key;
		private String parentKey;
		public JSONProperty(String key, String parentKey) {
			this.key = key;
			this.parentKey = parentKey;
		}
		/** Get the key of the property. */
		public String getKey() {
			return key;
		}
		public String getParentKey() {
			return parentKey;
		}
		public String getTreeKey() {
			return parentKey == null ? key : parentKey + "/" + key;
		}
		/** Get the value of the property */
		public abstract Object getValue();
		/** Add text */
		public abstract void addText(String text);
		/** Return a new property object with this property added */
		public abstract JSONPropertyObject withProperty(JSONProperty property, boolean add);
		public JSONPropertyObject withProperty(JSONProperty property) {
			return withProperty(property, true);
		}
	}

	/**
	 * Property with a String value.
	 */
	private final class JSONPropertyString extends JSONProperty {
		private StringBuilder object = new StringBuilder();
		public JSONPropertyString(String key, String parentKey) {
			super(key, parentKey);
		}
		public Object getValue() {
			return object.toString();
		}
		public void addText(String text) {
			object.append(text);
		}
		public JSONPropertyObject withProperty(JSONProperty property, boolean add) {
			// Duplicate some code from JSONPropertyObject
			// because we can do things with fewer checks, and
			// therefore more efficiently.
			JSONObject jo = new JSONObject(false,
					                       convention.getIgnoredElements(),
					                       convention.isWriteNullAsString(),
					                       convention.isEscapeForwardSlashAlways());
			try {
				// only add the text property if it's non-empty
				String strValue = getValue().toString();
				if (MIXED_CONTENT_VALUE_KEY == valueKey) {
					strValue = strValue.trim();
				}
				if (strValue.length() > 0) {
					jo.put(valueKey, strValue);
				}
				
				Object value = property.getValue();
				boolean emptyString = value instanceof String && ((String)value).isEmpty();
				if (value instanceof String && !emptyString) {
				    value = convention.convertToJSONPrimitive((String)value);
				}
				if (getSerializedAsArrays().contains(getPropertyArrayKey(property))) {
				    JSONArray values = new JSONArray();
				    if (!convention.isIgnoreEmptyArrayValues() 
				    		|| (!emptyString && value != null)) {
				    	values.put(value);
				    }
					value = values;
				    
				}
				jo.put(property.getKey(), value);
			} catch (JSONException e) {
				// Impossible by construction
				throw new AssertionError(e);				
			}
			return new JSONPropertyObject(getKey(), getParentKey(), jo);
		}
	}

	/**
	 * Property with a JSONObject value.
	 */
	private final class JSONPropertyObject extends JSONProperty {
	    private JSONObject object;
	    public JSONPropertyObject(String key, String parentKey, JSONObject object) {
	        super(key, parentKey);
	        this.object = object;
	    }
	    public Object getValue() {
	        return object;
	    }
	    public void addText(String text) {
	    	if (MIXED_CONTENT_VALUE_KEY == valueKey) {
	    		text = text.trim();
	    		if (text.length() == 0) {
	    			return;
	    		}
	    	}
	        try {
	            text = object.getString(valueKey) + text;
	        } catch (JSONException e) {
	            // no existing text, that's fine
	        }
	        try {
	        	if (valueKey != null) {
	        		object.put(valueKey, text);
	        	}
	        } catch (JSONException e) {
	            // Impossible by construction
	            throw new AssertionError(e);
	        }
	    }
	    public JSONPropertyObject withProperty(JSONProperty property, boolean add) {
	        Object value = property.getValue();
	        if(value instanceof String && !((String)value).isEmpty()) {
	            value = convention.convertToJSONPrimitive((String)value);
	        }
	        Object old = object.opt(property.getKey());
	        try {
	            if(old != null) {
	                JSONArray values;
	                // Convert an existing property to an array
	                // and append to the array
	                if (old instanceof JSONArray) {
	                    values = (JSONArray)old;
	                } else {
	                    values = new JSONArray();
	                    values.put(old);
	                }
	                values.put(value);

	                object.put(property.getKey(), values);
	            } else if(getSerializedAsArrays().contains(getPropertyArrayKey(property))) {
	                JSONArray values = new JSONArray();
	                boolean emptyString = value instanceof String && ((String)value).isEmpty();
	                if (!convention.isIgnoreEmptyArrayValues()  
				    		|| (!emptyString && value != null)) {
				    	values.put(value);
				    }
	                object.put(property.getKey(), values);
	            } else {
	                // Add the property directly.
	                object.put(property.getKey(), value);
	            }
	        } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
                return this;
	    }
	}
	
	public MappedXMLStreamWriter(MappedNamespaceConvention convention, Writer writer) {
		super();
		this.convention = convention;
		this.writer = writer;
		this.namespaceContext = convention;
	}

	private String getPropertyArrayKey(JSONProperty property) {
		return isArrayKeysWithSlashAvailable()  ? property.getTreeKey() : property.getKey(); 
	}
	
	public NamespaceContext getNamespaceContext() {
		return namespaceContext;
	}

	public void setNamespaceContext(NamespaceContext context)
			throws XMLStreamException {
		this.namespaceContext = context;
	}

	public String getTextKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public void writeStartDocument() throws XMLStreamException {
		// The document is an object with one property -- the root element
		current = new JSONPropertyObject(null, 
				                         null,
		    new JSONObject(convention.isDropRootElement(), 
		    		       convention.getIgnoredElements(),
		    		       convention.isWriteNullAsString(),
		    		       convention.isEscapeForwardSlashAlways()));
		stack.clear();
	}
	
	public void writeStartElement(String prefix, String local, String ns) throws XMLStreamException {
		String parentKey = current.getTreeKey();
		stack.push(current);
		String key = convention.createKey(prefix, ns, local);
		current = new JSONPropertyString(key, parentKey);
	}
	
	public void writeAttribute(String prefix, String ns, String local, String value) throws XMLStreamException {
		String key = convention.isElement(prefix, ns, local)
			? convention.createKey(prefix, ns, local)
			: convention.createAttributeKey(prefix, ns, local);
		JSONPropertyString prop = new JSONPropertyString(key, null);
		prop.addText(value);
		current = current.withProperty(prop, false);
	}

	public void writeAttribute(String ns, String local, String value) throws XMLStreamException {
		writeAttribute(null, ns, local, value);
	}

	public void writeAttribute(String local, String value) throws XMLStreamException {
		writeAttribute(null, local, value);
	}

	public void writeCharacters(String text) throws XMLStreamException {
	    current.addText(text);
	}
	
	public void writeEndElement() throws XMLStreamException {
		if (stack.isEmpty()) throw new XMLStreamException("Too many closing tags.");
		current = stack.pop().withProperty(current);
	}

	public void writeEndDocument() throws XMLStreamException {
		if (!stack.isEmpty()) throw new XMLStreamException("Missing some closing tags.");
		// We know the root is a JSONPropertyObject so this cast is safe
		writeJSONObject((JSONObject)current.getValue());
		try {
			writer.flush();
		} catch (IOException e) {
			throw new XMLStreamException(e);
		}
	}
	
	/**
	 * For clients who want to modify the output object before writing to override.
     * @param root root
     * @throws XMLStreamException XMLStreamException
	 */
	protected void writeJSONObject(JSONObject root) throws XMLStreamException {
		try {
			if (root == null) writer.write("null");
			else root.write(writer);
		} catch (JSONException e) {
			throw new XMLStreamException(e);
		} catch (IOException e) {
			throw new XMLStreamException(e);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// The following methods are supplied only to satisfy the interface

	public void close() throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void flush() throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public String getPrefix(String arg0) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProperty(String arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDefaultNamespace(String arg0) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void setPrefix(String arg0, String arg1) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void writeDefaultNamespace(String arg0) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void writeEntityRef(String arg0) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void writeNamespace(String arg0, String arg1) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void writeProcessingInstruction(String arg0) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

	public void writeProcessingInstruction(String arg0, String arg1) throws XMLStreamException {
		// TODO Auto-generated method stub

	}
	public MappedNamespaceConvention getConvention() {
		return convention;
	}
}