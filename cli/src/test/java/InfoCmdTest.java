import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

import com.google.common.collect.Table;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import serializers.cli.Cli;

public class InfoCmdTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private final Table table = Cli.registerAllSerializers().media.getTable();


  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testAll() throws Exception {
    String[] args = {"info", "-a"};
    Cli.main(args);
    assertThat("Number of serializers",
        getOutputWithLineSplit().length,
        equalTo(table.size()));

  }

  private String[] getOutputWithLineSplit() {
    if (outContent.toString().isEmpty()) {
      return new String[0];
    } else {
      return outContent.toString().split(System.getProperty("line.separator"));
    }
  }

  @Test
  public void testNames() throws Exception {
    String[] args = {"info", "-n"};
    Cli.main(args);
    assertThat("Number of serializers by name",
        getOutputWithLineSplit().length,
        equalTo(table.rowKeySet().size()));


  }


  @Test
  public void testBinary() throws Exception {
    String[] args = {"info", "-b"};
    Cli.main(args);
    assertThat("Binary Serializers",
        Arrays.asList(getOutputWithLineSplit()),
        everyItem(containsStringIgnoringCase("binary")));

  }

  @Test
  public void testJSON() throws Exception {
    String[] args = {"info", "-j"};
    Cli.main(args);
    assertThat("JSON Serializers",
        Arrays.asList(getOutputWithLineSplit()),
        everyItem(containsStringIgnoringCase("json")));

  }

  @Test
  public void testXML() throws Exception {
    String[] args = {"info", "-x"};
    Cli.main(args);
    assertThat("XML Serializers",
        Arrays.asList(getOutputWithLineSplit()),
        everyItem(containsStringIgnoringCase("xml")));

  }

  @Test
  public void testText() throws Exception {
    String[] args = {"info", "-e"};
    Cli.main(args);
    assertThat("Text Serializers",
        Arrays.asList(getOutputWithLineSplit()),
        everyItem(either(containsStringIgnoringCase("json"))
            .or(containsStringIgnoringCase("xml"))));

  }

  @Test
  public void testFind() throws Exception {
    String[] args = {"info", "-f", "cross-lang", "back-compat", "binary", "rt-reflection", "pojo"};
    Cli.main(args);
    assertThat("Find Serializers",
        Arrays.asList(getOutputWithLineSplit()),
        everyItem(allOf(containsStringIgnoringCase("cross-lang"),
            containsStringIgnoringCase("back-compat"),
            containsStringIgnoringCase("binary"),
            containsStringIgnoringCase("cross-lang"),
            containsStringIgnoringCase("rt-reflection"),
            containsStringIgnoringCase("pojo"))));

  }

  @Test
  public void testFindNoResults() throws Exception {
    String[] args = {"info", "-f", "code-first", "schema-first"};
    Cli.main(args);
    assertThat("No results Find Serializers",
        getOutputWithLineSplit(), emptyArray());

  }

}
