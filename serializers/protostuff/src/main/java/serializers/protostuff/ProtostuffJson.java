package serializers.protostuff;

import io.protostuff.JsonIOUtil;
import io.protostuff.JsonXIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import serializers.JavaBuiltIn;
import serializers.Serializer;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;
import serializers.protostuff.media.MediaContent;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.XML_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Format.JSON;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;
import static serializers.protostuff.Protostuff.MEDIA_CONTENT_SCHEMA;

/**
 * @author David Yu
 * @created Oct 26, 2009
 */

public final class ProtostuffJson
{

    public static void register(MediaContentTestGroup groups)
    {
        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder
                .format(JSON)
                .mode(SCHEMA_FIRST)
                .apiStyle(FIELD_BASED)
                .valueType(NONE)
                .feature(XML_CONVERTER)
                .name("protostuff")
                .projectURL("https://protostuff.github.io/docs/")
                .build();

        // manual (hand-coded schema, no autoboxing)
        groups.media.add(JavaBuiltIn.mediaTransformer, new JsonManualMediaSerializer(properties));

        SerializerProperties runtime_properties = properties.toBuilder()
                .apiStyle(REFLECTION)
                .valueType(POJO)
                .build();
        // runtime (reflection)
        groups.media.add(JavaBuiltIn.mediaTransformer, new JsonRuntimeMediaSerializer(runtime_properties));
        
        /* protostuff has too many entries

        // generated code
        groups.media.add(Protostuff.mediaTransformer, JsonMediaSerializer,
                new SerFeatures(
                        SerFormat.JSON,
                        SerGraph.FLAT_TREE,
                        SerClass.CLASSES_KNOWN,
                        "json + generated code"
                )
        );

        // generated code, numeric
        groups.media.add(Protostuff.mediaTransformer, JsonMediaSerializerNumeric,
                new SerFeatures(
                        SerFormat.JSON,
                        SerGraph.FLAT_TREE,
                        SerClass.CLASSES_KNOWN,
                        "json + numeric + generated code"
                )
        );
        
        // manual, numeric (hand-coded schema, no autoboxing)
        groups.media.add(JavaBuiltIn.mediaTransformer, JsonManualMediaSerializerNumeric,
                new SerFeatures(
                        SerFormat.JSON,
                        SerGraph.FLAT_TREE,
                        SerClass.MANUAL_OPT,
                        "json + numeric + manual"
                )
        );
        
        // runtime, numeric (reflection)
        groups.media.add(JavaBuiltIn.mediaTransformer, JsonRuntimeMediaSerializerNumeric,
                new SerFeatures(
                        SerFormat.JSON,
                        SerGraph.FLAT_TREE,
                        SerClass.ZERO_KNOWLEDGE,
                        "json + numeric + reflection"
                )
        );
        */
    }

    // Normal
    public static class JsonMediaSerializer extends Serializer<MediaContent>
    {

        JsonMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }
        public MediaContent deserialize(byte[] array) throws Exception
        {
            MediaContent mc = MediaContent.getDefaultInstance();
            JsonIOUtil.mergeFrom(array, mc, mc.cachedSchema(), false);
            return mc;
        }

        public byte[] serialize(MediaContent content) throws Exception
        {
            return JsonIOUtil.toByteArray(content, content.cachedSchema(), false);
        }

    }

    // Numeric
    public static class JsonMediaSerializerNumeric extends Serializer<MediaContent>
    {
        JsonMediaSerializerNumeric(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(512);

        public MediaContent deserialize(byte[] array) throws Exception
        {
            MediaContent mc = MediaContent.getDefaultInstance();
            JsonIOUtil.mergeFrom(array, mc, mc.cachedSchema(), true);
            return mc;
        }

        public byte[] serialize(MediaContent content) throws Exception
        {
            try
            {
                return JsonXIOUtil.toByteArray(content, content.cachedSchema(), true, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

    }

    public static class JsonRuntimeMediaSerializer extends Serializer<data.media.MediaContent>
    {

        JsonRuntimeMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }
	final Schema<data.media.MediaContent> schema = RuntimeSchema.getSchema(data.media.MediaContent.class);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            JsonIOUtil.mergeFrom(array, mc, schema, false);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            return JsonIOUtil.toByteArray(content, schema, false);
        }
    }

    public static class JsonRuntimeMediaSerializerNumeric extends Serializer<data.media.MediaContent>
    {

        JsonRuntimeMediaSerializerNumeric(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(512);

	    final Schema<data.media.MediaContent> schema = RuntimeSchema.getSchema(data.media.MediaContent.class);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            JsonIOUtil.mergeFrom(array, mc, schema, true);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return JsonXIOUtil.toByteArray(content, schema, true, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

        
    }

    public static class JsonManualMediaSerializer extends Serializer<data.media.MediaContent>
    {

        JsonManualMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }
        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            JsonIOUtil.mergeFrom(array, mc, MEDIA_CONTENT_SCHEMA, false);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            return JsonIOUtil.toByteArray(content, MEDIA_CONTENT_SCHEMA, false);
        }
        
    }

    public static class JsonManualMediaSerializerNumeric extends Serializer<data.media.MediaContent>
    {

        JsonManualMediaSerializerNumeric(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(512);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            JsonIOUtil.mergeFrom(array, mc, MEDIA_CONTENT_SCHEMA, true);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return JsonXIOUtil.toByteArray(content, MEDIA_CONTENT_SCHEMA, true, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }
        
    }
}
