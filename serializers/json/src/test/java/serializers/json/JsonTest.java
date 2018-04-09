package serializers.json;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;


public class JsonTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
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

		runCorrectness(groups);
	}
}
