package serializers.json;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import data.media.MediaContent;
import java.io.IOException;
import java.io.OutputStream;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

/**
 * This serializer uses FastJSON [https://github.com/alibaba/fastjson] for JSON data binding.
 */
public class FastJSONDatabind {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(SerializerProperties.Format.JSON)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("fastjson")
        .projectUrl("https://github.com/alibaba/fastjson")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new GenericSerializer<MediaContent>(properties, MediaContent.class));
  }

  static class GenericSerializer<T> extends Serializer<T> {

    private final Class<T> type;

    public GenericSerializer(SerializerProperties properties, Class<T> clazz) {
      super(properties);
      type = clazz;
    }

    public void serializeItems(T[] items, OutputStream out) throws IOException {
      for (int i = 0, len = items.length; i < len; ++i) {
        JSON.writeJSONString(out, items[i], SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.DisableCircularReferenceDetect);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(byte[] array) throws Exception {
      // fastjson can parse from byte array, yay:
      return (T) JSON.parseObject(array, type, Feature.DisableCircularReferenceDetect);
    }

    @Override
    public byte[] serialize(T data) throws IOException {
      return JSON.toJSONBytes(data, SerializerFeature.WriteEnumUsingToString,
          SerializerFeature.DisableCircularReferenceDetect);
    }
  }
}
