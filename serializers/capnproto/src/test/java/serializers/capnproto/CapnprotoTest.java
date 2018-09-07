package serializers.capnproto;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class CapnprotoTest {


  @Test
  public void TestCorrectness() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);
    CapNProto.register(groups);

    val.checkForCorrectness(groups, "data");
  }
}
