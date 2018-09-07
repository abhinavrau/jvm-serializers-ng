package serializers.jackson;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

public class JacksonCBORDatabind {

  public static void register(
      MediaContentTestGroup groups) { // Jackson Smile defaults: share names, not values
    register(groups, true, false);
  }

  public static void register(MediaContentTestGroup groups, boolean sharedNames,
      boolean sharedValues) {
    CBORFactory factory = new CBORFactory();
    // no point in using enum names with binary format, so:
    ObjectMapper mapper = new ObjectMapper(factory);

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("cbor")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .feature(JSON_CONVERTER)
        .projectUrl("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/cbor")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new StdJacksonDataBind<MediaContent>(properties, MediaContent.class, mapper));

    SerializerProperties optimized_properties = properties.toBuilder()
        .apiStyle(FIELD_BASED)
        .valueType(NONE)
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new JacksonJsonManual(optimized_properties, factory));
  }

}
