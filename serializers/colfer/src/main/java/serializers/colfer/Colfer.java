package serializers.colfer;

import data.media.MediaTransformer;
import serializers.*;
import serializers.colfer.media.Player;
import serializers.colfer.media.Size;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Colfer {

	public static void register(TestGroups groups) {
		groups.media.add(new Transformer(), new ColferSerializer(),
			new SerFeatures(
				SerFormat.BIN_CROSSLANG,
				SerGraph.FLAT_TREE,
				SerClass.CLASSES_KNOWN,
				"generated code"
			)
		);
	}

	static final class ColferSerializer extends Serializer<serializers.colfer.media.MediaContent> {

		private byte[] buffer = new byte[1024];


		@Override
		public serializers.colfer.media.MediaContent deserialize(byte[] array) throws Exception {
			serializers.colfer.media.MediaContent mc = new serializers.colfer.media.MediaContent();
			mc.unmarshal(array, 0);
			return mc;
		}

		@Override
		public byte[] serialize(serializers.colfer.media.MediaContent content) {
			while (true) {
				try {
					int n = content.marshal(buffer, 0);
					return Arrays.copyOf(buffer, n);
				} catch (BufferOverflowException e) {
					buffer = new byte[buffer.length * 2];
				}
			}
		}

		public String getName() {
			return "colfer";
		}

	}

	static final class Transformer extends MediaTransformer<serializers.colfer.media.MediaContent> {

		@Override
		public serializers.colfer.media.MediaContent[] resultArray(int size) {
			return new serializers.colfer.media.MediaContent[size];
		}

		@Override
		public data.media.MediaContent shallowReverse(serializers.colfer.media.MediaContent mc) {
			return new data.media.MediaContent(reverseMedia(mc.getMedia()), Collections.<data.media.Image>emptyList());
		}

		@Override
		public serializers.colfer.media.MediaContent forward(data.media.MediaContent src) {
			int i = src.images.size();
			serializers.colfer.media.Image[] images = new serializers.colfer.media.Image[i];
			while (--i >= 0) {
				images[i] = forwardImage(src.images.get(i));
			}

			serializers.colfer.media.MediaContent dst = new serializers.colfer.media.MediaContent();
			dst.images = images;
			dst.media = forwardMedia(src.media);
			return dst;
		}

		@Override
		public data.media.MediaContent reverse(serializers.colfer.media.MediaContent src) {
			List<data.media.Image> images = new ArrayList<>(src.images.length);
			for (serializers.colfer.media.Image image : src.images) {
				images.add(reverseImage(image));
			}

			return new data.media.MediaContent(reverseMedia(src.media), images);
		}

		private static serializers.colfer.media.Media forwardMedia(data.media.Media src) {
			serializers.colfer.media.Media dst = new serializers.colfer.media.Media();
			dst.uri = (src.uri != null ? src.uri:"");
			dst.title = (src.title !=null ? src.title:"");
			dst.width = src.width;
			dst.height = src.height;
			dst.format = src.format;
			dst.duration = src.duration;
			dst.size = src.size;
			dst.bitrate = src.bitrate;
			dst.hasBitrate = src.hasBitrate;
			dst.persons = src.persons.toArray(dst.persons);
			dst.player = new Player();
			switch (src.player) {
				case FLASH:
					dst.player.flash = true;
					break;
				case JAVA:
					dst.player.java = true;
					break;
			}
			if (src.copyright != null)
				dst.copyright = src.copyright;
			return dst;
		}

		private static data.media.Media reverseMedia(serializers.colfer.media.Media src) {
			data.media.Media dst = new data.media.Media();
			dst.uri = src.uri;
			dst.title = src.title;
			dst.width = src.width;
			dst.height = src.height;
			dst.format = src.format;
			dst.duration = src.duration;
			dst.size = src.size;
			dst.bitrate = src.bitrate;
			dst.hasBitrate = src.hasBitrate;
			dst.persons = Arrays.asList(src.persons);
			if (src.player.flash) {
				dst.player = data.media.Media.Player.FLASH;
			}
			if (src.player.java) {
				dst.player = data.media.Media.Player.JAVA;
			}
			if (src.copyright.length() != 0) {
				dst.copyright = src.copyright;
			}
			return dst;
		}

		private static serializers.colfer.media.Image forwardImage(data.media.Image src) {
			serializers.colfer.media.Image dst = new serializers.colfer.media.Image();
			dst.uri = (src.uri != null ? src.uri:"");
			dst.title = (src.title !=null ? src.title:"");
			dst.width = src.width;
			dst.height = src.height;
			dst.size = new Size();
			switch (src.size) {
				case SMALL:
					dst.size.small = true;
					break;
				case LARGE:
					dst.size.large = true;
					break;
			}
			return dst;
		}

		private static data.media.Image reverseImage(serializers.colfer.media.Image src) {
			data.media.Image dst = new data.media.Image();
			dst.uri = src.uri;
			dst.title = src.title;
			dst.width = src.width;
			dst.height = src.height;
			if (src.size.small) {
				dst.size = data.media.Image.Size.SMALL;
			}
			if (src.size.large) {
				dst.size = data.media.Image.Size.LARGE;
			}
			return dst;
		}

	}
}
