package serializers.jboss;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;


public class JBossTest  {


	@Test
	public void TestCorrectness() throws Throwable
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();
		Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class, MediaContent.class);

		//TODO: Fix this : JBossMarshalling.register(groups);
		JBossSerialization.register(groups);

		val.checkForCorrectness(groups,"data");
	}
}
