package serializers.core;



import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.media.MediaContent;

import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.junit.Assert;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import serializers.util.unitils.ReflectionAssert;
import serializers.Serializer;
import serializers.TestGroup;
import serializers.TestGroups;
import serializers.Transformer;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class Validator<S,T> {

	// Reference object of type S
	private final Class<S> sClass;
	// Serialized object of type T
	private final Class<T> tClass;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	Mapper mapper = DozerBeanMapperBuilder.create()
			.withMappingFiles("dozerBeanMapping.xml")
			.build();



	public Validator(final Class<S> sClass, final Class<T> tClass)
	{
		this.sClass = sClass;
		this.tClass = tClass;
	}

	public void checkForCorrectness(final TestGroups groups, final String dataPath) throws Exception {

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

	protected void checkForCorrectness(final TestGroups groups, final InputStream inputStream, final String dataFileName) throws Exception
	{
		Set<Map.Entry<String, TestGroup.Entry<MediaContent, Object>>> entries = groups.media.entries.entrySet();

		S testData = loadTestData(inputStream);
		for(Map.Entry<String, TestGroup.Entry<MediaContent, Object>> entry:  entries)
		{
			Serializer<?> serializer =  entry.getValue().serializer;
			Transformer<?,?> transformer = entry.getValue().transformer;
			logger.info("Testing serializer: {} with file: {}", serializer.getName() , dataFileName);
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

		//T transformed = transformer.forward(reference);

		T transformed = transformer.forward(reference);


		//T transformed = mapper.map(reference, tClass);

		ReflectionAssert.assertLenientEquals("Error in copying Bean Properties", reference, transformed );
		// Serialize T to bytes
		byte[] bytes = serializer.serialize(transformed);

		// deserialize bytes  and compare output to data.media
		T deserialized = serializer.deserialize(bytes);

		//S reversed = transformer.reverse(deserialized);
		Assert.assertEquals("De-serialized object does not match serialized object", transformed, deserialized);
		//ReflectionAssert.assertLenientEquals("De-serialized object does not match serialized object", reference, reversed);
		ReflectionAssert.assertLenientEquals("De-serialized object does not match reference data object", reference, deserialized);
	}


}
