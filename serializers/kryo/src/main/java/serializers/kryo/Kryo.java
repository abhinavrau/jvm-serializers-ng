
package serializers.kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import data.media.Image;
import data.media.Image.Size;
import data.media.Media;
import data.media.MediaContent;
import serializers.*;
import serializers.core.metadata.SerializerProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.*;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

public class Kryo {
    
	public static void register (MediaContentTestGroup groups) {
		register(groups.media, JavaBuiltIn.mediaTransformer, MediaTypeHandler);
	}
	
	private static <T, S> void register (TestGroup<T> group, Transformer<T, S> transformer, TypeHandler<S> handler) {

		SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
		SerializerProperties properties = builder.format(BINARY)
				.apiStyle(REFLECTION)
				.mode(CODE_FIRST)
				.valueType(POJO)
				.name("kryo")
				.projectURL("https://github.com/EsotericSoftware/kryo")
				.build();
		group.add(transformer, new DefaultSerializer<>(properties, handler, false));

		SerializerProperties withReferences_properties = properties.toBuilder()
				.feature(SUPPORTS_CYCLIC_REFERENCES)
				.build();
		group.add(transformer, new DefaultSerializer<>(withReferences_properties, handler, true));

		SerializerProperties typeid_properties = properties.toBuilder()
				.feature(USER_MANAGED_TYPE_IDS)
				.build();
		group.add(transformer, new BasicSerializer<>(typeid_properties, handler));

		SerializerProperties optimized_properties = properties.toBuilder()
				.feature(OPTIMIZED)
				.optimizedDescription("nullable fields")
				.build();
		group.add(transformer, new OptimizedSerializer<>(optimized_properties, handler));

		SerializerProperties field_based_properties = properties.toBuilder()
				.apiStyle(FIELD_BASED)
				.build();
		group.add(transformer, new CustomSerializer<>(field_based_properties, handler));
	}

	// ------------------------------------------------------------
	// Serializers

    /** This is the most basic Kryo usage. Don't register anything go. */
    public static class DefaultSerializer<T> extends Serializer<T> {
        final com.esotericsoftware.kryo.Kryo kryo;

        private final byte[] buffer = new byte[BUFFER_SIZE];
        private final Output output = new Output(buffer, -1);
        private final Input input = new Input(buffer);
        private final Class<T> type;
        boolean shared;

        public DefaultSerializer (SerializerProperties properties, TypeHandler<T> handler,boolean shared) {
            super(properties);
            this.type = handler.type;
            this.shared = shared;
            this.kryo = new com.esotericsoftware.kryo.Kryo();
            kryo.setReferences(shared);
            kryo.setRegistrationRequired(false);
        }

        @SuppressWarnings("unchecked")
        public T deserialize (byte[] array) {
            input.setBuffer(array);
            return (T) kryo.readObject(input, type);
        }

        public byte[] serialize (T content) {
            output.setBuffer(buffer, -1);
            kryo.writeObject(output, content);
            return output.toBytes();
        }

        public void serializeItems (T[] items, OutputStream outStream) throws Exception {
            output.setOutputStream(outStream);
            for (int i = 0, n = items.length; i < n; ++i) {
                kryo.writeClassAndObject(output, items[i]);
            }
            output.flush();
        }

        @SuppressWarnings("unchecked")
        public T[] deserializeItems (InputStream inStream, int numberOfItems) throws IOException {
            input.setInputStream(inStream);
            MediaContent[] result = new MediaContent[numberOfItems];
            for (int i = 0; i < numberOfItems; ++i) {
                result[i] = (MediaContent) kryo.readClassAndObject(input);
            }
            return (T[])result;
        }

    }

	/** This is slightly advanced Kryo usage. Just register the classes and go. */
	public static class BasicSerializer<T> extends Serializer<T> {
		private final Class<T> type;
		final com.esotericsoftware.kryo.Kryo kryo;

