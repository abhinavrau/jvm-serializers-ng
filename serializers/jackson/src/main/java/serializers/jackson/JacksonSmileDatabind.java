package serializers.jackson;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

public class JacksonSmileDatabind {

  public static void register(
      MediaContentTestGroup groups) { // Jackson Smile defaults: share names, not values
    register(groups, true, false);
  }

  public static void register(MediaContentTestGroup groups, boolean sharedNames,
      boolean sharedValues) {
    SmileFactory factory = new SmileFactory();
    factory.configure(SmileGenerator.Feature.CHECK_SHARED_NAMES, sharedNames);
    factory.configure(SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES, sharedValues);
    // no point in using enum names with binary format, so:
    ObjectMapper mapper = new ObjectMapper(factory);
    mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("smile")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(JSON_CONVERTER)
        .projectUrl("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/smile")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new StdJacksonDataBind<>(properties,
            MediaContent.class, mapper));
  }

}
