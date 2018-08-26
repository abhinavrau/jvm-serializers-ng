package serializers.msgpack;

import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

public class MsgPack
{
    private final static String DESC = 
            "uses positional (column) layout (instead of Maps std impl uses) to eliminate use of names";
    
    public static void register(MediaContentTestGroup groups) {

        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder
                .format(BINARY)
                .mode(CODE_FIRST)
                .apiStyle(FIELD_BASED)
                .valueType(NONE)
                .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
                .feature(JSON_CONVERTER)
                .name("msgpack")
                .projectURL("https://github.com/msgpack/msgpack-java")
                .build();


        groups.media.add(JavaBuiltIn.mediaTransformer, new MsgPackSerializer(properties));

        SerializerProperties jackson_properties = properties.toBuilder()
                .apiStyle(REFLECTION)
                .valueType(POJO)
                .projectURL("https://github.com/msgpack/msgpack-java/tree/develop/msgpack-jackson")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer, new JacksonMsgPackSerializer(jackson_properties));
    }
}