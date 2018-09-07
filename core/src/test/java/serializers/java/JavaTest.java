package serializers.java;

import data.media.MediaContent;
import org.junit.Test;
import serializers.JavaBuiltIn;
import serializers.JavaManual;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class JavaTest {


  @Test
  public void TestCorrectness() throws Throwable {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class,
        MediaContent.class);

    JavaBuiltIn.register(groups);
    JavaManual.register(groups);

    val.checkForCorrectness(groups, "data");

  }
}
