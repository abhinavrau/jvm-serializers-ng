package serializers.core;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Table;
import data.media.MediaContent;
import org.junit.Assert;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializers.Serializer;
import serializers.TestGroup;
import serializers.MediaContentTestGroup;
import serializers.Transformer;
import serializers.core.metadata.SerializerProperties;
import serializers.util.unitils.ReflectionAssert;

import java.io.InputStream;
import java.util.Set;
import java.util.regex.Pattern;


public class Validator<S,T> {

	// Reference object of type S
	private final Class<S> sClass;
	// Serialized object of type T
	private final Class<T> tClass;
	Logger logger = LoggerFactory.getLogger(this.getClass());




	public Validator(final Class<S> sClass, final Class<T> tClass)
	{
		this.sClass = sClass;
		this.tClass = tClass;
	}

	public void checkForCorrectness(final MediaContentTestGroup groups, final String dataPath) throws Exception {

		if(!dataPath.endsWith(".json")) {

			Reflections reflections = new Reflections(dataPath, new ResourcesScanner());

			Set<String> jsonFiles =
					reflections.getResources(Pattern.compile(".*\\.json"));

			for (String filePath : jsonFiles) {

				checkForCorrectness(groups, this.getClass().getClassLoader().getResourceAsStream(filePath), filePath);

			}

		}else
		{
			checkForCorrectness(groups, this.getClass().getClassLoader().getResourceAsStream(dataPath), dataPath);
		}
	}

	protected void checkForCorrectness(final MediaContentTestGroup groups, final InputStream inputStream, final String dataFileName) throws Exception
	{
		Set<Table.Cell<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>>> entries
				= groups.media.entries.cellSet();

		S testData = loadTestData(inputStream);
		for(Table.Cell<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>> entry:  entries)
		{
			Serializer<?> serializer =  entry.getValue().serializer;
			Transformer<?,?> transformer = entry.getValue().transformer;
			logger.info("Testing serializer: {} with file: {}", entry.getColumnKey().getShortName() , dataFileName);
			checkCorrectness((Serializer<T>) serializer, (Transformer<S, T>) transformer, testData);

		}

	}
	protected S loadTestData( final InputStream inputstream) throws Exception
	{
		//Load the json file into data.media object
		JsonFactory jf = new JsonFactory();
		jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
		ObjectMapper mapper = new ObjectMapper(jf);


		// Convert JSON string from file to Object
		return mapper.readValue(inputstream, sClass);
	}



	private void  checkCorrectness(final Serializer<T> serializer, final Transformer<S, T> transformer, final S reference) throws Exception {
		String name = serializer.getName();

		T transformed = null;

		if(transformer != null) {
			// Use Transformer to convert object to the one that can be serialized
			transformed = transformer.forward(reference);
		}
		else {
			//transformed = mapper.map(reference, tClass);
		}


		// Serialize T to bytes
		byte[] bytes = serializer.serialize(transformed);

		// deserialize bytes  and compare output to data.media
		T deserialized = serializer.deserialize(bytes);

		// Check if deserialized matches what we serialized
		Assert.assertEquals("De-serialized object does not match serialized object",
				transformed, deserialized);

		S reversed = null;

		if(transformer!=null) {
			reversed = transformer.reverse(deserialized);
		}
		else {
			//reversed = mapper.map(deserialized, sClass);
		}
		// check some more things just in case.
		ReflectionAssert.assertLenientEquals("De-serialized and transformed object does not match reference object",
				reference, reversed);


	}


}
