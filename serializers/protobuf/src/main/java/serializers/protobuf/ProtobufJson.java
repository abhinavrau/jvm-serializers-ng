package serializers.protobuf;

import static com.google.protobuf.TextFormat.merge;
import static com.google.protobuf.TextFormat.printToString;
import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_BACKWARD_COMPATIBILITY;
import static serializers.core.metadata.SerializerProperties.Format.JSON;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.BUILDER_PATTERN;

import java.io.IOException;
import java.nio.charset.Charset;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;
import serializers.protobuf.media.MediaContentHolder.MediaContent;

public class ProtobufJson {

  public static void register(MediaContentTestGroup groups) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(JSON)
        .mode(SCHEMA_FIRST)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .valueType(BUILDER_PATTERN)
        .feature(JSON_CONVERTER)
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
        .name("protobuf")
        .projectUrl("https://github.com/google/protobuf/tree/master/java")
        .build();

    groups.media.add(new Protobuf.Transformer(), new JsonSerializer(properties));
  }

  private static final Charset _charset = Charset.forName("UTF-8");

  static final class JsonSerializer extends Serializer<MediaContent> {

    JsonSerializer(SerializerProperties properties) {
      super(properties);
    }

    public MediaContent deserialize(byte[] array) throws Exception {
      MediaContent.Builder builder = MediaContent.newBuilder();
      merge(new String(array, _charset.name()), builder);
      return builder.build();
    }

    public byte[] serialize(MediaContent content) throws IOException {
      return printToString(content).getBytes(_charset.name());
    }

  }
}
