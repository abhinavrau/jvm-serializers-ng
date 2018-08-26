package serializers.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import java.io.IOException;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.*;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.BUILDER_PATTERN;

public class JacksonProtobufDatabind
{
    public static void register(MediaContentTestGroup groups) {
        ProtobufMapper mapper = new ProtobufMapper();
        ProtobufSchema schema;
        try {
            schema = new ProtobufSchemaLoader().load(JacksonProtobufDatabind.class.getClassLoader()
                            .getResourceAsStream("schema/media.proto.jackson"),
                    "MediaContent");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // not sure if using (or not) of index actually makes much diff but:
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        final JavaType type = mapper.constructType(MediaContent.class);

        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder.format(BINARY)
                .apiStyle(BUILD_TIME_CODE_GENERATION)
                .mode(SCHEMA_FIRST)
                .valueType(BUILDER_PATTERN)
                .name("protobuf")
                .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
                .feature(SUPPORTS_CYCLIC_REFERENCES)
                .feature(JSON_CONVERTER)
                .projectURL("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/protobuf")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<>(properties, type, mapper,
                        mapper.readerFor(MediaContent.class).with(schema),
                        mapper.writerFor(MediaContent.class).with(schema)));

        SerializerProperties afterburner_properties = properties.toBuilder()
                .feature(OPTIMIZED)
                .build();

        mapper = new ProtobufMapper();
        mapper.registerModule(new AfterburnerModule());
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        
        groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<>(afterburner_properties, type, mapper,
                        mapper.readerFor(MediaContent.class).with(schema),
                        mapper.writerFor(MediaContent.class).with(schema)));
    }
}
