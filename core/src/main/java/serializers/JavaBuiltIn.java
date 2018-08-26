package serializers;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import data.media.MediaTransformer;
import serializers.core.metadata.SerializerProperties;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;

public class JavaBuiltIn
{
	public static void register(MediaContentTestGroup groups)
	{
		SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
		SerializerProperties properties = builder.format(BINARY)
				.apiStyle(REFLECTION)
				.valueType(POJO)
				.name("jdk-serializable")
				.build();

		groups.media.add(mediaTransformer, new GenericSerializer<MediaContent>(properties,true));

		builder = properties.toBuilder();
		SerializerProperties properties2 = builder.feature(SUPPORTS_CYCLIC_REFERENCES).build();
        groups.media.add(mediaTransformer, new GenericSerializer<MediaContent>(properties2,false));
	}

	// ------------------------------------------------------------
	// Serializer (just one)

	public static class GenericSerializer<T> extends Serializer<T>
	{

        boolean unshared;

        public GenericSerializer(SerializerProperties properties, boolean unshared)
		{
			super(properties);
            this.unshared = unshared;
		}

        @Override
		public T deserialize(byte[] array) throws Exception
		{
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(array));
            if ( unshared ) {
                return (T) ois.readUnshared();
            } else {
                return (T) ois.readObject();
            }
		}

		@Override
		public byte[] serialize(T data) throws IOException
		{
			ByteArrayOutputStream baos = outputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
            if ( unshared ) {
                oos.writeUnshared(data);
            } else {
			    oos.writeObject(data);
            }
			return baos.toByteArray();
		}


	}

	// ------------------------------------------------------------
	// MediaTransformer

	public static final MediaTransformer<MediaContent> mediaTransformer = new MediaTransformer<MediaContent>()
	{
		@Override
		public MediaContent[] resultArray(int size) { return new MediaContent[size]; }

		public MediaContent forward(MediaContent mc)
		{
			return copy(mc);
		}

		public MediaContent reverse(MediaContent mc)
		{
			return copy(mc);
		}

		private MediaContent copy(MediaContent mc)
		{
			ArrayList<Image> images = new ArrayList<Image>(mc.images.size());
			for (Image i : mc.images) {
				images.add(new Image(i.uri, i.title, i.width, i.height, i.size));
			}
			return new MediaContent(copy(mc.media), images);
		}

		private Media copy(Media m)
		{
			return new Media(m.uri, m.title, m.width, m.height, m.format, m.duration, m.size, m.bitrate, m.hasBitrate, new ArrayList<String>(m.persons), m.player, m.copyright);
		}

		public MediaContent shallowReverse(MediaContent mc)
		{
			return new MediaContent(copy(mc.media), Collections.<Image>emptyList());
		}
	};
}
