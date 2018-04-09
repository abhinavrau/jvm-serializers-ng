package serializers.protobuf;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;
import serializers.jackson.JacksonJsonManual;


public class ProtobufTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		Protobuf.register(groups);
		runCorrectness(groups);
	}
}
