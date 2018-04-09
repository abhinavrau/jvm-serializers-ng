package serializers.javaxjson;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;



public class JavaxJsonTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		JavaxJsonStreamGlassfish.register(groups);
		//JavaxJsonTreeGlassfish.register(groups);



		runCorrectness(groups);
	}
}
