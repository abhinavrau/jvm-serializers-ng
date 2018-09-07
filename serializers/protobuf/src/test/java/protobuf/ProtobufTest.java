package serializers.protobuf;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class ProtobufTest {


  @Test
  public void TestCorrectness() throws Throwable {
    MediaContentTestGroup groups = new MediaContentTestGroup();

    Protobuf.register(groups);
    ProtobufJson.register(groups);
    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);

    val.checkForCorrectness(groups, "data");
  }
}
