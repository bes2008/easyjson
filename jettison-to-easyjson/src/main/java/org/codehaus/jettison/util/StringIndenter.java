/**
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
package org.codehaus.jettison.util;

/**
 * <pre>
 * indent json, assumes the input is not yet indented.  here is an example:
 * {
 * "FastResponseJs":{
 *   "ajaxElements":{
 *     "AjaxMapEntry":[
 *       {
 *         "theKey":{
 *           "@class":"string",
 *           "$":"ajax1a"
 *         },
 *         "theValue":{
 *           "@class":"AjaxEvent",
 *           "ajaxSendFormNames":{
 *             "string":"ajax1"
 *           },
 *           "ajaxEventType":"NORMAL",
 *           "eventName":"onblur",
 *           "ajaxId":"PRLZY5QZ",
 *           "screenElementId":"ajax1a",
 *           "asynchronous":true,
 *           "disableValidation":true
 *         }
 *         
 *       },
 *       {
 *         "theKey":{
 *        ...
 *        
 *        
 *   Usage: String formatted = new StringIndenter(jsonString).result();
 * </pre>
 */
public class StringIndenter {
  
  /** chars to process */
  private String json;
  
  /** current start tag */
  private int startTagIndex;
  
  /** current end tag */
  private int endTagIndex;
  
  /** current number of indents (times to is the indent */
  private int currentNumberOfIndents;
  
  /** result */
  private StringBuilder result;
  
  /**
   * get the result
   * @return the result
   */
  public String result() {
    try {
      this.indent();
    } catch (RuntimeException re) {
      throw new RuntimeException("Problem here: " + this, re);
    }
    if (this.json == null) {
      return null;
    }
    String resultString = this.result.toString();
    return resultString == null ? null : resultString.trim();
  }

  /**
   * indent the string
   */
  private void indent() {
    if (this.json == null) {
      return;
    }
    this.result = new StringBuilder();
    this.startTagIndex = -1;
    this.endTagIndex = -1;
    this.currentNumberOfIndents = 0;
    //{
    //  "a":{
    //    "b\"b":{
    //      "c\\":"d"
    //    },
    //    "e":"f",
    //    "g":[
    //      "h":"i"
    //    ]
    //  }
    //}
    while(true) {
      this.startTagIndex = findStartTagIndex();
      
      if (this.startTagIndex == -1) {
        //cant find anything else...  make sure everything there
        if (this.endTagIndex != this.json.length()-1) {
          this.result.append(this.json, this.endTagIndex+1, this.json.length());
        }
        break;
      }
      
      //handles first tag
      if (instantIndent(this.json, this.startTagIndex)) {
        this.currentNumberOfIndents++;
        this.printNewlineIndent(this.startTagIndex, this.startTagIndex+1);
        this.endTagIndex = this.startTagIndex;
        continue;
      }
      
      //handles end of associative array with comma
      if (instantUnindentTwoChars(this.json, this.startTagIndex)) {
        this.currentNumberOfIndents--;
        //this is on a line by itself
        this.newlineIndent();
        this.printNewlineIndent(this.startTagIndex, this.startTagIndex+2);
        this.endTagIndex = this.startTagIndex+1;
        continue;
      }
      
      //handles end of array with comma
      if (instantUnindent(this.json, this.startTagIndex)) {
        this.currentNumberOfIndents--;
        if (onNewline()) {
          this.unindent();
        } else {
          this.newlineIndent();
        }
        this.printNewlineIndent(this.startTagIndex, this.startTagIndex+1);
        this.endTagIndex = this.startTagIndex;
        continue;
      }
      
      //handles end of array with comma
      if (instantNewline(this.json, this.startTagIndex)) {
        this.printNewlineIndent(this.startTagIndex, this.startTagIndex+1);
        this.endTagIndex = this.startTagIndex;
        continue;
      }
      
      this.endTagIndex = findEndTagIndex();
      
      //one thing's for sure, we are printing out this tag
      this.result.append(this.json, this.startTagIndex, this.endTagIndex+1);
      
      //go back to top to end
      if (this.endTagIndex >= this.json.length()-1) {
        continue;
      }
      char nextChar = this.json.charAt(this.endTagIndex+1);
      //if next is colon, print that out too
      if (nextChar == ':') {
        this.result.append(':');
        this.endTagIndex++;
      }
      
      //ready to loop around...
    }
  }
  
  /**
   * see if current pos is on newline
   * @return true if on new line
   */
  private boolean onNewline() {
    for (int i=this.result.length()-1;i>=0;i--) {
      char curChar = this.result.charAt(i);
      if (curChar == '\n') {
        return true;
      }
      if (Character.isWhitespace(curChar)) {
        continue;
      }
      //if not whitespace, then not on own line
      return false;
    }
    //i guess first line is new line
    return true;
  }
  
