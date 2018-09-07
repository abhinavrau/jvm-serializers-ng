package serializers.dslplatform;

import data.media.MediaContent;
import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;
import serializers.dslplatform.data.media.DSLMediaContent;


public class DSLPlatformTest extends CorrectnessCheckHelper {


  @Test
  public void TestCorrectness() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);

    DSLPlatform.register(groups);

    val.checkForCorrectness(groups, "data");
  }

  @Test
  public void TestCorrectnessWithAnnotationProcessor() throws Exception {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<DSLMediaContent, DSLMediaContent> val =
        new Validator<>(DSLMediaContent.class, DSLMediaContent.class);

    DSLPlatform.registerAnnotationBased(groups);

    val.checkForCorrectness(groups, "data");
  }

}
