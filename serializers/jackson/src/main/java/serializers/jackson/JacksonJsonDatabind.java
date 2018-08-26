package serializers.jackson;

import data.media.MediaContent;
import serializers.*;

import com.fasterxml.jackson.databind.*;
import serializers.core.metadata.SerializerProperties;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

/**
 * This serializer uses Jackson in full automated data binding mode, which
 * can handle typical Java POJOs (esp. beans; otherwise may need to annotate
 * to configure)
 */
public class JacksonJsonDatabind
{
    public static void register(MediaContentTestGroup groups)
    {
        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
                .apiStyle(REFLECTION)
                .mode(CODE_FIRST)
                .valueType(POJO)
                .name("jackson")
                .projectURL("https://github.com/FasterXML/jackson-databind")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        // note: could also force static typing; left out to keep defaults
        groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<>(properties, MediaContent.class, mapper));
    }
}
