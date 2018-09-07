package serializers.jackson;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

/**
 * Codec(s) for serializing logical JSON content as JSON Array instead of the usual JSON Object;
 * this condenses data size significantly and typically improves performance similarly.
 */
public class JacksonWithColumnsDatabind {

  private final static String STD_DESC = "uses positional (column) layout to eliminate use of names";

  public static void registerAll(MediaContentTestGroup groups) {
    registerJSON(groups);
    registerSmile(groups);
    registerCBOR(groups);
  }

  public static void registerCBOR(MediaContentTestGroup groups) {
    ObjectMapper cborMapper = new ObjectMapper(
        new com.fasterxml.jackson.dataformat.cbor.CBORFactory());
    cborMapper.setAnnotationIntrospector(new AsArrayIntrospector());

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("cbor")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .feature(JSON_CONVERTER)
        .feature(OPTIMIZED)
        .optimizedDescription(STD_DESC)
        .projectUrl("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/cbor")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new StdJacksonDataBind<>(properties, MediaContent.class, cborMapper));
  }

  public static void registerJSON(MediaContentTestGroup groups) {
    ObjectMapper jsonMapper = new ObjectMapper();
    jsonMapper.setAnnotationIntrospector(new AsArrayIntrospector());

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("jackson")
        .feature(OPTIMIZED)
        .optimizedDescription(STD_DESC)
        .projectUrl("https://github.com/FasterXML/jackson-databind")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new StdJacksonDataBind<>(properties, MediaContent.class, jsonMapper));
  }

  public static void registerSmile(MediaContentTestGroup groups) {
    ObjectMapper smileMapper = new ObjectMapper(
        new com.fasterxml.jackson.dataformat.smile.SmileFactory());
    smileMapper.setAnnotationIntrospector(new AsArrayIntrospector());

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("smile")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(JSON_CONVERTER)
        .feature(OPTIMIZED)
        .optimizedDescription(STD_DESC)
        .projectUrl("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/smile")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new StdJacksonDataBind<>(properties, MediaContent.class, smileMapper));
  }
}