		private final byte[] buffer = new byte[BUFFER_SIZE];
		private final Output output = new Output(buffer, -1);
		private final Input input = new Input(buffer);

		public BasicSerializer (SerializerProperties properties, TypeHandler<T> handler) {
			super(properties);
			this.type = handler.type;
			this.kryo = new com.esotericsoftware.kryo.Kryo();
			kryo.setReferences(false);
			kryo.setRegistrationRequired(true);

			handler.register(this.kryo);
		}

		public T deserialize (byte[] array) {
			input.setBuffer(array);
			return kryo.readObject(input, type);
		}

		public byte[] serialize (T content) {
			output.setBuffer(buffer, -1);
			kryo.writeObject(output, content);
			return output.toBytes();
		}

		public void serializeItems (T[] items, OutputStream outStream) throws Exception {
			output.setOutputStream(outStream);
			for (int i = 0, n = items.length; i < n; ++i) {
				kryo.writeObject(output, items[i]);
			}
			output.flush();
		}

		@SuppressWarnings("unchecked")
		public T[] deserializeItems (InputStream inStream, int numberOfItems) throws IOException {
			input.setInputStream(inStream);
			MediaContent[] result = new MediaContent[numberOfItems];
			for (int i = 0; i < numberOfItems; ++i) {
				result[i] = kryo.readObject(input, MediaContent.class);
			}
			return (T[])result;
		}
	}

	/** This shows how to configure individual Kryo serializersto reduce the serialized bytes. */
	public static class OptimizedSerializer<T> extends BasicSerializer<T> {
		public OptimizedSerializer (SerializerProperties properties, TypeHandler<T> handler) {
			super(properties,handler);
			handler.optimize(this.kryo);
		}
	}

	/** This shows how to use hand written serialization code with Kryo, while still leveraging Kryo for most of the work. A
	 * serializer for each class can be implemented, as it is here, or the classes to be serialized can implement an interface and
	 * host their own serialization code (similar to java.io.Externalizable). */
	public static class CustomSerializer<T> extends BasicSerializer<T> {
		public CustomSerializer (SerializerProperties properties, TypeHandler<T> handler) {
			super(properties, handler);
			handler.registerCustom(this.kryo);
		}
	}

	// ------------------------------------------------------------

	public static abstract class TypeHandler<T> {
		public final Class<T> type;

		protected TypeHandler (Class<T> type) {
			this.type = type;
		}

		public abstract void register (com.esotericsoftware.kryo.Kryo kryo);

		public abstract void optimize (com.esotericsoftware.kryo.Kryo kryo);

		public abstract void registerCustom (com.esotericsoftware.kryo.Kryo kryo);
	}

	// ------------------------------------------------------------
	// Media

	public static final TypeHandler<MediaContent> MediaTypeHandler = new TypeHandler<MediaContent>(MediaContent.class) {
		public void register (com.esotericsoftware.kryo.Kryo kryo) {
			kryo.register(ArrayList.class);
			kryo.register(MediaContent.class);
			kryo.register(Media.Player.class);
			kryo.register(Media.class);
			kryo.register(Image.Size.class);
			kryo.register(Image.class);
		}

		@SuppressWarnings("rawtypes")
        public void optimize (com.esotericsoftware.kryo.Kryo kryo) {
			FieldSerializer imageSerializer = (FieldSerializer)kryo.getSerializer(Image.class);
			imageSerializer.setFieldsCanBeNull(false);
			imageSerializer.getField("title").setCanBeNull(true);

			FieldSerializer mediaContentSerializer = (FieldSerializer)kryo.getSerializer(MediaContent.class);
			mediaContentSerializer.setFieldsCanBeNull(false);

			CachedField imagesField = mediaContentSerializer.getField("images");
			CollectionSerializer imagesSerializer = new CollectionSerializer();
			imagesSerializer.setElementsCanBeNull(false);
			imagesField.setClass(ArrayList.class, imagesSerializer);

			FieldSerializer mediaSerializer = new FieldSerializer(kryo, Media.class);
			mediaSerializer.setFieldsCanBeNull(false);
			mediaSerializer.getField("title").setCanBeNull(true);
			mediaSerializer.getField("copyright").setCanBeNull(true);

			CachedField mediaField = mediaContentSerializer.getField("media");
			mediaField.setClass(Media.class, mediaSerializer);

			CachedField personsField = mediaSerializer.getField("persons");
			CollectionSerializer personsSerializer = new CollectionSerializer();
			personsSerializer.setElementsCanBeNull(false);
			personsField.setClass(ArrayList.class, personsSerializer);
		}

		public void registerCustom (com.esotericsoftware.kryo.Kryo kryo) {
			kryo.register(Image.class, new ImageSerializer());
			kryo.register(MediaContent.class, new MediaContentSerializer(kryo));
			kryo.register(Media.class, new MediaSerializer(kryo));
		}
	};

