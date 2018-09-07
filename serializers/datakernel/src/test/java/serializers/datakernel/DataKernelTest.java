package serializers.datakernel;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class DataKernelTest {


  @Test
  public void TestCorrectness() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, DataKernelSerializer.DMediaContent> val =
        new Validator<>(MediaContent.class, DataKernelSerializer.DMediaContent.class);

    DataKernelSerializer.register(groups);

    val.checkForCorrectness(groups, "data");
  }
}
