package serializers.kryo;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class KryoTest {


  @Test
  public void TestCorrectness() throws Throwable {
    MediaContentTestGroup groups = new MediaContentTestGroup();

    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);

    Kryo.register(groups);

    val.checkForCorrectness(groups, "data");
  }
}
