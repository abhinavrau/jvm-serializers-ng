package serializers.javaxjson;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class JavaxJsonTest {


	@Test
	public void TestCorrectness() throws Throwable
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();

		Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class, MediaContent.class);

		JavaxJsonStreamGlassfish.register(groups);

		// TODO: Fix this: JavaxJsonTreeGlassfish.register(groups);



		val.checkForCorrectness(groups,"data");
	}
}
