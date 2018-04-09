package serializers.scala;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.Scala;
import serializers.TestGroups;


public class ScalaTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();

		Scala.register(groups);

		runCorrectness(groups);
	}
}
