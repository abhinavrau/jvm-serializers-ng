package serializers.javaxjson;

import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Base class for javax.json benchmark using the tree model intermediate
 */
public  class JavaxJsonTree extends Serializer<JsonStructure> {
    private final JsonProvider json;

    public JavaxJsonTree(SerializerProperties properties, JsonProvider json) {
    	super(properties);
        this.json = json;
    }

    @Override
    public JsonStructure deserialize(byte[] array) throws Exception {
    	return json.createReader(new ByteArrayInputStream(array)).read();
    }

    @Override
    public byte[] serialize(JsonStructure content) throws Exception {
    	ByteArrayOutputStream outputStream = outputStream();
    	JsonWriter writer = json.createWriter(outputStream);
    	writer.write(content);
    	writer.close();
    	return outputStream.toByteArray();
    }

}