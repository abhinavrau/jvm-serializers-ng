package serializers.avro;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.EMBEDDED_SCHEMA;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_BACKWARD_COMPATIBILITY;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.avro.media.MediaContent;
import serializers.core.metadata.SerializerProperties;

public class AvroSpecific {

  public static void register(MediaContentTestGroup groups) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .mode(SCHEMA_FIRST)
        .valueType(POJO)
        .feature(EMBEDDED_SCHEMA)
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
        .name("avro")
        .projectUrl("https://avro.apache.org/")
        .build();

    groups.media.add(new AvroTransformer(),
        new GenericSerializer<MediaContent>(properties, MediaContent.class));
  }

  private static final DecoderFactory DECODER_FACTORY = DecoderFactory.get();
  private static final EncoderFactory ENCODER_FACTORY = EncoderFactory.get();

  public static final class GenericSerializer<T> extends Serializer<T> {


    private final SpecificDatumReader<T> READER;
    private final SpecificDatumWriter<T> WRITER;

    private BinaryEncoder encoder;
    private BinaryDecoder decoder;

    private final Class<T> clazz;

    public GenericSerializer(SerializerProperties properties, Class<T> clazz) {
      super(properties);
      this.clazz = clazz;
      this.READER = new SpecificDatumReader<T>(clazz);
      this.WRITER = new SpecificDatumWriter<T>(clazz);
    }

    public T deserialize(byte[] array) throws Exception {
      decoder = DECODER_FACTORY.binaryDecoder(array, decoder);
      return READER.read(null, decoder);
    }

    public byte[] serialize(T content) throws Exception {
      ByteArrayOutputStream out = outputStream();
      encoder = ENCODER_FACTORY.binaryEncoder(out, encoder);
      WRITER.write(content, encoder);
      encoder.flush();
      return out.toByteArray();
    }

    @Override
    public void serializeItems(T[] items, OutputStream out) throws IOException {
      encoder = ENCODER_FACTORY.binaryEncoder(out, encoder);
      for (T item : items) {
        WRITER.write(item, encoder);
      }
      encoder.flush();
    }

    @Override
    public T[] deserializeItems(InputStream in0, int numberOfItems) throws IOException {
      decoder = DECODER_FACTORY.binaryDecoder(in0, decoder);
      @SuppressWarnings("unchecked")
      T[] result = (T[]) Array.newInstance(clazz, numberOfItems);
      T item = null;
      for (int i = 0; i < numberOfItems; ++i) {
        result[i] = READER.read(item, decoder);
      }
      return result;
    }
  }
}
