package serializers.jboss;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;


public class JBossTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		//TODO: Fix this : JBossMarshalling.register(groups);
		JBossSerialization.register(groups);

		runCorrectness(groups);
	}
}
