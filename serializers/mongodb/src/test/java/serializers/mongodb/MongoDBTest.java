package serializers.mongodb;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;
import serializers.mongodb.MongoDB;
import serializers.mongodb.MongoDBManual;


public class MongoDBTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		MongoDBManual.register(groups);
		MongoDB.register(groups);

		runCorrectness(groups);
	}
}
