package serializers.flatbuffers;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_BACKWARD_COMPATIBILITY;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.CUSTOM_TYPE;

import com.google.flatbuffers.FlatBufferBuilder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;
import serializers.flatbuffers.media.Image;
import serializers.flatbuffers.media.Media;
import serializers.flatbuffers.media.MediaContent;
import serializers.flatbuffers.media.Player;
import serializers.flatbuffers.media.Size;

public class FlatBuffers {

  public static void register(MediaContentTestGroup groups) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .mode(SCHEMA_FIRST)
        .valueType(CUSTOM_TYPE)
        .name("flatbuffers")
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
        .feature(JSON_CONVERTER)
        .projectUrl("https://google.github.io/flatbuffers/")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer, new FBSerializer(properties));
  }

  static final class FBSerializer extends Serializer<data.media.MediaContent> {

    ByteBuffer bb = ByteBuffer.allocate(1024);

    public FBSerializer(SerializerProperties properties) {
      super(properties);
    }


    @Override
    public data.media.MediaContent deserialize(byte[] array) throws Exception {
      ByteBuffer bb = ByteBuffer.wrap(array);
      MediaContent mc = MediaContent.getRootAsMediaContent(bb);
      return reverse(mc);
    }

    @Override
    public byte[] serialize(data.media.MediaContent content) {
      FlatBufferBuilder fbb = new FlatBufferBuilder(bb);
      forward(content, fbb);
      return fbb.sizedByteArray();
    }

    public void forward(data.media.MediaContent mc, FlatBufferBuilder fbb) {
      int media = forwardMedia(mc.media, fbb);
      int images[] = new int[mc.images.size()];
      for (int i = 0; i < mc.images.size(); i++) {
        images[i] = forwardImage(mc.images.get(i), fbb);
      }
      int image = MediaContent.createImagesVector(fbb, images);
      int offset = MediaContent.createMediaContent(fbb, image, media);
      MediaContent.finishMediaContentBuffer(fbb, offset);
    }

    private int forwardMedia(data.media.Media media, FlatBufferBuilder fbb) {
      int uri = fbb.createString(media.uri);
      int title = media.title != null ? fbb.createString(media.title) : 0;
      int format = fbb.createString(media.format);
      int[] persons = new int[media.persons.size()];
      for (int i = 0; i < media.persons.size(); i++) {
        persons[i] = fbb.createString(media.persons.get(i));
      }
      int person = Media.createPersonsVector(fbb, persons);
      byte player = forwardPlayer(media.player);
      int copyright = media.copyright != null ? fbb.createString(media.copyright) : 0;
      return Media.createMedia(
          fbb,
          uri,
          title,
          media.width,
          media.height,
          format,
          media.duration,
          media.size,
          media.bitrate,
          media.hasBitrate,
          person,
          player,
          copyright);
    }

    public byte forwardPlayer(data.media.Media.Player p) {
      switch (p) {
        case JAVA:
          return Player.JAVA;
        case FLASH:
          return Player.FLASH;
        default:
          throw new AssertionError("invalid case: " + p);
      }
    }

    private int forwardImage(data.media.Image image, FlatBufferBuilder fbb) {
      int uri = fbb.createString(image.uri);
      int title = image.title != null ? fbb.createString(image.title) : 0;
      byte size = forwardSize(image.size);
      return Image.createImage(fbb, uri, title, image.width, image.height, size);
    }

    public byte forwardSize(data.media.Image.Size s) {
      switch (s) {
        case SMALL:
          return Size.SMALL;
        case LARGE:
          return Size.LARGE;
        default:
          throw new AssertionError("invalid case: " + s);
      }
    }

    public data.media.MediaContent reverse(MediaContent mc) {
      List<data.media.Image> images = new ArrayList<data.media.Image>(mc.imagesLength());
      for (int i = 0; i < mc.imagesLength(); i++) {
        images.add(reverseImage(mc.images(i)));
      }
      return new data.media.MediaContent(reverseMedia(mc.media()), images);
    }

    private data.media.Media reverseMedia(Media media) {
      return new data.media.Media(
          media.uri(),
          media.title(),
          media.width(),
          media.height(),
          media.format(),
          media.duration(),
          media.size(),
          media.bitrate(),
          media.hasBitrate(),
          reversePersons(media),
          reversePlayer(media.player()),
          media.copyright()
      );
    }

    public data.media.Media.Player reversePlayer(byte p) {
      switch (p) {
        case Player.JAVA:
          return data.media.Media.Player.JAVA;
        case Player.FLASH:
          return data.media.Media.Player.FLASH;
        default:
          throw new AssertionError("invalid case: " + p);
      }
    }

    private List<String> reversePersons(Media media) {
      List<String> result = new ArrayList<>(media.personsLength());
      for (int i = 0; i < media.personsLength(); i++) {
        result.add(media.persons(i));
      }
      return result;
    }

    private data.media.Image reverseImage(Image image) {
      return new data.media.Image(
          image.uri(),
          image.title(),
          image.width(),
          image.height(),
          reverseSize(image.size()));
    }

    public data.media.Image.Size reverseSize(byte s) {
      switch (s) {
        case Size.SMALL:
          return data.media.Image.Size.SMALL;
        case Size.LARGE:
          return data.media.Image.Size.LARGE;
        default:
          throw new AssertionError("invalid case: " + s);
      }
    }

    public data.media.MediaContent shallowReverse(MediaContent mc) {
      return new data.media.MediaContent(reverseMedia(mc.media()),
          Collections.<data.media.Image>emptyList());
    }
  }
}
