package serializers.avro;

import data.media.MediaContent;
import org.junit.Test;
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
		val.checkForCorrectness(groups, "data");

	}
}