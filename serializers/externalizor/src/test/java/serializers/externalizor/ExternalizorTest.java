package serializers.externalizor;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;



public class ExternalizorTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		Externalizors.register(groups);

		runCorrectness(groups);
	}
}
