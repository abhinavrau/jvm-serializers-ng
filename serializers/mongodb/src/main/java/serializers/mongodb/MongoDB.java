package serializers.mongodb;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.mongodb.MongoClient;
import data.media.MediaContent;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.ByteBufNIO;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.ByteBufferBsonInput;
import org.bson.io.OutputBuffer;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

public class MongoDB {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("mongodb")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .projectUrl("http://bsonspec.org/")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new MongoDBSerializer<MediaContent>(properties, MediaContent.class));
  }

  // ------------------------------------------------------------
  // Serializer (just one)

  public final static class MongoDBSerializer<T> extends Serializer<T> {

    private final Class<T> clz;
    //private final CodecProvider pojoCodecProvider;
    private final CodecRegistry pojoCodecRegistry;
    private final Codec<T> codec;

    public MongoDBSerializer(SerializerProperties properties, Class<T> c) {
      super(properties);
      clz = c;

      pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
          fromProviders(PojoCodecProvider.builder().automatic(true).build()));

      codec = pojoCodecRegistry.get(clz);
    }

    @SuppressWarnings("unchecked")
    public T deserialize(byte[] array) throws Exception {
      BsonBinaryReader reader = new BsonBinaryReader(new ByteBufferBsonInput(
          new ByteBufNIO(ByteBuffer.wrap(array))));
      return codec.decode(reader, DecoderContext.builder().build());

    }

    public byte[] serialize(T data) throws IOException {
      OutputBuffer outputBuffer = new BasicOutputBuffer();
      BsonBinaryWriter bsonWriter = new BsonBinaryWriter(outputBuffer);
      codec.encode(bsonWriter, data, EncoderContext.builder().build());
      bsonWriter.close();
      return outputBuffer.toByteArray();
    }
  }
}
