# Tea DnD maintenance

Keep a shared GWT-compatible API in tea-dnd-gwt/src/main/java. Generate TeaVM sources with
teavm-compat; never edit target/teavm-sources. Reusable GWT compatibility fixes belong in
teavm-compat with browser contract tests there.

Retain the original com.allen_sauer.gwt.dnd packages, copyright notices and module alias.
Mark modified upstream files. Document alpha limitations and verified behavior plainly.

Use JDK 21. Run mvn clean verify and mvn -Pproduction clean verify before publishing.
Both showcases must exercise real browser input against the compiled Java application.
