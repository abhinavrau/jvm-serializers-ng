package serializers.java;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;
import serializers.jackson.JacksonJsonManual;


public class JacksonTest {


  @Test
  public void TestCorrectness() throws Throwable {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class,
        MediaContent.class);

    JacksonJsonManual.register(groups);

    val.checkForCorrectness(groups, "data");

  }
}
