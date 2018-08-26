package serializers.fst;

import data.media.MediaContent;
import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class FastSerializationTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();

		Validator<MediaContent, MediaContent> val =
				new Validator<>(MediaContent.class, MediaContent.class);
		FastSerialization.register(groups);

		val.checkForCorrectness(groups, "data");
	}
}
