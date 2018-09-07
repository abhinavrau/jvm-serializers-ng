package serializers.hessian;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.Hessian2StreamingOutput;
import data.media.MediaContent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

public class Hessian {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("hessian")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .projectUrl("http://hessian.caucho.com/")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new HessianSerializer<MediaContent>(properties, MediaContent.class));
  }

  // ------------------------------------------------------------
  // Serializer (just one)

  public final static class HessianSerializer<T> extends Serializer<T> {

    private final Class<T> clz;

    public HessianSerializer(SerializerProperties properties, Class<T> c) {
      super(properties);
      clz = c;
    }


    @SuppressWarnings("unchecked")
    public T deserialize(byte[] array) throws Exception {
      ByteArrayInputStream in = new ByteArrayInputStream(array);
      Hessian2StreamingInput hin = new Hessian2StreamingInput(in);
      return (T) hin.readObject();
    }

    public byte[] serialize(T data) throws IOException {
      ByteArrayOutputStream out = outputStream();
      Hessian2StreamingOutput hout = new Hessian2StreamingOutput(out);
      hout.writeObject(data);
      return out.toByteArray();
    }

    @Override
    public final void serializeItems(T[] items, OutputStream out) throws Exception {
      Hessian2StreamingOutput hout = new Hessian2StreamingOutput(out);
      for (Object item : items) {
        hout.writeObject(item);
      }
      hout.flush();
      hout.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] deserializeItems(InputStream in, int numberOfItems) throws Exception {
      Hessian2StreamingInput hin = new Hessian2StreamingInput(in);
      T[] result = (T[]) Array.newInstance(clz, numberOfItems);
      for (int i = 0; i < numberOfItems; ++i) {
        result[i] = (T) hin.readObject();
      }
      hin.close();
      return result;
    }
  }
}
