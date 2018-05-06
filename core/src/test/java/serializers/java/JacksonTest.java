package serializers.java;

import data.media.MediaContent;
import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;
import serializers.core.Validator;
import serializers.jackson.JacksonJsonManual;

import java.nio.file.Paths;


public class JacksonTest  {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class, MediaContent.class);

		JacksonJsonManual.register(groups);

		val.checkForCorrectness(groups, "data");

	}
}
