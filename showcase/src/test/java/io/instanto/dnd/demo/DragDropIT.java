package io.instanto.dnd.demo;

import static org.junit.Assert.*;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.BoundingBox;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DragDropIT {
  @Parameterized.Parameters(name="{0}")
  public static Object[] engines() { return new Object[]{"chromium", "webkit"}; }
  @Parameterized.Parameter public String engine;
  private HttpServer server;
  private Playwright playwright;
  private Browser browser;
  private Page page;
  private final List<String> errors = new ArrayList<>();

  @Before public void start() throws Exception {
    Path root = Path.of("target/site").toAbsolutePath().normalize();
    server = HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
    server.createContext("/", exchange -> {
      String name = exchange.getRequestURI().getPath();
      Path file = root.resolve(name.equals("/") ? "index.html" : name.substring(1)).normalize();
      if (!file.startsWith(root) || !Files.isRegularFile(file)) { exchange.sendResponseHeaders(404,-1); exchange.close(); return; }
      byte[] body = Files.readAllBytes(file);
      exchange.getResponseHeaders().set("Content-Type",name.endsWith(".js") ? "application/javascript" : name.endsWith(".css") ? "text/css" : "text/html");
      exchange.sendResponseHeaders(200,body.length);
      try(var out=exchange.getResponseBody()){out.write(body);}
    });
    server.start();
    playwright = Playwright.create();
    browser = (engine.equals("chromium") ? playwright.chromium() : playwright.webkit()).launch(new BrowserType.LaunchOptions().setTimeout(20000));
    page=browser.newPage(new Browser.NewPageOptions().setViewportSize(1100,900));
    page.setDefaultTimeout(7000);
    page.onPageError(errors::add);
    page.navigate("http://127.0.0.1:"+server.getAddress().getPort()+"/");
    page.waitForSelector("#demo[data-ready=true]");
  }
  @After public void stop() {
    if(page!=null && !errors.isEmpty()) System.err.println(errors);
    if(browser!=null) browser.close();
    if(playwright!=null) playwright.close();
    if(server!=null) server.stop(0);
  }
  private void drag(String source, String target, double yOffset) {
    BoundingBox a=page.locator(source).boundingBox();
    BoundingBox b=page.locator(target).boundingBox();
    page.mouse().move(a.x+a.width/2,a.y+a.height/2);
    page.mouse().down();
    page.mouse().move(a.x+a.width/2+12,a.y+a.height/2+12,new Mouse.MoveOptions().setSteps(3));
    page.mouse().move(b.x+b.width/2,b.y+yOffset,new Mouse.MoveOptions().setSteps(18));
    page.mouse().up();
    System.out.println(engine+" "+source+" -> "+target+": "+page.locator("#status").innerText());
    page.screenshot(new Page.ScreenshotOptions().setPath(Path.of("target/"+engine+"-"+target.substring(1)+".png")));
  }
  private void healthy(){ assertTrue("Browser errors: "+errors,errors.isEmpty()); }
  @Test public void movesBetweenColumnsAndReportsCompletion() {
    drag("#alpha-handle","#done",35);
    page.waitForSelector("#done #alpha");
    assertEquals("Moved alpha",page.locator("#status").innerText());
    assertEquals("1",page.locator("#status").getAttribute("data-completed"));
    healthy();
  }
  @Test public void reordersWithinAColumn() {
    drag("#alpha-handle","#gamma",65);
    page.waitForFunction("document.querySelector('#todo .card').id === 'beta'");
    assertEquals(List.of("beta","gamma","alpha"),page.locator("#todo .card").evaluateAll("xs=>xs.map(x=>x.id)"));
    healthy();
  }
  @Test public void vetoRestoresOriginalOrder() {
    drag("#alpha-handle","#locked",35);
    page.waitForSelector("#todo #alpha");
    assertEquals(List.of("alpha","beta","gamma"),page.locator("#todo .card").evaluateAll("xs=>xs.map(x=>x.id)"));
    assertEquals("Cancelled alpha",page.locator("#status").innerText());
    healthy();
  }
  @Test public void formFieldsRemainEditable() {
    page.locator("#alpha input").fill("Updated request");
    assertEquals("Updated request",page.locator("#alpha input").inputValue());
    assertEquals(3,page.locator("#todo .card").count());
    healthy();
  }
  @Test public void disablingRemovesDragging() {
    page.locator("#disable").click();
    drag("#alpha-handle","#done",35);
    assertEquals(1,page.locator("#todo #alpha").count());
    assertEquals(0,page.locator("#done .card").count());
    healthy();
  }
}
