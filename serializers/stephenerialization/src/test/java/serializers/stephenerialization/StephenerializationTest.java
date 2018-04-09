package serializers.stephenerialization;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.Stephenerialization;
import serializers.TestGroups;


public class StephenerializationTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		Stephenerialization.register(groups);

		runCorrectness(groups);
	}
}
