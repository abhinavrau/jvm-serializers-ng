package serializers.protostuff;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;


public class ProtostuffTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();

		// TODO: Fix this test below as it fails as it does not honor the optional fields
		//Protostuff.register(groups);
		ProtostuffJson.register(groups);
		ProtostuffSmile.register(groups);
		ProtostuffXml.register(groups);

		runCorrectness(groups);
	}
}
