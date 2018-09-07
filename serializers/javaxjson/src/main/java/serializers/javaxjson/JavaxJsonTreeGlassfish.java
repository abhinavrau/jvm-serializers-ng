package serializers.javaxjson;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;

import org.glassfish.json.JsonProviderImpl;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

public class JavaxJsonTreeGlassfish {

  private static final JsonProviderImpl JSON = new JsonProviderImpl();


  public static void register(MediaContentTestGroup groups) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
        .apiStyle(FIELD_BASED)
        .mode(CODE_FIRST)
        .valueType(NONE)
        .name("glassfish")
        .feature(OPTIMIZED)
        .optimizedDescription("using the tree model intermediate")
        .projectUrl("https://github.com/javaee/jsonp/tree/master/impl")
        .build();

    groups.media.add(new JavaxJsonTransformer(JSON), new JavaxJsonTree(properties, JSON));
  }
}
