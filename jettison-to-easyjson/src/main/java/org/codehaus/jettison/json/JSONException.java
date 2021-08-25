/*
Copyright (c) 2002 JSON.org

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.codehaus.jettison.json;

/**
 * The JSONException is thrown by the JSON.org classes then things are amiss.
 * @author JSON.org
 * @version 2
 */
public class JSONException extends Exception {
    private Throwable cause;
    private int line = -1;
    private int column = -1;
    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public JSONException(String message) {
        super(message);
    }
    
    public JSONException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }

    public JSONException(Throwable t) {
        super(t.getMessage(), t);
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }

	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}

}