	static class MediaContentSerializer extends com.esotericsoftware.kryo.Serializer<MediaContent> {
		private CollectionSerializer _imagesSerializer;

		public MediaContentSerializer (com.esotericsoftware.kryo.Kryo kryo) {
			_imagesSerializer = new CollectionSerializer();
			_imagesSerializer.setElementsCanBeNull(false);
		}

		public MediaContent read (com.esotericsoftware.kryo.Kryo kryo, Input input, Class<MediaContent> type) {
			final Media media = kryo.readObject(input, Media.class);
			@SuppressWarnings("unchecked")
			final List<Image> images = (List<Image>)kryo.readObject(input, ArrayList.class, _imagesSerializer);
			return new MediaContent(media, images);
		}

		public void write (com.esotericsoftware.kryo.Kryo kryo, Output output, MediaContent obj) {
			kryo.writeObject(output, obj.media);
			kryo.writeObject(output, obj.images, _imagesSerializer);
		}
	}

	static class MediaSerializer extends com.esotericsoftware.kryo.Serializer<Media> {
		private final CollectionSerializer _personsSerializer;

		public MediaSerializer (final com.esotericsoftware.kryo.Kryo kryo) {
			_personsSerializer = new CollectionSerializer();
			_personsSerializer.setElementsCanBeNull(false);
		}

		@SuppressWarnings("unchecked")
		public Media read (com.esotericsoftware.kryo.Kryo kryo, Input input, Class<Media> type) {
			return new Media(input.readString(), input.readString(), input.readInt(true), input.readInt(true), input.readString(),
				input.readLong(true), input.readLong(true), input.readInt(true), input.readBoolean(), (List<String>)kryo.readObject(
					input, ArrayList.class, _personsSerializer), kryo.readObject(input, Media.Player.class), input.readString());
		}

		public void write (com.esotericsoftware.kryo.Kryo kryo, Output output, Media obj) {
			output.writeString(obj.uri);
			output.writeString(obj.title);
			output.writeInt(obj.width, true);
			output.writeInt(obj.height, true);
			output.writeString(obj.format);
			output.writeLong(obj.duration, true);
			output.writeLong(obj.size, true);
			output.writeInt(obj.bitrate, true);
			output.writeBoolean(obj.hasBitrate);
			kryo.writeObject(output, obj.persons, _personsSerializer);
			kryo.writeObject(output, obj.player);
			output.writeString(obj.copyright);
		}
	}

	static class ImageSerializer extends com.esotericsoftware.kryo.Serializer<Image> {
		public Image read (com.esotericsoftware.kryo.Kryo kryo, Input input, Class<Image> type) {
			return new Image(input.readString(), input.readString(), input.readInt(true), input.readInt(true), kryo.readObject(
				input, Size.class));
		}

		public void write (com.esotericsoftware.kryo.Kryo kryo, Output output, Image obj) {
			output.writeString(obj.uri);
			output.writeString(obj.title);
			output.writeInt(obj.width, true);
			output.writeInt(obj.height, true);
			kryo.writeObject(output, obj.size);
		}
	}
}
