package serializers.thrift;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;
import serializers.Thrift;


public class ThriftTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		Thrift.register(groups);

		runCorrectness(groups);
	}
}
