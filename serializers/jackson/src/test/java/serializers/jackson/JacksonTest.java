package serializers.jackson;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;
import serializers.avro.AvroGeneric;


public class JacksonTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		JacksonJsonManual.register(groups);
		JacksonCBORDatabind.register(groups);
		//JacksonProtobufDatabind.register(groups);

		// JSON
		JacksonJsonDatabind.register(groups);
		JacksonJrDatabind.register(groups);
		JacksonSmileManual.register(groups);
		JacksonSmileDatabind.register(groups);

		JacksonXmlDatabind.register(groups);
		// Jackson databind with Afterburner; add-on module that uses bytecode gen for speed
		JacksonWithAfterburner.registerAll(groups);

		// Jackson's column-oriented variants for formats that usually use key/value notation
		JacksonWithColumnsDatabind.registerAll(groups);

		runCorrectness(groups);
	}
}
