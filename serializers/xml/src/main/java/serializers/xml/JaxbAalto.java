package serializers.xml;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.XML;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import com.fasterxml.aalto.stax.InputFactoryImpl;
import com.fasterxml.aalto.stax.OutputFactoryImpl;
import data.media.MediaContent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

public class JaxbAalto<T> extends Serializer<T> {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(XML)
        .mode(CODE_FIRST)
        .apiStyle(REFLECTION)
        .valueType(POJO)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .name("jaxb-aalto")
        .projectUrl("https://github.com/FasterXML/aalto-xml")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new JaxbAalto<>(properties, MediaContent.class,
            new InputFactoryImpl(), new OutputFactoryImpl()));
  }

  private final XMLInputFactory inputFactory;
  private final XMLOutputFactory outputFactory;
  private final JAXBContext jaxbContext;

  @SuppressWarnings("UnusedParameters")
  public JaxbAalto(SerializerProperties properties, Class<T> clazz,
      XMLInputFactory inputF, XMLOutputFactory outputF) {
    super(properties);
    inputFactory = inputF;
    outputFactory = outputF;
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
      XMLStreamWriter sw = outputFactory.createXMLStreamWriter(baos, "UTF-8");
      jaxbContext.createMarshaller().marshal(data, sw);
      sw.close();
    } catch (Exception e) {
      throw new IOException(e);
    }
    return baos.toByteArray();
  }

  @Override
  public T deserialize(byte[] data) throws Exception {
    try {
      XMLStreamReader sr = inputFactory.createXMLStreamReader(new ByteArrayInputStream(data));
      @SuppressWarnings("unchecked")
      T result = (T) jaxbContext.createUnmarshaller().unmarshal(sr);
      sr.close();
      return result;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
