package serializers.jackson;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class JacksonTest {


  @Test
  public void TestCorrectness() throws Throwable {
    Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class,
        MediaContent.class);

    MediaContentTestGroup groups = new MediaContentTestGroup();

    //Binary
    JacksonAvroDatabind.register(groups);
    JacksonCBORDatabind.register(groups);
    JacksonProtobufDatabind.register(groups);
    JacksonSmileManual.register(groups);
    JacksonSmileDatabind.register(groups);

    // JSON
    JacksonJrDatabind.register(groups);
    JacksonJsonDatabind.register(groups);
    JacksonJsonTree.register(groups);
    JacksonJsonManual.register(groups);

    // Jackson databind with Afterburner; add-on module that uses bytecode gen for speed
    JacksonWithAfterburner.registerAll(groups);
    // Jackson's column-oriented variants for formats that usually use key/value notation
    JacksonWithColumnsDatabind.registerAll(groups);

    // XML
    JacksonXmlDatabind.register(groups);

    val.checkForCorrectness(groups, "data");
  }
}
