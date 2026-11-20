/* Derived from gwt-dnd styles, Copyright 2009 Fred Sauer.
 * Modified by Instanto, 2026: embed styles for both compilers; remove global HTML reset.
 * Licensed under the Apache License, Version 2.0. See LICENSE.
 */
package com.allen_sauer.gwt.dnd.client.util;

import com.google.gwt.dom.client.StyleInjector;

public final class BundledDragResources implements DragClientBundle {
  private static final DragCssResource CSS =
      new DragCssResource() {
        private boolean injected;

        public String getName() {
          return "tea-dnd";
        }

        public String getText() {
          return
"""
















.dragdrop-boundary {
}

.dragdrop-dropTarget-engage {
}

.dragdrop-dropTarget {
}

.dragdrop-handle {
\tcursor: move;
\tuser-select: none;
\t-khtml-user-select: none;
\t-moz-user-select: none;
}

.dragdrop-draggable {
\tzoom: 1; /* IE gain hasLayout */
}

.dragdrop-dragging {
\tzoom: normal; /* remove artifacts */
}

.dragdrop-positioner {
\tborder: 1px dashed #1E90FF; /* blue */
\tmargin: 0px !important;
\tzoom: 1; /* IE gain hasLayout */
\tz-index: 100;
}

.dragdrop-flow-panel-positioner {
\tcolor: #1E90FF; /* blue */
\tdisplay: inline;
\ttext-align: center;
\tvertical-align: middle;
}

.dragdrop-proxy {
\tbackground-color: #77AAFF; /* light blue */
}

.dragdrop-selected,.dragdrop-dragging,.dragdrop-proxy {
\t-webkit-filter: opacity(.3);
\t-moz-opacity: 0.3;
\t-khtml-opacity: 0.3;
\t-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=30)";
\topacity: 0.3;
}

.dragdrop-movable-panel {
\tz-index: 200;
\tmargin: 0px !important;
\tborder: none !important;
}
""";
        }

        public boolean ensureInjected() {
          if (injected) return false;
          StyleInjector.injectAtStart(getText());
          injected = true;
          return true;
        }

        public String boundary() {
          return "dragdrop-boundary";
        }

        public String draggable() {
          return "dragdrop-draggable";
        }

        public String dragging() {
          return "dragdrop-dragging";
        }

        public String dropTarget() {
          return "dragdrop-dropTarget";
        }

        public String dropTargetEngage() {
          return "dragdrop-dropTarget-engage";
        }

        public String flowPanelPositioner() {
          return "dragdrop-flow-panel-positioner";
        }

        public String handle() {
          return "dragdrop-handle";
        }

        public String movablePanel() {
          return "dragdrop-movable-panel";
        }

        public String positioner() {
          return "dragdrop-positioner";
        }

        public String proxy() {
          return "dragdrop-proxy";
        }

        public String selected() {
          return "dragdrop-selected";
        }
      };

  public DragCssResource css() {
    return CSS;
  }
}
