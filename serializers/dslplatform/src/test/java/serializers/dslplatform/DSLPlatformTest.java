package serializers.dslplatform;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;



public class DSLPlatformTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		DSLPlatform.register(groups);

		runCorrectness(groups);
	}
}
