package serializers.protostuff;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;
import static serializers.protostuff.Protostuff.MEDIA_CONTENT_SCHEMA;

import io.protostuff.Schema;
import io.protostuff.SmileIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;
import serializers.protostuff.media.MediaContent;

/**
 * @author David Yu
 * @created Jan 18, 2011
 */

public final class ProtostuffSmile {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(BINARY)
        .mode(CODE_FIRST)
        .apiStyle(FIELD_BASED)
        .valueType(POJO)
        .feature(JSON_CONVERTER)
        .name("smile")
        .relatedSerializer("protostuff")
        .projectUrl("https://github.com/FasterXML/smile-format-specification")
        .build();

    // manual (hand-coded schema, no autoboxing)
    groups.media.add(JavaBuiltIn.mediaTransformer, new SmileManualMediaSerializer(properties));

    SerializerProperties runtime_properties = properties.toBuilder()
        .apiStyle(REFLECTION)
        .build();
    // runtime (reflection)
    groups.media
        .add(JavaBuiltIn.mediaTransformer, new SmileRuntimeMediaSerializer(runtime_properties));

    SerializerProperties generated_properties = properties.toBuilder()
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .build();
    // generated code
    groups.media.add(Protostuff.mediaTransformer, new SmileMediaSerializer(generated_properties));

  }

  public static class SmileMediaSerializer extends Serializer<MediaContent> {

    public SmileMediaSerializer(SerializerProperties properties) {
      super(properties);

    }

    public MediaContent deserialize(byte[] array) throws Exception {
      final MediaContent mc = MediaContent.newBuilder().build();
      SmileIOUtil.mergeFrom(array, mc, mc.cachedSchema(), false);
      return mc;
    }

    public byte[] serialize(MediaContent content) throws Exception {
      return SmileIOUtil.toByteArray(content, content.cachedSchema(), false);
    }

  }

  ;

  public static class SmileRuntimeMediaSerializer extends Serializer<data.media.MediaContent> {

    public SmileRuntimeMediaSerializer(SerializerProperties properties) {
      super(properties);

    }

    final Schema<data.media.MediaContent> schema = RuntimeSchema
        .getSchema(data.media.MediaContent.class);

    public data.media.MediaContent deserialize(byte[] array) throws Exception {
      final data.media.MediaContent mc = new data.media.MediaContent();
      SmileIOUtil.mergeFrom(array, mc, schema, false);
      return mc;
    }

    public byte[] serialize(data.media.MediaContent content) throws Exception {
      return SmileIOUtil.toByteArray(content, schema, false);
    }


  }

  ;

  public static class SmileManualMediaSerializer
      extends Serializer<data.media.MediaContent> {

    public SmileManualMediaSerializer(SerializerProperties properties) {
      super(properties);

    }

    public data.media.MediaContent deserialize(byte[] array) throws Exception {
      final data.media.MediaContent mc = new data.media.MediaContent();
      SmileIOUtil.mergeFrom(array, mc, MEDIA_CONTENT_SCHEMA, false);
      return mc;
    }

    public byte[] serialize(data.media.MediaContent content) throws Exception {
      return SmileIOUtil.toByteArray(content, MEDIA_CONTENT_SCHEMA, false);
    }


  }

  ;
}
