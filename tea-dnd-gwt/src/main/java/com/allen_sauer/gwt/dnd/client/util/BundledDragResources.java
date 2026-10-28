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
	cursor: move;
	user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
}

.dragdrop-draggable {
	zoom: 1; /* IE gain hasLayout */
}

.dragdrop-dragging {
	zoom: normal; /* remove artifacts */
}

.dragdrop-positioner {
	border: 1px dashed #1E90FF; /* blue */
	margin: 0px !important;
	zoom: 1; /* IE gain hasLayout */
	z-index: 100;
}

.dragdrop-flow-panel-positioner {
	color: #1E90FF; /* blue */
	display: inline;
	text-align: center;
	vertical-align: middle;
}

.dragdrop-proxy {
	background-color: #77AAFF; /* light blue */
}

.dragdrop-selected,.dragdrop-dragging,.dragdrop-proxy {
	-webkit-filter: opacity(.3);
	-moz-opacity: 0.3;
	-khtml-opacity: 0.3;
	-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=30)";
	opacity: 0.3;
}

.dragdrop-movable-panel {
	z-index: 200;
	margin: 0px !important;
	border: none !important;
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
