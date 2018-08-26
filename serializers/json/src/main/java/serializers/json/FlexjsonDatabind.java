package serializers.json;

import data.media.MediaContent;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import serializers.JavaBuiltIn;
import serializers.Serializer;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import java.io.IOException;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

/**
 * This serializer uses Flexjson for data binding.  
 * http://flexjson.sourceforge.net
 */
public class FlexjsonDatabind
{
  public static void register(MediaContentTestGroup groups)
  {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
            .format(SerializerProperties.Format.JSON)
            .apiStyle(REFLECTION)
            .mode(CODE_FIRST)
            .valueType(POJO)
            .name("flexjson")
            .feature(SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES)
            .projectURL("http://flexjson.sourceforge.net")
            .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
                    new GenericSerializer<MediaContent>(properties, MediaContent.class));
  }

  static class GenericSerializer<T> extends Serializer<T>
  {

    private final Class<T> type;

    public GenericSerializer(SerializerProperties properties, Class<T> clazz)
    {
      super(properties);
      type = clazz;
    }


    public T deserialize(byte[] array) throws Exception
    {
      return new JSONDeserializer<T>().deserialize(new String(array, "UTF-8"), type);
    }

    public byte[] serialize(T data) throws IOException
    {
      String jsonString = new JSONSerializer().exclude("*.class").deepSerialize(data);
      return jsonString.getBytes("UTF-8");
    }
  }
}
