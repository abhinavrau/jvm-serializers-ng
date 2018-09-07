package serializers.protostuff;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.XML_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Format.XML;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;
import static serializers.protostuff.Protostuff.MEDIA_CONTENT_SCHEMA;

import io.protostuff.Schema;
import io.protostuff.XmlIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;
import serializers.protostuff.media.MediaContent;

public final class ProtostuffXml {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(XML)
        .mode(SCHEMA_FIRST)
        .apiStyle(FIELD_BASED)
        .valueType(NONE)
        .feature(XML_CONVERTER)
        .name("protostuff")
        .projectUrl("https://protostuff.github.io/docs/")
        .build();

    // manual (hand-coded schema, no autoboxing)
    groups.media.add(JavaBuiltIn.mediaTransformer, new XmlManualMediaSerializer(properties));

    SerializerProperties runtime_properties = properties.toBuilder()
        .apiStyle(REFLECTION)
        .valueType(POJO)
        .build();
    // runtime (reflection)
    groups.media
        .add(JavaBuiltIn.mediaTransformer, new XmlRuntimeMediaSerializer(runtime_properties));

        /* protostuff has too many entries

        // generated code
        groups.media.add(Protostuff.MediaTransformer, XmlMediaSerializer);*/
  }

  public static class XmlMediaSerializer extends Serializer<MediaContent> {

    XmlMediaSerializer(SerializerProperties properties) {
      super(properties);
    }

    public MediaContent deserialize(byte[] array) throws Exception {
      MediaContent mc = MediaContent.getDefaultInstance();
      XmlIOUtil.mergeFrom(array, mc, mc.cachedSchema());
      return mc;
    }

    public byte[] serialize(MediaContent content) throws Exception {
      return XmlIOUtil.toByteArray(content, content.cachedSchema());
    }

  }

  public static class XmlRuntimeMediaSerializer extends Serializer<data.media.MediaContent> {

    XmlRuntimeMediaSerializer(SerializerProperties properties) {
      super(properties);
    }


    final Schema<data.media.MediaContent> schema = RuntimeSchema
        .getSchema(data.media.MediaContent.class);

    public data.media.MediaContent deserialize(byte[] array) throws Exception {
      data.media.MediaContent mc = new data.media.MediaContent();
      XmlIOUtil.mergeFrom(array, mc, schema);
      return mc;
    }

    public byte[] serialize(data.media.MediaContent content) throws Exception {
      return XmlIOUtil.toByteArray(content, schema);
    }

  }

  public static class XmlManualMediaSerializer extends Serializer<data.media.MediaContent> {

    XmlManualMediaSerializer(SerializerProperties properties) {
      super(properties);
    }


    public data.media.MediaContent deserialize(byte[] array) throws Exception {
      data.media.MediaContent mc = new data.media.MediaContent();
      XmlIOUtil.mergeFrom(array, mc, MEDIA_CONTENT_SCHEMA);
      return mc;
    }

    public byte[] serialize(data.media.MediaContent content) throws Exception {
      return XmlIOUtil.toByteArray(content, MEDIA_CONTENT_SCHEMA);
    }
  }
}
