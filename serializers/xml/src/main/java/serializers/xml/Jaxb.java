package serializers.xml;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.XML;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import data.media.MediaContent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

public class Jaxb<T> extends Serializer<T> {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(XML)
        .mode(CODE_FIRST)
        .apiStyle(REFLECTION)
        .valueType(POJO)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .name("jaxb")
        .projectUrl("https://github.com/javaee/jaxb-v2")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new Jaxb<>(properties, MediaContent.class));
  }

  private final JAXBContext jaxbContext;

  public Jaxb(SerializerProperties properties, Class<T> clazz) {
    super(properties);
    try {
      jaxbContext = JAXBContext.newInstance(MediaContent.class);
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public byte[] serialize(T data) throws IOException {
    ByteArrayOutputStream baos = outputStream();
    try {
      jaxbContext.createMarshaller().marshal(data, baos);
    } catch (Exception e) {
      throw new IOException(e);
    }
    return baos.toByteArray();
  }

  @Override
  public T deserialize(byte[] data) throws Exception {
    try {
      @SuppressWarnings("unchecked")
      T result = (T) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(data));
      return result;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
