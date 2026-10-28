package io.instanto.dnd.demo;

import com.allen_sauer.gwt.dnd.client.*;
import com.allen_sauer.gwt.dnd.client.drop.*;
import com.google.gwt.user.client.ui.*;

/** The same widget application runs through GWT and TeaVM. */
public final class Showcase {
  private static int completed;
  public static void start() {
    AbsolutePanel boundary = new AbsolutePanel();
    boundary.getElement().setId("board");
    boundary.setStyleName("board");
    boundary.setPixelSize(850, 340);
    RootPanel.get("demo").add(boundary);
    PickupDragController drag = new PickupDragController(boundary, false);
    drag.setBehaviorDragStartSensitivity(5);
    drag.setBehaviorScrollIntoView(false);
    VerticalPanel todo = column("todo", boundary, 20);
    VerticalPanel done = column("done", boundary, 300);
    VerticalPanel locked = column("locked", boundary, 580);
    drag.registerDropController(new VerticalPanelDropController(todo));
    drag.registerDropController(new VerticalPanelDropController(done));
    drag.registerDropController(new VerticalPanelDropController(locked) {
      @Override public void onPreviewDrop(DragContext context) throws VetoDragException {
        throw new VetoDragException();
      }
    });
    card("alpha", "Review the request", todo, drag);
    card("beta", "Prepare the document", todo, drag);
    card("gamma", "Check the result", todo, drag);
    Label status = new Label("Ready. Drag a card using its handle.");
    status.getElement().setId("status");
    RootPanel.get("events").add(status);
    drag.addDragHandler(new DragHandlerAdapter() {
      @Override public void onDragStart(DragStartEvent event) { status.setText("Dragging " + event.getContext().draggable.getElement().getId()); }
      @Override public void onDragEnd(DragEndEvent event) {
        boolean accepted = event.getContext().finalDropController != null;
        if (accepted) completed++;
        status.setText((accepted ? "Moved " : "Cancelled ") + event.getContext().draggable.getElement().getId());
        status.getElement().setAttribute("data-completed", Integer.toString(completed));
      }
    });
    Button disable = new Button("Disable first card");
    disable.getElement().setId("disable");
    disable.addClickHandler(event -> { drag.makeNotDraggable(first); status.setText("First card disabled"); });
    RootPanel.get("actions").add(disable);
    RootPanel.get("demo").getElement().setAttribute("data-ready", "true");
  }
  private static Widget first;
  private static VerticalPanel column(String id, AbsolutePanel parent, int x) {
    VerticalPanel panel = new VerticalPanel();
    panel.getElement().setId(id);
    panel.setStyleName("column");
    panel.setPixelSize(250, 300);
    parent.add(panel, x, 20);
    return panel;
  }
  private static void card(String id, String title, VerticalPanel parent, PickupDragController drag) {
    FlowPanel card = new FlowPanel(); card.setStyleName("card"); card.getElement().setId(id);
    Label handle = new Label("Move · " + title); handle.setStyleName("handle"); handle.getElement().setId(id + "-handle");
    card.add(handle);
    TextBox input = new TextBox(); input.setText(title); input.getElement().setAttribute("aria-label", title); card.add(input);
    parent.add(card); drag.makeDraggable(card, handle); if (first == null) first = card;
  }
}
