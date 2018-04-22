package serializers.core;



import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.media.MediaContent;
import org.apache.commons.beanutils.BeanUtils;
import org.unitils.reflectionassert.ReflectionAssert;
import serializers.Serializer;

import java.io.File;


public class Validator {



public static <T> void checkForCorrectness(Serializer<T> serializer, String dataFileName) throws Exception
{
	//Load the json file into data.media object
	JsonFactory jf = new JsonFactory();
	jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
	ObjectMapper mapper = new ObjectMapper(jf);

	// Convert JSON string from file to Object
	MediaContent reference = mapper.readValue(new File(dataFileName), MediaContent.class);

//	//Copy the data.media.MediaContent object to object of type T
//
//	BeanUtils.copyProperties(toSerialize, reference);
//
//	// Serialize T to bytes
//	byte[] bytes = serializer.serialize(toSerialize);
//
//	// deserialize bytes  and compare output to data.media
//	T toCompare = serializer.deserialize(bytes);
//
//	ReflectionAssert.assertLenientEquals("Deserialized object does not match", reference, toCompare);

}

//	private void validateDataFile(BenchmarkBase.Params params) {
//		// And then let's verify input data file bit more...
//		File dataFile = new File(params.dataFileName);
//		if (!dataFile.exists()) {
//			throw new IllegalArgumentException("Couldn't find data file \"" + dataFile.getPath() + "\"");
//		}
//		String[] parts = dataFile.getName().split("\\.");
//		if (parts.length < 3) {
//			throw new IllegalArgumentException("Data file \"" + dataFile.getName() + "\" should be of the form \"<type>.<name>.<extension>\"");
//
//		}
//		params.dataType = parts[0];
//		params.dataExtra = parts[1];
//		params.dataExtension = parts[parts.length-1];
//	}
//
//	protected TestGroup<?> findGroupForTestData(TestGroups groups, BenchmarkBase.Params params)
//	{
//		TestGroup<?> group = groups.groupMap.get(params.dataType);
//		if (group == null) {
//			throw new IllegalArgumentException("Data file \"" + params.dataFileName + "\" can't be loaded. Don't know about data type \"" + params.dataType + "\"");
//
//		}
//		return group;
//	}
//
//	protected Object loadTestData(TestGroup<?> bootstrapGroup, BenchmarkBase.Params params)
//	{
//		TestGroup.Entry<?,Object> loader = bootstrapGroup.extensionHandlers.get(params.dataExtension);
//		if (loader == null) {
//			System.err.println("Data file \"" + params.dataFileName + "\" can't be loaded.");
//			System.err.println("No deserializer registered for data type \"" + params.dataType
//					+ "\" and file extension \"." + params.dataExtension + "\"");
//			System.exit(1);
//		}
//		byte[] fileBytes;
//		try {
//			// Load entire file into a byte array.
//			fileBytes = java.nio.file.Files.readAllBytes(new File(params.dataFileName).toPath());
//		}
//		catch (IOException ex) {
//			System.err.println("Error loading data from file \"" + params.dataFileName + "\".");
//			System.err.println(ex.getMessage());
//			System.exit(1); return null;
//		}
//		try {
//			return convertTestData(loader, fileBytes);
//		} catch (Exception ex) {
//			System.err.println("Error converting test data from file \"" + params.dataFileName + "\".");
//			System.err.println(ex.getMessage());
//			System.exit(1); return null;
//		}
//	}
//
//	protected Object convertTestData(TestGroup.Entry<?,Object> loader, byte[] data)
//			throws Exception
//	{
//		Object deserialized = loader.serializer.deserialize(data);
//		return loader.transformer.reverse(deserialized);
//	}
//
//	/**
//	 * Method called to both load in test data and figure out which tests should
//	 * actually be run, from all available test cases.
//	 */
//	protected Iterable<TestGroup.Entry<Object,Object>> findApplicableTests(TestGroups groups, BenchmarkBase.Params params,
//	                                                                       TestGroup<?> bootstrapGroup)
//	{
//		@SuppressWarnings("unchecked")
//		TestGroup<Object> group_ = (TestGroup<Object>) bootstrapGroup;
//		Set<String> matched = new HashSet<String>();
//
//		Iterable<TestGroup.Entry<Object,Object>> available = group_.entries.values();
//
//		if (params.filterStrings == null) {
//			return available;
//		}
//		ArrayList<TestGroup.Entry<Object,Object>> matchingEntries = new ArrayList<TestGroup.Entry<Object,Object>>();
//
//		for (TestGroup.Entry<?,Object> entry_ : available) {
//			@SuppressWarnings("unchecked")
//			TestGroup.Entry<Object,Object> entry = (TestGroup.Entry<Object,Object>) entry_;
//			String name = entry.serializer.getName();
//			// See if any of the filters match.
//			boolean found = false;
//			for (String s : params.filterStrings) {
//				boolean thisOneMatches = match(s, name);
//				if (thisOneMatches) {
//					matched.add(s);
//					found = true;
//				}
//			}
//
//			if (found == params.filterIsInclude) {
//				matchingEntries.add(entry);
//			}
//		}
//		Set<String> unmatched = new HashSet<String>(params.filterStrings);
//		unmatched.removeAll(matched);
//		for (String s : unmatched) {
//			System.err.println("Warning: there is no implementation name matching the pattern \"" + s + "\"");
//		}
//		return matchingEntries;
//	}
//
//	protected static boolean match(String pattern, String name)
//	{
//		StringBuilder regex = new StringBuilder();
//
//		while (pattern.length() > 0) {
//			int starPos = pattern.indexOf('*');
//			if (starPos < 0) {
//				regex.append(Pattern.quote(pattern));
//				break;
//			}
//			else {
//				String beforeStar = pattern.substring(0, starPos);
//				String afterStar = pattern.substring(starPos + 1);
//
//				regex.append(Pattern.quote(beforeStar));
//				regex.append(".*");
//				pattern = afterStar;
//			}
//		}
//
//		return Pattern.matches(regex.toString(), name);
//	}
//
//	public void checkForCorrectness(TestGroups groups, String dataFileName) throws Exception
//	{
//		BenchmarkBase.Params params = new BenchmarkBase.Params();
//		params.dataFileName = dataFileName;
//		validateDataFile(params);
//
//		TestGroup<?> bootstrapGroup = findGroupForTestData(groups, params);
//		Object testData = loadTestData(bootstrapGroup, params);
//		Iterable<TestGroup.Entry<Object,Object>> matchingEntries
//				= findApplicableTests(groups, params, bootstrapGroup);
//
//		StringWriter errors = new StringWriter();
//		PrintWriter errorsPW = new PrintWriter(errors);
//		for (TestGroup.Entry<Object,Object> entry : matchingEntries)
//		{
//			System.out.println("Checking correctness for " + entry.serializer.getName() );
//			checkCorrectness(errorsPW, entry.transformer, entry.serializer, testData);
//		}
//		System.out.println("[done]");
//
//	}
//
//	protected <J> void checkCorrectness(PrintWriter errors, Transformer<J,Object> transformer,
//	                                   Serializer<Object> serializer, J value)
//			throws Exception
//	{
//
//	}

}
