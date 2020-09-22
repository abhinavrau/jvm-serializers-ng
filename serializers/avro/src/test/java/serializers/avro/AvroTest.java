package serializers.avro;

import data.media.MediaContent;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.infra.Blackhole;
import serializers.CorrectnessCheckHelper;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class AvroTest extends CorrectnessCheckHelper {


  @Test
  public void TestCorrectness() throws Throwable {
    MediaContentTestGroup groups = new MediaContentTestGroup();
    Validator<MediaContent, MediaContent> val =
        new Validator<>(MediaContent.class, MediaContent.class);

    AvroGeneric.register(groups);
    AvroSpecific.register(groups);
    val.checkForCorrectness(groups, "data/media.1.json");

  }

  @Test
  public void testJMH() throws Exception {
    AvroBenchmark bench = new AvroBenchmark();

    AvroBenchmark.SerializationState state = new AvroBenchmark.SerializationState();

    state.doSetup();

    Assert.assertNotNull(bench.avroGenericSerialization(state));

  }
}