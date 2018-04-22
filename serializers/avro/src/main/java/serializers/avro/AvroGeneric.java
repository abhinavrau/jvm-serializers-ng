package serializers.avro;

import data.media.*;

import org.apache.avro.Schema;
import org.apache.avro.generic.*;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.BinaryDecoder;

import serializers.*;
import serializers.avro.media.player;
import serializers.avro.media.size;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvroGeneric
{
	public static void register(TestGroups groups)
	{
		groups.media.add(MediaTransformer, new GenericSerializer(Avro.Media.sMediaContent),
                new SerFeatures(
                        SerFormat.BIN_CROSSLANG,
                        SerGraph.FLAT_TREE,
                        SerClass.MANUAL_OPT,
                        ""
                )
        );
	}


	// ------------------------------------------------------------
	// Serializer (just one class)

	private static final DecoderFactory DECODER_FACTORY
          = DecoderFactory.get();
	private static final EncoderFactory ENCODER_FACTORY
          = EncoderFactory.get();

	public static class GenericSerializer extends Serializer<GenericRecord>
	{
		public String getName() { return "avro-generic"; }

		private final GenericDatumWriter<GenericRecord> WRITER;
		private final GenericDatumReader<GenericRecord> READER;

		private BinaryEncoder encoder;
		private BinaryDecoder decoder;

		public GenericSerializer(Schema schema)
		{
			WRITER = new GenericDatumWriter<GenericRecord>(schema);
			READER = new GenericDatumReader<GenericRecord>(schema);
		}

		public GenericRecord deserialize(byte[] array) throws Exception
		{
                  decoder = DECODER_FACTORY.binaryDecoder(array, decoder);
                  return READER.read(null, decoder);
		}

		public byte[] serialize(GenericRecord data) throws IOException
		{
                  ByteArrayOutputStream out = outputStream();
                  encoder = ENCODER_FACTORY.binaryEncoder(out, encoder);
                  WRITER.write(data, encoder);
                  encoder.flush();
                  return out.toByteArray();
		}
	}

	// ------------------------------------------------------------
	// MediaTransformer


	public static final Transformer<MediaContent,GenericRecord> MediaTransformer = new MediaTransformer<GenericRecord>()
	{

		private final Schema sPlayer = serializers.avro.media.player.SCHEMA$;
		private final Schema sSize = serializers.avro.media.size.SCHEMA$;

		private  final Schema sMediaContent = serializers.avro.media.MediaContent.SCHEMA$;

		public GenericRecord[] resultArray(int size) {
                    return new GenericRecord[size];
                }
                
		// ----------------------------------------------------------
		// Forward

		public GenericRecord forward(MediaContent mc)
		{
			GenericRecord content = new GenericData.Record(Avro.Media.sMediaContent);

			content.put("media", forwardMedia(mc.media));

			GenericData.Array<GenericRecord> images = new GenericData.Array<>(mc.images.size(), Avro.Media.sImages);
			for (Image image : mc.images) {
				images.add(forwardImage(image));
			}
			content.put("images", images);

			return content;
		}
		
		private GenericRecord forwardMedia(Media media)
		{
			GenericRecord m = new GenericData.Record(Avro.Media.sMedia);
			m.put("uri", media.uri);
			m.put("format", media.format);
			if (media.title != null) {
				m.put("title", media.title);
			}
			m.put("duration", media.duration);
			if (media.hasBitrate) {
				m.put("bitrate", media.bitrate);
			}

			GenericData.Array<CharSequence> persons =  new GenericData.Array<>(media.persons.size(), Avro.Media.sPersons);
			for (String p : media.persons) {
                          persons.add(p);
			}

			m.put("persons", persons);
			m.put("player", forwardPlayer(media.player));
			m.put("height", media.height);
			m.put("width", media.width);
			m.put("size", media.size);
			if (media.copyright != null) {
				m.put("copyright", media.copyright);
			}

			return m;
		}

		public GenericEnumSymbol forwardPlayer(Media.Player p)
		{
			switch (p) {
				case JAVA: return new GenericData.EnumSymbol(sPlayer, "JAVA");
				case FLASH: return new GenericData.EnumSymbol(sSize, "FLASH");
				default:
					throw new AssertionError("invalid case: " + p);
			}
		}

		private GenericRecord forwardImage(Image image)
		{
			GenericRecord i = new GenericData.Record(Avro.Media.sImage);
			i.put("uri", image.uri);
			i.put("width", image.width);
			i.put("height", image.height);
			i.put("size", forwardSize(image.size));
			if (image.title != null) {
                          i.put("title",  image.title);
			}
			return i;
		}

		public GenericEnumSymbol forwardSize(Image.Size s)
		{
			switch (s) {
				case SMALL: return new GenericData.EnumSymbol(sSize, "SMALL");
				case LARGE: return new GenericData.EnumSymbol(sSize, "LARGE");
				default:
					throw new AssertionError("invalid case: " + s);
			}
		}

		// ----------------------------------------------------------
		// Reverse

		public MediaContent reverse(GenericRecord mc)
		{
			@SuppressWarnings("unchecked")
			GenericData.Array<GenericRecord> gimages = (GenericData.Array<GenericRecord>) mc.get("images");
			List<Image> images = new ArrayList<Image>((int) gimages.size());
			for (GenericRecord image : gimages) {
				images.add(reverseImage(image));
			}

			return new MediaContent(reverseMedia((GenericRecord) mc.get("media")), images);
		}

		private Media reverseMedia(GenericRecord media)
		{
			// Media

			@SuppressWarnings("unchecked")
			GenericData.Array<CharSequence> gpersons = (GenericData.Array<CharSequence>) media.get("persons");
			List<String> persons = new ArrayList<String>((int) gpersons.size());
			for (CharSequence person : gpersons) {
				persons.add(person.toString());
			}

			// Optional fields.
			CharSequence title = (CharSequence) media.get("title");
			Integer bitrate = (Integer) media.get("bitrate");
			CharSequence copyright = (CharSequence) media.get("copyright");

			return new Media(
				((CharSequence) media.get("uri")).toString(),
				title != null ? title.toString() : null,
				(Integer) media.get("width"),
				(Integer) media.get("height"),
				((CharSequence) media.get("format")).toString(),
				(Long) media.get("duration"),
				(Long) media.get("size"),
				bitrate != null ? bitrate : 0,
				bitrate != null,
				persons,
				reversePlayer((GenericEnumSymbol)media.get("player")),
				copyright != null ? copyright.toString() : null
			);
		}

		public Media.Player reversePlayer(GenericEnumSymbol p)
		{
			switch (p.toString()) {
				case "JAVA": return Media.Player.JAVA;
				case "FLASH": return Media.Player.FLASH;
				default: throw new AssertionError("invalid case: " + p);
			}
		}

		private Image reverseImage(GenericRecord image)
		{
			CharSequence title = (CharSequence) image.get("title");
			return new Image(
				((CharSequence) image.get("uri")).toString(),
				title == null ? null : title.toString(),
				(Integer) image.get("width"),
				(Integer) image.get("height"),
				reverseSize((GenericEnumSymbol)image.get("size")));
		}

		public Image.Size reverseSize(GenericEnumSymbol s)
		{
			switch (s.toString()) {
				case "SMALL": return Image.Size.SMALL;
				case "LARGE": return Image.Size.LARGE;
				default: throw new AssertionError("invalid case: " + s);
			}
		}

		public MediaContent shallowReverse(GenericRecord mc)
		{
			return new MediaContent(reverseMedia((GenericRecord) mc.get("media")), Collections.<Image>emptyList());
		}
	};
}
