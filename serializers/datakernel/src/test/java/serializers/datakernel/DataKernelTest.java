package serializers.datakernel;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;



public class DataKernelTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		DataKernelSerializer.register(groups);

		runCorrectness(groups);
	}
}