  /**
   * see if instant indent
   * @param json
   * @param index
   * @return if it is an instant indent
   */
  private static boolean instantIndent(String json, int index) {
    char curChar = json.charAt(index);
    if (curChar == '{' || curChar == '[') {
      return true;
    }
    return false;
  }
  
  /**
   * see if instant indent
   * @param json
   * @param index
   * @return if it is an instant indent
   */
  private static boolean instantNewline(String json, int index) {
    char curChar = json.charAt(index);
    if (curChar == ',') {
      return true;
    }
    return false;
  }
  
  /**
   * see if instant unindent
   * @param json
   * @param index
   * @return if it is an instant unindent
   */
  private static boolean instantUnindent(String json, int index) {
    char curChar = json.charAt(index);
    if (curChar == '}' || curChar == ']') {
      return true;
    }
    return false;
  }
  
  /**
   * see if instant indent
   * @param json
   * @param index
   * @return if it is an instant indent
   */
  private static boolean instantUnindentTwoChars(String json, int index) {
    char curChar = json.charAt(index);
    if (index == json.length()-1) {
      return false;
    }
    char nextchar = json.charAt(index+1);
    if (curChar == '}' && nextchar == ',') {
      return true;
    }
    return false;
  }
  
  /**
   * put a newline and indent
   * @param start
   * @param end
   */
  private void printNewlineIndent(int start, int end) {
    //lets put this tag on the queue
    this.result.append(this.json, start, end);
    this.newlineIndent();
    
  }

  /**
   * put a newline and indent
   */
  private void newlineIndent() {
    this.result.append("\n").append(repeat("  ", this.currentNumberOfIndents));
  }

  /**
   * repeat a string a certain number of times.
   * @param theString
   * @param times
   * @return the string
   */
  private static String repeat(String theString, int times) {
    StringBuilder result = new StringBuilder();
    //loop through, keep appending
    for (int i = 0; i < times; i++) {
      result.append(theString);
    }
    return result.toString();

  }
  
  /**
   * unindent a previous indent if it is there
   */
  private void unindent() {
    for (int i=0;i<2;i++) {
      if (this.result.charAt(this.result.length()-1) == ' ') {
        this.result.deleteCharAt(this.result.length()-1);
      }
    }
  }
  
  /**
   * after the last end tag, find the next start tag
   * @return the next start tag
   */
  private int findStartTagIndex() {
    return findNextStartTagIndex(this.json, this.endTagIndex+1);
  }

  /**
   * after the last start tag, find the next end start tag
   * @return the next start tag
   */
  private int findEndTagIndex() {
    return findNextEndTagIndex(this.json, this.startTagIndex+1);
  }

  /**
   * find the start tag from json and a start from index
   * either look for a quote, {, [ or scalar.  generally not whitespace
   * @param json
   * @param startFrom
   * @return the start tag index of -1 if not found another
   */
  private static int findNextStartTagIndex(String json, int startFrom) {
    int length = json.length();
    for (int i= startFrom; i<length;i++) {
      char curChar = json.charAt(i);
      if (Character.isWhitespace(curChar)) {
        continue;
      }
      return i;
    }
    return -1;
  }
  
  /**
   * find the end tag from json and a start from index
   * @param json
   * @param startFrom is the char after the start of tag
   * @return the start tag index of -1 if not found another
   */
  private static int findNextEndTagIndex(String json, int startFrom) {
    int length = json.length();
    
    //see if quoted string
    boolean quotedString = json.charAt(startFrom-1) == '\"';
    
    int ignoreSlashInIndex = -1;
    boolean afterSlash = false;
    for (int i= startFrom; i<length;i++) {
      //we are after a slash, if not ignored, and if last was slash
      afterSlash = i != ignoreSlashInIndex && i!= startFrom && json.charAt(i-1) == '\\';
      char curChar = json.charAt(i);
      
      //if first slash, ignore slash in next index
      if (!afterSlash && curChar == '\\') {
        ignoreSlashInIndex = i+2;
      }
      
      if (!quotedString) {
        if (curChar == ':' || Character.isWhitespace(curChar)
            || curChar == ']' || curChar == '}'
              || curChar == ',') {
          return i-1;
          
        }
      } else {
        if (!afterSlash && curChar == '\"') {
          return i;
        }
      }
    }
    //end at end of string
    return json.length()-1;
  }
  
  /**
   * @param theJson is the json to format
   * indenter
   */
  public StringIndenter(String theJson) {
    this.json = theJson == null ? "" : theJson.trim();
  }
}
