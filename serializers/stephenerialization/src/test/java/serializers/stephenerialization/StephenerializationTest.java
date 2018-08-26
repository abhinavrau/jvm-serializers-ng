package serializers.stephenerialization;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class StephenerializationTest  {


	@Test
	public void TestCorrectness() throws Exception
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();
		Stephenerialization.register(groups);

		Validator<data.media.MediaContent,data.media.MediaContent> val = new Validator<>(data.media.MediaContent.class,
				MediaContent.class);

		val.checkForCorrectness(groups, "data");
	}
}
