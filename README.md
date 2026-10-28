# Tea DnD

Tea DnD adds drag and drop to Java browser applications built with **GWT or TeaVM**.
It is an **alpha maintenance port** of [Fred Sauer’s GWT-DnD](https://github.com/fredsa/gwt-dnd),
with the original `com.allen_sauer.gwt.dnd` packages and controller API.

A draggable item is an existing GWT widget. A drop controller decides where that widget can
be placed, and Java event handlers let the application accept, reject or record the move.
This makes the library useful for editable lists, task boards and movable panels in DOM-based
applications, including Verrai applications that expose GWT-compatible widgets.

Tea DnD does not introduce a canvas or an application framework. GWT builds use GWT’s widgets;
TeaVM builds use the same library sources through [teavm-compat](https://github.com/instanto-io/teavm-compat).
The application continues to own its data, templates and persistence.

Try the [TeaVM showcase](https://instanto-io.github.io/tea-dnd/) or the
[GWT showcase](https://instanto-io.github.io/tea-dnd/gwt/).

## Choose a compiler

The two artifacts expose the same public API. Use one in an application, according to its compiler.
The initial alpha is built from source; Maven Central publication is planned.

| Compiler | Maven artifact | Runtime |
| --- | --- | --- |
| GWT | `io.instanto:tea-dnd-gwt:0.1.0-SNAPSHOT` | Modern GWT; verified with 2.13.1 |
| TeaVM | `io.instanto:tea-dnd-teavm:0.1.0-SNAPSHOT` | Instanto’s GWT compatibility layer; TeaVM version managed by the organization parent |

For example, a TeaVM application adds:

```xml
<dependency>
  <groupId>io.instanto</groupId>
  <artifactId>tea-dnd-teavm</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

For GWT, use `tea-dnd-gwt` instead and inherit the module in your application’s `.gwt.xml`:

```xml
<inherits name="com.allen_sauer.gwt.dnd.Dnd"/>
```

The original module name, `com.allen_sauer.gwt.dnd.gwt-dnd`, is also retained.
TeaVM applications do not need a GWT module descriptor.

## Make a widget draggable

Start with a boundary panel. It defines the area in which a drag can take place. The pickup
controller temporarily moves widgets while the user drags them; registered drop controllers
then place the widgets in their destination panels.

This example creates two lists and allows a card to move between them:

```java
AbsolutePanel boundary = new AbsolutePanel();
boundary.setPixelSize(800, 400);
RootPanel.get("board").add(boundary);

VerticalPanel todo = new VerticalPanel();
todo.setPixelSize(280, 320);
VerticalPanel done = new VerticalPanel();
done.setPixelSize(280, 320);
boundary.add(todo, 20, 20);
boundary.add(done, 340, 20);

PickupDragController drag = new PickupDragController(boundary, false);
drag.registerDropController(new VerticalPanelDropController(todo));
drag.registerDropController(new VerticalPanelDropController(done));

Label card = new Label("Review the request");
todo.add(card);
drag.makeDraggable(card);
```

The classes above are in `com.google.gwt.user.client.ui`,
`com.allen_sauer.gwt.dnd.client`, and `com.allen_sauer.gwt.dnd.client.drop`.
The `false` argument prevents dropping directly onto the boundary: a drop must be accepted by
a registered target. Dropping elsewhere restores the widget’s original location.
Give empty targets a visible size in CSS as well. The showcase uses `display: block` on its
vertical-panel tables so an empty table does not collapse in WebKit.

## Give a card a separate handle

A draggable card may contain text fields, buttons or other interactive controls. Register a
separate handle so those controls can still receive input:

```java
FlowPanel card = new FlowPanel();
Label handle = new Label("Move card");
TextBox title = new TextBox();
card.add(handle);
card.add(title);
todo.add(card);
drag.makeDraggable(card, handle);
```

In a Verrai component, pass its GWT-compatible widget and handle to this API after they have
been added to their parent panel. Arbitrary DOM elements are not automatically widgets:
this first alpha does not provide a general adapter for independently managed template DOM.
Moving a widget also changes its widget parent and DOM position, so template ownership must
allow that move.

## Accept or reject a move

A drop controller can veto an operation before it is committed. The library then puts the
widget back where it started. For example, this target always rejects incoming cards:

```java
drag.registerDropController(new VerticalPanelDropController(locked) {
  @Override
  public void onPreviewDrop(DragContext context) throws VetoDragException {
    throw new VetoDragException();
  }
});
```

Use a `DragHandlerAdapter` to connect the interaction to application state. A non-null final
drop controller identifies an accepted destination; a cancelled drag has no final controller.

```java
drag.addDragHandler(new DragHandlerAdapter() {
  @Override
  public void onDragEnd(DragEndEvent event) {
    DragContext context = event.getContext();
    if (context.finalDropController != null) {
      // Read the new parent/order and update the application model here.
    }
  }
});
```

The library moves widgets, not database records. Your application decides when and how to save
an accepted change. Call `makeNotDraggable(widget)` when removing a draggable component, and
`unregisterDropController(controller)` when disposing a target.

## Styles and browser assets

Controller construction injects the small default stylesheet once. No external JavaScript
library, CDN request or asset-staging step is required. Consequently this port has no separate
WebJar: its behavior is compiled Java and its styles are embedded in the library.

The original `dragdrop-*` CSS names are retained so applications can style handles, selected
items, active drop targets and position indicators. Tea DnD removes the old stylesheet’s global
HTML margin reset; the surrounding page keeps control of its layout.

## Current scope

The source API includes pickup dragging, separate handles, selection, drag proxies, drag
sensitivity, veto hooks, and drop controllers for absolute, grid-constrained, flow, horizontal,
vertical and indexed panels. These are inherited capabilities, not a claim that every combination
has been verified in this alpha.

The browser contracts exercise moving between vertical panels, reordering, restoring a rejected
move, editing controls inside a card and disabling dragging. Both showcases compile the same
Java application and run the same browser tests.

The inherited mouse and touch paths remain. Keyboard dragging and a modern Pointer Events
implementation are not included. Legacy IE and Opera strategies now use standard browser behavior;
those obsolete browsers are not supported. This is widget movement within a page, not operating
system file drag-and-drop.

## Build and run the showcase

Build with JDK 21 and Maven. First install the organization parent POMs and the current
`teavm-compat` modules into your local Maven repository if they are not already available.

```shell
mvn clean install -DskipTests
mvn -f tea-dnd-showcase-gwt/pom.xml org.codehaus.mojo:exec-maven-plugin:3.6.3:java \
  -Dexec.classpathScope=test -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install chromium webkit"
mvn verify
mvn -Pproduction clean verify
```

The first command builds the libraries and both browser applications. The following commands
install the test browsers, exercise the standard build and repeat the checks with compiler
optimizations enabled. A successful compilation alone does not establish that dragging works.

Serve either generated site over HTTP:

```shell
python3 -m http.server 8080 --directory tea-dnd-showcase-teavm/target/site
```

For GWT, use `tea-dnd-showcase-gwt/target/site` instead. Open `http://localhost:8080` to try the
board and inspect its Java callbacks. The shared example source is in
[`Showcase.java`](showcase/src/main/java/io/instanto/dnd/demo/Showcase.java).

## Upstream and licence

Tea DnD builds on [GWT-DnD](https://github.com/fredsa/gwt-dnd), created by
[Fred Sauer](https://github.com/fredsa) and its contributors. The controller design and
original implementation come from their work. This port starts at commit `66cf6881542cf9e2ae64cf74e9f5761101969ebd`.
The upstream Git history, copyright headers and attribution are retained. Instanto’s changes
include the Maven modules, TeaVM adaptation, modern browser strategy, embedded resource provider,
showcase, browser checks and documentation.

Distributed under the [Apache License 2.0](LICENSE). See [NOTICE](NOTICE) for attribution.


## Questions and bug reports

For questions, bug reports or feature requests about this port, use the
[Tea DnD issue tracker](https://github.com/instanto-io/tea-dnd/issues). Please include whether
you are using GWT or TeaVM, your browser version and a small example of the problem.

The original project points users to the [GWT-DnD discussion group](https://groups.google.com/g/gwt-dnd).
Its existing discussions can help explain the original API. GWT-DnD is archived and no longer
maintained, so please direct issues with Tea DnD to our tracker.

## Support development

You can [sponsor Instanto’s maintenance of this port](https://github.com/sponsors/instanto-io).
If you use the TeaVM build, you can also [support TeaVM and its author, Alexey Andreev](https://github.com/sponsors/konsoletyper).
These are separate projects; sponsoring Instanto funds our maintenance work.
