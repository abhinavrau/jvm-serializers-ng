package serializers.json;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import data.media.MediaContent;
import java.io.IOException;
import java.io.StringWriter;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

/**
 * This serializer uses Google-gson for data binding. to configure)
 */
public class JsonGsonDatabind {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(SerializerProperties.Format.JSON)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("gson")
        .projectUrl("https://github.com/google/gson")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new GenericSerializer<MediaContent>(properties, MediaContent.class));
  }

  // ------------------------------------------------------------
  // Serializer (just one)

  static class GenericSerializer<T> extends Serializer<T> {

    private final com.google.gson.Gson _gson = new com.google.gson.Gson();
    private final Class<T> type;

    public GenericSerializer(SerializerProperties properties, Class<T> clazz) {
      super(properties);
      type = clazz;
    }

    public T deserialize(byte[] array) throws Exception {
      T result = _gson.fromJson(new String(array, "UTF-8"), type);
      return result;
    }

    public byte[] serialize(T data) throws IOException {
      StringWriter w = new StringWriter();
      _gson.toJson(data, w);
      w.flush();
      return w.toString().getBytes("UTF-8");
    }
  }
}
