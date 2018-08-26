package serializers.protostuff;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class ProtostuffTest  {


	@Test
	public void TestCorrectness() throws Throwable
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();

		Protostuff.register(groups);
		ProtostuffJson.register(groups);
		ProtostuffSmile.register(groups);
		ProtostuffXml.register(groups);

		Validator<MediaContent, MediaContent> val =
				new Validator<>(MediaContent.class, MediaContent.class);

		val.checkForCorrectness(groups, "data");
	}
}
