package serializers.core;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Table;
import data.media.MediaContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.TestGroup;
import serializers.Transformer;
import serializers.core.metadata.SerializerProperties;
import serializers.core.util.Pair;

import java.io.InputStream;
import java.util.Set;


public class DataLoader<S, T> {

  // Reference object of type S
  private final Class<S> sClass;
  // Serialized object of type T
  private final Class<T> tClass;
  Logger logger = LoggerFactory.getLogger(this.getClass());


  public DataLoader(final Class<S> sClass, final Class<T> tClass) {
    this.sClass = sClass;
    this.tClass = tClass;
  }

  public static Pair<Serializer,Transformer> getSerializer(final MediaContentTestGroup groups)
  {
    Set<Table.Cell<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>>> entries
            = groups.media.getTable().cellSet();

    Serializer<?> serializer = null;
    Transformer<?, ?> transformer = null;
    for (Table.Cell<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>> entry : entries) {
      serializer = entry.getValue().serializer;
      transformer = ((TestGroup.Entry)entry.getValue()).transformer;
    }
    return new Pair<>(serializer,transformer);
  }

  public S loadTestDataFromPath(final String filePath)
      throws Exception {

     return loadTestData( this.getClass().getClassLoader().getResourceAsStream(filePath));
  }

  protected S loadTestData(final InputStream inputstream) throws Exception {
    //Load the json file into data.media object
    JsonFactory jf = new JsonFactory();
    jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
    ObjectMapper mapper = new ObjectMapper(jf);

    // Convert JSON string from file to Object
    return mapper.readValue(inputstream, sClass);
  }


}
