package serializers.flatbuffers;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;
import serializers.flatbuffers.FlatBuffers;


public class FlatBuffersTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		FlatBuffers.register(groups);

		runCorrectness(groups);
	}
}
