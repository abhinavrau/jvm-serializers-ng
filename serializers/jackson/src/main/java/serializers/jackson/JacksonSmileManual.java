package serializers.jackson;

import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;

public class JacksonSmileManual
{
    public static void register(MediaContentTestGroup groups) { // Jackson Smile defaults: share names, not values
        register(groups, true, false);
    }

    public static void register(MediaContentTestGroup groups, boolean sharedNames, boolean sharedValues)
    {
        SmileFactory factory = new SmileFactory();
        factory.configure(SmileGenerator.Feature.CHECK_SHARED_NAMES, sharedNames);
        factory.configure(SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES, sharedValues);SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder.format(BINARY)
                .apiStyle(FIELD_BASED)
                .mode(CODE_FIRST)
                .valueType(NONE)
                .name("smile")
                .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
                .feature(JSON_CONVERTER)
                .projectURL("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/smile")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer, new JacksonJsonManual(properties, factory));
    }

}
