package serializers.xml;

import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import javax.xml.stream.*;
import java.io.InputStream;
import java.io.OutputStream;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.Format.XML;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;

/**
 * Codec that works with standard full Stax implementations, where
 * we have Stax input and output factories.
 */
public class XmlStax
{
    /**
     * Since XML streams must still have a single root, we'll use
     * this as the tag
     */
    public final static String STREAM_ROOT = "stream";
    
    public static final Handler[] HANDLERS = new Handler[] {
        new Handler("woodstox",
                new com.ctc.wstx.stax.WstxInputFactory(),
                new com.ctc.wstx.stax.WstxOutputFactory()),
        new Handler("aalto",
                new com.fasterxml.aalto.stax.InputFactoryImpl(),
                new com.fasterxml.aalto.stax.OutputFactoryImpl()),
        new Handler("fastinfo",
                new com.sun.xml.fastinfoset.stax.factory.StAXInputFactory(),
                new com.sun.xml.fastinfoset.stax.factory.StAXOutputFactory()),
    };
    
    public static void register(MediaContentTestGroup groups, boolean woodstox, boolean aalto, boolean fastinfoset)
    {
        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder()
                .format(XML)
                .mode(CODE_FIRST)
                .apiStyle(FIELD_BASED)
                .valueType(NONE)
                .relatedSerializer("stax");


        if (woodstox) {

           SerializerProperties properties = builder
                    .name("woodstox")
                    .projectURL("https://github.com/FasterXML/woodstox")
                    .build();

            groups.media.add(JavaBuiltIn.mediaTransformer, new StaxMediaSerializer(properties, HANDLERS[0]));

        }
        if (aalto) {

            SerializerProperties properties = builder
                    .name("aalto")
                    .projectURL("https://github.com/FasterXML/aalto-xml")
                    .build();

            groups.media.add(JavaBuiltIn.mediaTransformer, new StaxMediaSerializer(properties, HANDLERS[1]));
        }
        if (fastinfoset) {
            SerializerProperties properties = builder
                    .name("fastinfoset")
                    .projectURL("https://github.com/pbielicki/fastinfoset-java")
                    .build();

            groups.media.add(JavaBuiltIn.mediaTransformer, new StaxMediaSerializer(properties, HANDLERS[2]));
        }
    }

    // -------------------------------------------------------------------
    // Implementations

    public static final class Handler
    {
        protected final String name;
        protected final XMLInputFactory inFactory;
        protected final XMLOutputFactory outFactory;

        protected Handler(String name, XMLInputFactory inFactory, XMLOutputFactory outFactory)
        {
            this.name = name;
            this.inFactory = inFactory;
            this.outFactory = outFactory;
        }
    }

    // -------------------------------------------------------------------
    // Serializers

    // Serializer for full Stax implementations
    public static final class StaxMediaSerializer extends BaseStaxMediaSerializer
    {
        private final Handler handler;

        public StaxMediaSerializer(SerializerProperties properties, Handler handler)
        {
            // yes, standard implementations better implement it correctly
            super(properties,false);
            this.handler = handler;
        }

        @Override
        protected XMLStreamReader createReader(InputStream in) throws XMLStreamException {
            return handler.inFactory.createXMLStreamReader(in);
        }

        @Override
        protected XMLStreamWriter createWriter(OutputStream out) throws XMLStreamException {
            return handler.outFactory.createXMLStreamWriter(out, "UTF-8");
        }
    }
}
