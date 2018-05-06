package serializers.java;

import data.media.MediaContent;
import org.junit.Test;
import serializers.JavaBuiltIn;
import serializers.JavaManual;
import serializers.TestGroups;
import serializers.core.Validator;



public class JavaTest2  {



	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		Validator<MediaContent, MediaContent> val = new Validator<>(MediaContent.class, MediaContent.class);

		JavaBuiltIn.register(groups);
		JavaManual.register(groups);

		val.checkForCorrectness(groups, "data");

	}
}
