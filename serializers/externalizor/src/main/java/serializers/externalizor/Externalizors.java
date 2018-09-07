package serializers.externalizor;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.qwazr.externalizor.Externalizor;
import data.media.MediaContent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

public class Externalizors {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("externalizor")
        .projectUrl("https://github.com/qwazr/externalizor")
        .build();

    SerializerProperties gzip_properties = properties.toBuilder()
        .feature(OPTIMIZED).build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new ExternalizorSerializer<MediaContent>(MediaContent.class, properties));
    groups.media.add(JavaBuiltIn.mediaTransformer,
        new GZIPExternalizorSerializer<MediaContent>(MediaContent.class, gzip_properties));
  }

  // ------------------------------------------------------------
  // Serializer (just one)

  public final static class ExternalizorSerializer<T> extends Serializer<T> {

    private final Class<T> clz;


    public ExternalizorSerializer(Class<T> c, SerializerProperties properties) {
      super(properties);
      clz = c;
    }

    @SuppressWarnings("unchecked")
    public T deserialize(byte[] array) throws Exception {
      try (ByteArrayInputStream input = new ByteArrayInputStream(array)) {
        return Externalizor.deserializeRaw(input, clz);
      }
    }

    public byte[] serialize(T data) throws IOException, ReflectiveOperationException {
      try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
        Externalizor.serializeRaw(data, output);
        return output.toByteArray();
      }
    }


  }

  public final static class GZIPExternalizorSerializer<T> extends Serializer<T> {

    private final Class<T> clz;


    public GZIPExternalizorSerializer(Class<T> c, SerializerProperties properties) {
      super(properties);
      clz = c;
    }

    @SuppressWarnings("unchecked")
    public T deserialize(byte[] array) throws Exception {
      try (ByteArrayInputStream input = new ByteArrayInputStream(array)) {
        return Externalizor.deserialize(input, clz);
      }
    }

    public byte[] serialize(T data) throws IOException, ReflectiveOperationException {
      try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
        Externalizor.serialize(data, output);
        return output.toByteArray();
      }
    }


  }
}
