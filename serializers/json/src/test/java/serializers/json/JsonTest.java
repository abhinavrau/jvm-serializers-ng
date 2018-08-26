package serializers.json;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;
import serializers.jackson.JacksonJsonManual;


public class JsonTest {


	@Test
	public void TestCorrectness() throws Throwable
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();
		JacksonJsonManual.register(groups);
		//TODO: All commented lines fail with null on media.2.json
		FastJSONArrayDatabind.register(groups);
		FastJSONDatabind.register(groups);
		FlexjsonDatabind.register(groups);
		//JsonArgoTree.register(groups);
		//JsonDotOrgManualTree.register(groups);
		JsonGsonDatabind.register(groups);
		JsonGsonManual.register(groups);
		//JsonGsonTree.register(groups);
		//JsonijJpath.register(groups);
		//JsonijManualTree.register(groups);
		JsonLibJsonDatabind.register(groups);

		// JsonPathDeserializerOnly.register(groups);
		JsonSimpleManualTree.register(groups);
		JsonSimpleWithContentHandler.register(groups);
		JsonSmartManualTree.register(groups);
		JsonSvensonDatabind.register(groups);

		Validator<MediaContent,MediaContent> val = new Validator<>(MediaContent.class,
				MediaContent.class);

		val.checkForCorrectness(groups, "data");
	}
}
