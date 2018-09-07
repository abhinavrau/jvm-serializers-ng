package serializers.wobly;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_BACKWARD_COMPATIBILITY;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Features.USER_MANAGED_TYPE_IDS;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO_WITH_ANNOTATIONS;

import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;
import serializers.wobly.compact.WoblyCompactUtils;
import serializers.wobly.simple.WoblySimpleUtils;

public class Wobly {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(BINARY)
        .mode(CODE_FIRST)
        .apiStyle(REFLECTION)
        .valueType(POJO_WITH_ANNOTATIONS)
        .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .feature(USER_MANAGED_TYPE_IDS)
        .name("wobly")
        .projectUrl("https://code.google.com/archive/p/wobly/")
        .build();

    groups.media.add(new WoblySimpleUtils.WoblyTransformer(),
        new WoblySimpleUtils.WoblySerializer(properties));

    SerializerProperties compact_properties = properties.toBuilder()
        .feature(OPTIMIZED)
        .optimizedDescription("compact")
        .build();
    groups.media.add(new WoblyCompactUtils.WoblyTransformer(),
        new WoblyCompactUtils.WoblySerializer(compact_properties));
  }

}
