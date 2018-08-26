package serializers.wobly;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class WoblyTest {


	@Test
	public void TestCorrectness() throws Exception
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();
		Wobly.register(groups);

		Validator<MediaContent,MediaContent> val = new Validator<>(data.media.MediaContent.class,
				MediaContent.class);

		val.checkForCorrectness(groups, "data");
	}
}
