package serializers.jackson;

import serializers.*;

import com.fasterxml.aalto.stax.InputFactoryImpl;
import com.fasterxml.aalto.stax.OutputFactoryImpl;

import com.fasterxml.jackson.dataformat.xml.*;

import data.media.MediaContent;
import serializers.core.metadata.SerializerProperties;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

/**
 * Test for handling XML using "serializers.jackson-xml-databind" codec
 * (https://github.com/FasterXML/jackson-xml-databind)
 * with Aalto Stax XML parser.
 */
public class JacksonXmlDatabind
{
    public static void register(MediaContentTestGroup groups)
    {
        XmlMapper mapper = new XmlMapper(new XmlFactory(
                new InputFactoryImpl(), new OutputFactoryImpl()));

        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder.format(SerializerProperties.Format.XML)
                .apiStyle(REFLECTION)
                .mode(CODE_FIRST)
                .valueType(POJO)
                .name("jackson")
                .feature(JSON_CONVERTER)
                .projectURL("https://github.com/FasterXML/jackson-dataformat-xml")
                .build();

        groups.media.add(JavaBuiltIn.mediaTransformer,
                new StdJacksonDataBind<>(properties,
                        MediaContent.class, mapper));
    }
}
