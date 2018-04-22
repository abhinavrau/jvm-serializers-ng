package serializers;

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
 	public abstract String getName();
    
    SerFeatures features = new SerFeatures(); // ruediger: everything misc by default.
    
	public ByteArrayOutputStream outputStream () {
		return new ByteArrayOutputStream(BUFFER_SIZE);
	}

    public SerFeatures getFeatures() {
        return features;
    }

    public void setFeatures(SerFeatures features) {
        this.features = features;
    }


 	// Multi-item interfaces
 	
 	public S[] deserializeItems(InputStream in, int numberOfItems) throws Exception {
 	    throw new UnsupportedOperationException("Not implemented");
 	}

 	public void serializeItems(S[] items, OutputStream out) throws Exception {
            throw new UnsupportedOperationException("Not implemented");
 	}


}
