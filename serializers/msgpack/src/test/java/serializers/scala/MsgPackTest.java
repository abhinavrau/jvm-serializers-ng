package serializers.scala;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;


public class MsgPackTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();

		MsgPack.register(groups);

		runCorrectness(groups);
	}
}
