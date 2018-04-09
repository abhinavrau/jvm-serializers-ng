package serializers.xml;

import org.junit.Test;
import serializers.CorrectnessCheckHelper;
import serializers.TestGroups;


public class XMLTest extends CorrectnessCheckHelper {


	@Test
	public void TestCorrectness() throws Exception
	{
		TestGroups groups = new TestGroups();
		////TODO: XmlStax does not like media.2.json
		//ExiExificient.register(groups);
		Jaxb.register(groups);
		JaxbAalto.register(groups);
		// TODO: XmlJavolution does not like any tests
		//XmlJavolution.register(groups);
		//TODO: XmlStax does not like media.2.json
		//XmlStax.register(groups, true, false, false);
		// TODO: XmlStream does not like any tests
		//XmlXStream.register(groups);
		runCorrectness(groups);

		//TODO: XmlStax does not like media.2.json
//		 groups = new TestGroups();
//		XmlStax.register(groups, true, true, false);
//		runCorrectness(groups);
//
//		groups = new TestGroups();
//		XmlStax.register(groups, true, true, true);
//		runCorrectness(groups);
	}
}
