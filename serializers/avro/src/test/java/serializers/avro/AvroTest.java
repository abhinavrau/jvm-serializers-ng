package serializers.avro;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;



public class AvroTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		AvroGeneric.register(groups);
		AvroSpecific.register(groups);
		runCorrectness(groups);
	}
}
