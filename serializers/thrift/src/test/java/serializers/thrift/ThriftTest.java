package serializers.thrift;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.Thrift;
import serializers.core.Validator;


public class ThriftTest {


  @Test
  public void TestCorrectness() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Thrift.register(groups);

    Validator<MediaContent, MediaContent> val = new Validator<>(data.media.MediaContent.class,
        MediaContent.class);

    val.checkForCorrectness(groups, "data");
  }
}
