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

import java.io.OutputStream;

import org.codehaus.jettison.AbstractDOMDocumentSerializer;

/**
 * JSON BadgeFish DOM serializer
 * 
 * @author Thomas.Diesler@jboss.com
 * @author <a href="mailto:dejan@nighttale.net">Dejan Bosanac</a>
 * @since 21-Mar-2008
 */
public class BadgerFishDOMDocumentSerializer extends
		AbstractDOMDocumentSerializer {

	public BadgerFishDOMDocumentSerializer(OutputStream output) {
		super(output, new BadgerFishXMLOutputFactory());
	}
	
}
