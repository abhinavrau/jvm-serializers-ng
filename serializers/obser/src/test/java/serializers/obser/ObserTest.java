package serializers.obser;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class ObserTest {


  @Test
  public void TestCorrectness() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Obser.register(groups);

    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);
    val.checkForCorrectness(groups, "data");

  }
}
