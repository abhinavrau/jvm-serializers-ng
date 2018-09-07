package serializers.obser;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import net.sockali.obser.ObserEncoding;
import net.sockali.obser.ObserFactory;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.TestGroup;
import serializers.Transformer;
import serializers.core.metadata.SerializerProperties;

@SuppressWarnings("unchecked")
public class Obser {

  public static void register(MediaContentTestGroup groups) {
    register(groups.media, JavaBuiltIn.mediaTransformer);
  }

  private static <T, S> void register(TestGroup<?> group, Transformer transformer) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(BINARY)
        .mode(CODE_FIRST)
        .apiStyle(REFLECTION)
        .valueType(POJO)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .name("obser")
        .projectUrl("http://www.sockali.net/obser/")
        .build();

    group.add(transformer, new BasicSerializer<S>(properties));

    SerializerProperties compact_properties = properties.toBuilder()
        .feature(OPTIMIZED)
        .optimizedDescription("compact")
        .build();

    group.add(transformer, new CompactSerializer<S>(compact_properties));

    SerializerProperties custom_properties = properties.toBuilder()
        .feature(OPTIMIZED)
        .optimizedDescription("custom")
        .build();

    group.add(transformer, new CustomSerializer(custom_properties));

    SerializerProperties compactCustom_properties = properties.toBuilder()
        .feature(OPTIMIZED)
        .optimizedDescription("custom+compact")
        .build();

    group.add(transformer, new CustomCompactSerializer(compactCustom_properties));
  }

  // ------------------------------------------------------------
  // Serializers

  public static class BasicSerializer<T> extends Serializer<T> {

    final net.sockali.obser.Obser obser;
    //		private final byte[] buffer = new byte[1024*1024];
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);

    public BasicSerializer(SerializerProperties properties) {
      super(properties);
      this.obser = ObserFactory.createObser(ObserEncoding.nativeEncoding());
      obser.registerClass(MediaContentCustom.class);
      obser.registerClass(MediaContent.class);
      obser.registerClass(Media.class);
      obser.registerClass(Media.Player.class);
      obser.registerClass(Image.class);
      obser.registerClass(Image.Size.class);
      obser.registerClass(ArrayList.class);
      obser.registerClass(MediaContent[].class);
      obser.registerClass(MediaContentCustom[].class);
    }

    @Override
    public T deserialize(byte[] array) {
      buffer.position(0);
      buffer.put(array);

      return (T) obser.deserialize(buffer, 0);
    }

    @Override
    public byte[] serialize(T content) {
      buffer.position(0);
      int pos = obser.serialize(content, buffer, 0);
      byte[] ret = new byte[pos];
      buffer.get(ret);
      return ret;
    }

    @Override
    public void serializeItems(T[] items, OutputStream outStream) throws Exception {
      obser.serialize(items, outStream);
    }

    @Override
    public T[] deserializeItems(InputStream inStream, int numberOfItems) throws IOException {
      return obser.deserialize(inStream);
    }

  }

  public static class CompactSerializer<T> extends BasicSerializer<T> {

    public CompactSerializer(SerializerProperties properties) {
      super(properties);
      obser.setEncoding(ObserEncoding.nativeCompactEncoding());
    }
  }

  public static class CustomSerializer extends BasicSerializer<Object> {

    public CustomSerializer(SerializerProperties properties) {
      super(properties);
    }

    @Override
    public byte[] serialize(Object content) {
      MediaContentCustom mcc = new MediaContentCustom((MediaContent) content);
      return super.serialize(mcc);
    }

    @Override
    public MediaContent deserialize(byte[] array) {
      MediaContentCustom mcc = (MediaContentCustom) ((Object) super.deserialize(array));
      return mcc.getContent();
    }

    @Override
    public void serializeItems(Object[] items, OutputStream outStream) throws Exception {
      MediaContentCustom[] data = new MediaContentCustom[items.length];
      for (int i = 0; i < items.length; i++) {
        data[i] = new MediaContentCustom((MediaContent) items[i]);
      }
      super.serializeItems((MediaContent[]) (Object) data, outStream);
    }

    @Override
    public MediaContent[] deserializeItems(InputStream inStream, int numberOfItems)
        throws IOException {
      MediaContentCustom[] data = (MediaContentCustom[]) (Object) super
          .deserializeItems(inStream, numberOfItems);
      MediaContent[] items = new MediaContent[data.length];
      for (int i = 0; i < data.length; i++) {
        items[i] = data[i].getContent();
      }

      return items;
    }
  }

  public static class CustomCompactSerializer extends CustomSerializer {

    public CustomCompactSerializer(SerializerProperties properties) {
      super(properties);
      obser.setEncoding(ObserEncoding.nativeCompactEncoding());
    }

  }
}
