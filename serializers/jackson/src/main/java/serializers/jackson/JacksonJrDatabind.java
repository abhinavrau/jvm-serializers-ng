package serializers.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.jr.ob.JSON;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.Serializer;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

public class JacksonJrDatabind
{
    public static void register(MediaContentTestGroup groups) {

            SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
            SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
                    .apiStyle(REFLECTION)
                    .mode(CODE_FIRST)
                    .valueType(POJO)
                    .name("jacksonJr")
                    .projectURL("https://github.com/FasterXML/jackson-jr")
                    .build();

        groups.media.add(JavaBuiltIn.mediaTransformer, new JrSerializer<MediaContent>(properties, MediaContent.class));
    }

    public final static class JrSerializer<T> extends Serializer<T>
    {
        private final Class<T> type;
        private final JSON json;
        
        protected JrSerializer(SerializerProperties properties , Class<T> t)
        {
            super(properties);
            type = t;
            json = JSON.std;
        }

        @Override
        public T deserialize(byte[] array) throws Exception {
            return json.beanFrom(type, array);
        }

        @Override
        public byte[] serialize(T content) throws Exception {
            return json.asBytes(content);
        }

        @Override
        public final void serializeItems(T[] items, OutputStream out) throws IOException
        {
            JsonGenerator g = json.getStreamingFactory().createGenerator(out);
            // JSON allows simple sequences, so:
            for (int i = 0, len = items.length; i < len; ++i) {
                json.write(items[i], g);
            }
            g.close();
        }

        @Override
        public T[] deserializeItems(InputStream in, int numberOfItems) throws IOException 
        {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) Array.newInstance(type, numberOfItems);
            JsonParser p = json.getStreamingFactory().createParser(in);
            for (int i = 0; i < numberOfItems; ++i) {
                result[i] = json.beanFrom(type, p);
            }
            p.close();
            return result;
        }
    }
}
