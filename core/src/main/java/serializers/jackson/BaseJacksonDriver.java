package serializers.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

abstract class BaseJacksonDriver<T> extends Serializer<T> {

  public BaseJacksonDriver(SerializerProperties properties) {
    super(properties);
  }

  @Override
  public abstract byte[] serialize(T data) throws IOException;

  @Override
  public abstract T deserialize(byte[] array) throws IOException;

  // // Future extensions for testing performance for item sequences

  @Override
  public abstract void serializeItems(T[] items, OutputStream out) throws Exception;

  @Override
  public abstract T[] deserializeItems(InputStream in, int numberOfItems) throws IOException;
}
