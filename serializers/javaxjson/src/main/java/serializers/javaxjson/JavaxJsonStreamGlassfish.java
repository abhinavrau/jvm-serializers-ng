package serializers.javaxjson;

import org.glassfish.json.JsonProviderImpl;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;

public class JavaxJsonStreamGlassfish  {
    private static final JsonProviderImpl JSON = new JsonProviderImpl();



    public static void register(MediaContentTestGroup groups) {

        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
                .apiStyle(FIELD_BASED)
                .mode(CODE_FIRST)
                .valueType(NONE)
                .name("glassfish")
                .projectURL("https://github.com/javaee/jsonp/tree/master/impl")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer, new JavaxJsonStream(properties, JSON));
    }
}
