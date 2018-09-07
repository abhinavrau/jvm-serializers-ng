package serializers.jackson;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.avro.Avro;
import serializers.core.metadata.SerializerProperties;

public class JacksonAvroDatabind {

  public static void register(MediaContentTestGroup groups) {
    ObjectMapper mapper = new ObjectMapper(new AvroFactory());
    //mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    JavaType type = mapper.constructType(MediaContent.class);
    AvroSchema schema = new AvroSchema(Avro.Media.sMediaContent);
    ObjectReader reader = mapper.readerFor(type).with(schema);
    ObjectWriter writer = mapper.writerFor(type).with(schema);

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .mode(SCHEMA_FIRST)
        .valueType(POJO)
        .name("avro")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .feature(JSON_CONVERTER)
        .projectUrl("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/avro")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer, new StdJacksonDataBind<>
        (properties, type, mapper, reader, writer));
  }
}
