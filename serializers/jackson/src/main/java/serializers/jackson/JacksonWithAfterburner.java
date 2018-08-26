package serializers.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.*;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

/**
 * Class for registering variants of Jackson-based tests that use Afterburner
 * for generating byte code to avoid use of Reflection for calling methods
 * and constructing objects.
 */
public class JacksonWithAfterburner
{
    final static String STD_DESC = "uses bytecode generation to reduce overhead";
    
    public static void registerAll(MediaContentTestGroup groups)
    {
        registerJSON(groups);
        registerSmile(groups);
        registerCBOR(groups);
    }

    public static void registerJSON(MediaContentTestGroup groups)
    {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.registerModule(new AfterburnerModule());

        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
                .apiStyle(REFLECTION)
                .mode(CODE_FIRST)
                .valueType(POJO)
                .name("jackson")
                .feature(OPTIMIZED)
                .optimizedDescription(STD_DESC)
                .projectURL("https://github.com/FasterXML/jackson-databind")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<>(properties, MediaContent.class, mapper));
    }
    
    public static void registerCBOR(MediaContentTestGroup groups)
    {
        ObjectMapper mapper = new ObjectMapper(new CBORFactory());
        mapper.registerModule(new AfterburnerModule());

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
                .projectURL("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/cbor")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<MediaContent>(properties, MediaContent.class, mapper));
    }

    public static void registerSmile(MediaContentTestGroup groups) {
        // use defaults: shared names, not values (but set explicitly just in case)
        // sharing can reduce size, but also adds some processing overhead -- typically name sharing
        // cheap, value sharing bit more expensive; hence the defaults
        registerSmile(groups, true, false);
    }

    public static void registerSmile(MediaContentTestGroup groups, boolean shareNames, boolean shareValues)
    {
        SmileFactory f = new SmileFactory();
        f.configure(SmileGenerator.Feature.CHECK_SHARED_NAMES, shareNames);
        f.configure(SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES, shareValues);
        ObjectMapper smileMapper = new ObjectMapper(f);

        smileMapper.registerModule(new AfterburnerModule());

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
                .projectURL("https://github.com/FasterXML/jackson-dataformats-binary/tree/master/smile")
                .build();

                groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<>(properties, MediaContent.class, smileMapper));
    }
}
