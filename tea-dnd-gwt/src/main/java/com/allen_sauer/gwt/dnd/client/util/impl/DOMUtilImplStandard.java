// Modified by Instanto, 2026: maintained source formatting for Tea DnD.
/*
 * Copyright 2009 Fred Sauer
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allen_sauer.gwt.dnd.client.util.impl;

import com.google.gwt.dom.client.Element;

/** {@link com.allen_sauer.gwt.dnd.client.util.DOMUtil} implementation for standard browsers. */
public abstract class DOMUtilImplStandard extends DOMUtilImpl {

  // CHECKSTYLE_JAVADOC_OFF

  @Override
  public String adjustTitleForBrowser(String message) {
    return message.replaceAll("\r\n|\r|\n", " / ");
  }

  @Override
  public void cancelAllDocumentSelections() {
    cancelAllDocumentSelectionsNative();
  }

  private static native void cancelAllDocumentSelectionsNative() /*-{
    try {
      $wnd.getSelection().removeAllRanges();
    } catch(e) { throw new Error("unselect exception:\n" + e); }
  }-*/;

  @Override
  public int getBorderLeft(Element elem) {
    return getBorderLeftNative(elem);
  }

  private static native int getBorderLeftNative(Element elem) /*-{
    try {
      var computedStyle = $doc.defaultView.getComputedStyle(elem, null);
      var borderLeftWidth = computedStyle.getPropertyValue("border-left-width");
      return borderLeftWidth.indexOf("px") == -1 ? 0 : parseInt(borderLeftWidth.substr(0, borderLeftWidth.length - 2));
    } catch(e) { throw new Error("getBorderLeft exception:\n" + e); }
  }-*/;

  @Override
  public int getBorderTop(Element elem) {
    return getBorderTopNative(elem);
  }

  private static native int getBorderTopNative(Element elem) /*-{
    try {
      var computedStyle = $doc.defaultView.getComputedStyle(elem, null);
      var borderTopWidth = computedStyle.getPropertyValue("border-top-width");
      return borderTopWidth.indexOf("px") == -1 ? 0 : parseInt(borderTopWidth.substr(0, borderTopWidth.length - 2));
    } catch(e) { throw new Error("getBorderTop: " + e); }
  }-*/;

  @Override
  public int getClientHeight(Element elem) {
    return getClientHeightNative(elem);
  }

  private static native int getClientHeightNative(Element elem) /*-{
    try {
      return elem.clientHeight;
    } catch(e) { throw new Error("getClientHeight exception:\n" + e); }
  }-*/;

  @Override
  public int getClientWidth(Element elem) {
    return getClientWidthNative(elem);
  }

  private static native int getClientWidthNative(Element elem) /*-{
    try {
      return elem.clientWidth;
    } catch(e) { throw new Error("getClientWidth exception:\n" + e); }
  }-*/;
}
