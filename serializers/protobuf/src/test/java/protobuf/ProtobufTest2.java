package protobuf;


import org.junit.Test;
import serializers.TestGroups;
import serializers.core.Validator;
import serializers.protobuf.Protobuf;
import serializers.protobuf.ProtobufJson;
import serializers.protobuf.media.MediaContentHolder;


public class ProtobufTest2  {


	@Test
	public void TestCorrectness() throws Throwable
	{
		TestGroups groups = new TestGroups();
		Validator<data.media.MediaContent, MediaContentHolder> val =
				new Validator<>(data.media.MediaContent.class, MediaContentHolder.class);
		Protobuf.register(groups);
		ProtobufJson.register(groups);
		//val.checkForCorrectness(groups, "data");
	}
}
