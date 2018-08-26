package serializers.jboss;

import data.media.MediaContent;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.jboss.serial.util.StringUtilBuffer;
import serializers.JavaBuiltIn;
import serializers.Serializer;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import java.io.*;
import java.lang.reflect.Array;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Format.BINARY_JDK_COMPATIBLE;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

public class JBossSerialization {

	public static void register(final MediaContentTestGroup groups) {

		SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
		SerializerProperties properties = builder.format(BINARY_JDK_COMPATIBLE)
				.apiStyle(REFLECTION)
				.mode(CODE_FIRST)
				.valueType(POJO)
				.name("jboss-serialization")
				.projectURL("http://serialization.jboss.org/")
				.build();

		groups.media.add(
    		JavaBuiltIn.mediaTransformer,
    		new JBossSerializationSerializer<MediaContent>(properties, MediaContent.class));
    }

	private static final class JBossSerializationSerializer<T>
		extends Serializer<T> {

		private final Class<T> clz;

	    public JBossSerializationSerializer(SerializerProperties properties, final Class<T> c) {
	    	super(properties);
	    	clz = c;
    	}


        @Override
		@SuppressWarnings("unchecked")
        public T deserialize(final byte[] array) throws Exception {
	        ByteArrayInputStream bais = new ByteArrayInputStream(array);
	        JBossObjectInputStream jois = new JBossObjectInputStream(
        		bais,
        		new StringUtilBuffer(BUFFER_SIZE, BUFFER_SIZE)
    		);
	        return (T) jois.readObject();
	    }

	    @Override
		public byte[] serialize(final T data) throws IOException {
	        ByteArrayOutputStream baos = outputStream();
	        JBossObjectOutputStream joos = new JBossObjectOutputStream(
        		baos,
        		new StringUtilBuffer(BUFFER_SIZE, BUFFER_SIZE)
    		);
	        joos.writeObject(data);
	        return baos.toByteArray();
	    }

        @Override
        public final void serializeItems(final T[] items, final OutputStream os)
			throws Exception {
        	JBossObjectOutputStream jous = new JBossObjectOutputStream(os);
            for (Object item : items) {
            	jous.writeObject(item);
            }
            jous.flush();
            jous.close();
        }

        @SuppressWarnings("unchecked")
        @Override
        public T[] deserializeItems(final InputStream in, final int numOfItems)
    		throws Exception {
        	JBossObjectInputStream jois = new JBossObjectInputStream(in);
            T[] result = (T[]) Array.newInstance(clz, numOfItems);
            for (int i = 0; i < numOfItems; ++i) {
                result[i] = (T) jois.readObject();
            }
            jois.close();
            return result;
        }
	}
}
