package serializers.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.media.MediaContent;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

public class JacksonMsgPackSerializer extends Serializer<MediaContent> {

	ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

	public JacksonMsgPackSerializer(SerializerProperties properties) {
		super(properties);
	}

	@Override
	public MediaContent deserialize(byte[] array) throws Exception {
		MediaContent deserialized = objectMapper.readValue(array, MediaContent.class);
		return deserialized;
	}

	@Override
	public byte[] serialize(MediaContent content) throws Exception {

		byte[] bytes = objectMapper.writeValueAsBytes(content);

		return bytes;
	}

}
