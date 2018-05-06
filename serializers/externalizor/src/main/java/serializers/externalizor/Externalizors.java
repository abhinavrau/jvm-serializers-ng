package serializers.externalizor;

import java.io.*;

import data.media.MediaContent;

import com.qwazr.externalizor.Externalizor;

import serializers.*;

public class Externalizors
{
    public static void register(TestGroups groups)
    {
        groups.media.add(JavaBuiltIn.mediaTransformer, new ExternalizorSerializer<MediaContent>(MediaContent.class),
                new SerFeatures(
                        SerFormat.BIN_CROSSLANG,
                        SerGraph.FULL_GRAPH,
                        SerClass.ZERO_KNOWLEDGE,""
                ) 
        );
	    groups.media.add(JavaBuiltIn.mediaTransformer, new GZIPExternalizorSerializer<MediaContent>(MediaContent.class),
			    new SerFeatures(
					    SerFormat.BIN_CROSSLANG,
					    SerGraph.FULL_GRAPH,
					    SerClass.ZERO_KNOWLEDGE,""
			    )
	    );
    }

    // ------------------------------------------------------------
    // Serializer (just one)

	public final static class ExternalizorSerializer<T> extends Serializer<T>
	{
	    private final Class<T> clz;

	    
	    public ExternalizorSerializer(Class<T> c) { clz = c; }
	    
            public String getName() { return "externalizor"; }

            @SuppressWarnings("unchecked")
            public T deserialize(byte[] array) throws Exception
	    {
		    try (ByteArrayInputStream input = new ByteArrayInputStream(array)) {
			    return Externalizor.deserializeRaw(input, clz);
		    }
	    }

	    public byte[] serialize(T data) throws IOException, ReflectiveOperationException {
		    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			    Externalizor.serializeRaw(data, output);
			    return output.toByteArray();
		    }
	    }


	}

	public final static class GZIPExternalizorSerializer<T> extends Serializer<T>
	{
		private final Class<T> clz;


		public GZIPExternalizorSerializer(Class<T> c) { clz = c; }

		public String getName() { return "externalizor/gzip"; }

		@SuppressWarnings("unchecked")
		public T deserialize(byte[] array) throws Exception
		{
			try (ByteArrayInputStream input = new ByteArrayInputStream(array)) {
				return Externalizor.deserialize(input, clz);
			}
		}

		public byte[] serialize(T data) throws IOException, ReflectiveOperationException {
			try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
				Externalizor.serialize(data, output);
				return output.toByteArray();
			}
		}


	}
}
