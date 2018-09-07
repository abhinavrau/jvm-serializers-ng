package serializers.json;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import data.media.Image;
import data.media.MediaContent;
import java.io.IOException;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

/**
 * This serializer uses svenson for JSON data binding.
 */
public class JsonSvensonDatabind {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(SerializerProperties.Format.JSON)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("svenson")
        .projectUrl("https://github.com/fforw/svenson")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new GenericSerializer<MediaContent>(properties, MediaContent.class));
  }

  static class GenericSerializer<T> extends Serializer<T> {

    private final org.svenson.JSONParser _jsonParser;
    private final org.svenson.JSON _jsonWriter;
    private final Class<T> type;

    public GenericSerializer(SerializerProperties properties, Class<T> clazz) {
      super(properties);
      type = clazz;

      _jsonParser = org.svenson.JSONParser.defaultJSONParser();
      _jsonParser.addTypeHint(".images[]", Image.class);
      _jsonWriter = org.svenson.JSON.defaultJSON();
    }

    @Override
    public T deserialize(byte[] array) throws Exception {
      return _jsonParser.parse(type, new String(array, "UTF-8"));
    }

    @Override
    public byte[] serialize(T data) throws IOException {
      String result = _jsonWriter.forValue(data);
      return result.getBytes();
    }
  }
}
