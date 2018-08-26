package serializers;

import serializers.core.metadata.SerializerProperties;

import java.io.*;

public abstract class Serializer<S>
{
    /**
     * Buffer size for serializers.  Defaults to 1024 and can be changed 
     * via system properties.  Minimum set to 256.
     */
    public static final int BUFFER_SIZE = Math.max(
            Integer.getInteger("buffer_size", 1024), 256);
    
	public abstract S deserialize(byte[] array) throws Exception;
	public abstract byte[] serialize(S content) throws Exception;

	public Serializer(SerializerProperties properties)
	{
		this.serializerProperties = properties;
	}
 	public final String getName()
    {
    	return serializerProperties.getName();
    }

	public  SerializerProperties getProperties()
	{
		return serializerProperties;
	}

	SerializerProperties serializerProperties;

	public  void setProperties(SerializerProperties properties)
	{
		this.serializerProperties = properties;
	}

	public ByteArrayOutputStream outputStream () {
		return new ByteArrayOutputStream(BUFFER_SIZE);
	}

 	// Multi-item interfaces
 	
 	public S[] deserializeItems(InputStream in, int numberOfItems) throws Exception {
 	    throw new UnsupportedOperationException("Not implemented");
 	}

 	public void serializeItems(S[] items, OutputStream out) throws Exception {
            throw new UnsupportedOperationException("Not implemented");
 	}


	@SuppressWarnings("resource")
	public final byte[] serializeAsBytes(S[] items) throws Exception {
		ByteArrayOutputStream bytes = outputStreamForList(items);
		serializeItems(items, bytes);
		return bytes.toByteArray();
	}

	// And then bit bigger default when serializing a list or array
	public ByteArrayOutputStream outputStreamForList (S[] items) {
		return new ByteArrayOutputStream(BUFFER_SIZE * items.length);
	}


}
