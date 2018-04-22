package serializers.mongodb;

import com.mongodb.MongoClient;
import data.media.MediaContent;
import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.ByteBufNIO;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.ByteBufferBsonInput;
import org.bson.io.OutputBuffer;
import serializers.*;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB
{
    public static void register(TestGroups groups)
    {
        groups.media.add(JavaBuiltIn.mediaTransformer, new MongoDBSerializer<MediaContent>(MediaContent.class),
                new SerFeatures(
                        SerFormat.BIN_CROSSLANG,
                        SerGraph.FULL_GRAPH,
                        SerClass.ZERO_KNOWLEDGE,"Using new BSON Pojo support"
                ) 
        );
    }

    // ------------------------------------------------------------
    // Serializer (just one)

	public final static class MongoDBSerializer<T> extends Serializer<T>
	{
	    private final Class<T> clz;
		//private final CodecProvider pojoCodecProvider;
		private final CodecRegistry pojoCodecRegistry;
		private final Codec<T> codec;

	    public MongoDBSerializer(Class<T> c) {

			clz = c;

		    pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		    codec = pojoCodecRegistry.get(clz);
	    }
	    
            public String getName() { return "bson/mongodb/PojoCodecProvider"; }

            @SuppressWarnings("unchecked")
            public T deserialize(byte[] array) throws Exception
	    {
		    BsonBinaryReader reader = new BsonBinaryReader(new ByteBufferBsonInput(
		    		new ByteBufNIO(ByteBuffer.wrap(array))));
		    return codec.decode(reader, DecoderContext.builder().build());

	    }

	    public byte[] serialize(T data) throws IOException
	    {
		    OutputBuffer outputBuffer = new BasicOutputBuffer();
		    BsonBinaryWriter bsonWriter = new BsonBinaryWriter(outputBuffer);
		    codec.encode(bsonWriter, data, EncoderContext.builder().build());
		    bsonWriter.close();
		    return outputBuffer.toByteArray();
	    }
	}
}
