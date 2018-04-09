package serializers.colfer;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;



public class ColferTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		//TODO: Coded around NullPointer exception on media2
		Colfer.register(groups);

		runCorrectness(groups);
	}
}
