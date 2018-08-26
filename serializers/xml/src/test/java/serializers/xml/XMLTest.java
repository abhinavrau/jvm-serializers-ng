package serializers.xml;

import data.media.MediaContent;
import org.junit.Test;
import serializers.MediaContentTestGroup;
import serializers.core.Validator;
import serializers.xml.javolution.XmlJavolution;


public class XMLTest {


	@Test
	public void TestCorrectness() throws Exception
	{
		MediaContentTestGroup groups = new MediaContentTestGroup();

		ExiExificient.register(groups);
		Jaxb.register(groups);
		JaxbAalto.register(groups);
		XmlJavolution.register(groups);
		XmlXStream.register(groups);

		//TODO: XmlStax does not like media.2.json since it has nulls
		//XmlStax.register(groups, true, true, true);

		Validator<MediaContent,MediaContent> val = new Validator<>(MediaContent.class,
				MediaContent.class);

		val.checkForCorrectness(groups, "data");

	}
}
