package serializers.colfer;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class ColferTest {


  @Test
  public void TestCorrectness() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);

    //TODO: Coded around NullPointer exception on media2
    Colfer.register(groups);

    val.checkForCorrectness(groups, "data");
  }
}
