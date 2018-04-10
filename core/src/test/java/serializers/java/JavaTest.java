package serializers.java;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.JavaBuiltIn;
import serializers.JavaManual;
import serializers.TestGroups;


public class JavaTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		JavaBuiltIn.register(groups);
		JavaManual.register(groups);

		runCorrectness(groups);
	}
}
