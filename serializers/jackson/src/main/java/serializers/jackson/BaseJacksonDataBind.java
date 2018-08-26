package serializers.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import serializers.core.metadata.SerializerProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseJacksonDataBind<T> extends BaseJacksonDriver<T>
{
    protected final JavaType type;
    protected final ObjectMapper mapper;
    protected final ObjectReader reader;
    protected final ObjectWriter writer;

    protected BaseJacksonDataBind(SerializerProperties properties, Class<T> clazz, ObjectMapper mapper)
    {
        super(properties);
        type = mapper.getTypeFactory().constructType(clazz);
        this.mapper = mapper;
        reader = mapper.readerFor(type);
        writer = mapper.writerFor(type);
    }

    protected BaseJacksonDataBind(SerializerProperties properties, JavaType type,
            ObjectMapper mapper, ObjectReader reader, ObjectWriter writer)
    {
        super(properties);
        this.type = type;
        this.mapper = mapper;
        this.reader = reader;
        this.writer = writer;
    }
    
    protected final JsonParser constructParser(byte[] data) throws IOException {
        return mapper.getFactory().createParser(data, 0, data.length);
    }

    protected final JsonParser constructParser(InputStream in) throws IOException {
        return mapper.getFactory().createParser(in);
    }
    
    protected final JsonGenerator constructGenerator(OutputStream out) throws IOException {
        return mapper.getFactory().createGenerator(out, JsonEncoding.UTF8);
    }
}
