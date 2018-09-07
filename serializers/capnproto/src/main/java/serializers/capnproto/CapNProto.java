package serializers.capnproto;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_BACKWARD_COMPATIBILITY;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.BUILDER_PATTERN;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.capnproto.ArrayOutputStream;
import org.capnproto.MessageBuilder;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;
import org.capnproto.StructList;
import org.capnproto.Text;
import org.capnproto.TextList;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.capnproto.media.Mediacontent;
import serializers.core.metadata.SerializerProperties;

public class CapNProto {

  public static void register(MediaContentTestGroup groups) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .mode(SCHEMA_FIRST)
        .valueType(BUILDER_PATTERN)
        .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
        .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
        .name("capnproto")
        .projectUrl("https://capnproto.org/")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer, new CPSerializer(properties));

  }

  static final class CPSerializer extends Serializer<data.media.MediaContent> {

    //MessageBuilder message = new MessageBuilder();//TODO: no reset!?
    ArrayOutputStream os;

    public CPSerializer(SerializerProperties properties) {
      super(properties);
      os = new ArrayOutputStream(ByteBuffer.allocate(1024 * 3));
      os.buf.order(ByteOrder.LITTLE_ENDIAN);
    }


    @Override
    public data.media.MediaContent deserialize(byte[] array) throws Exception {
      MessageReader message = Serialize.read(ByteBuffer.wrap(array));
      Mediacontent.MediaContent.Reader mc = message.getRoot(Mediacontent.MediaContent.factory);
      return reverse(mc);
    }

    @Override
    public byte[] serialize(data.media.MediaContent content) throws Exception {
      MessageBuilder message = new MessageBuilder();
      Mediacontent.MediaContent.Builder builder = message
          .initRoot(Mediacontent.MediaContent.factory);
      forward(content, builder);
      os.buf.rewind();
      Serialize.write(os, message);
      return Arrays.copyOf(os.buf.array(), os.buf.position());
    }

    public void forward(data.media.MediaContent mc, Mediacontent.MediaContent.Builder builder) {
      forwardMedia(mc.media, builder.initMedia());
      StructList.Builder<Mediacontent.Image.Builder> images = builder.initImages(mc.images.size());
      for (int i = 0; i < mc.images.size(); i++) {
        forwardImage(mc.images.get(i), images.get(i));
      }
    }

    private void forwardMedia(data.media.Media media, Mediacontent.Media.Builder builder) {
      builder.setUri(media.uri);
      if (media.title != null) {
        builder.setTitle(media.title);
      }
      builder.setWidth(media.width);
      builder.setHeight(media.height);
      builder.setFormat(media.format);
      builder.setDuration(media.duration);
      builder.setSize(media.size);
      builder.setBitrate(media.bitrate);
      builder.setHasBitrate(media.hasBitrate);
      TextList.Builder persons = builder.initPersons(media.persons.size());
      for (int i = 0; i < media.persons.size(); i++) {
        persons.set(i, new Text.Reader(media.persons.get(i)));
      }
      builder.setPlayer(forwardPlayer(media.player));
      if (media.copyright != null) {
        builder.setCopyright(media.copyright);
      }
    }

    public Mediacontent.Media.Player forwardPlayer(data.media.Media.Player p) {
      switch (p) {
        case JAVA:
          return Mediacontent.Media.Player.JAVA;
        case FLASH:
          return Mediacontent.Media.Player.FLASH;
        default:
          throw new AssertionError("invalid case: " + p);
      }
    }

    private void forwardImage(data.media.Image image, Mediacontent.Image.Builder builder) {
      builder.setUri(image.uri);
      if (image.title != null) {
        builder.setTitle(image.title);
      }
      builder.setWidth(image.width);
      builder.setHeight(image.height);
      builder.setSize(forwardSize(image.size));
    }

    public Mediacontent.Image.Size forwardSize(data.media.Image.Size s) {
      switch (s) {
        case SMALL:
          return Mediacontent.Image.Size.SMALL;
        case LARGE:
          return Mediacontent.Image.Size.LARGE;
        default:
          throw new AssertionError("invalid case: " + s);
      }
    }

    public data.media.MediaContent reverse(Mediacontent.MediaContent.Reader reader) {
      data.media.Media media = reverseMedia(reader.getMedia());
      StructList.Reader<Mediacontent.Image.Reader> images = reader.getImages();
      List<data.media.Image> list = new ArrayList<>(images.size());
      for (int i = 0; i < images.size(); i++) {
        list.add(reverseImage(images.get(i)));
      }
      return new data.media.MediaContent(media, list);
    }

    private data.media.Media reverseMedia(Mediacontent.Media.Reader media) {
      return new data.media.Media(
          media.getUri().toString(),
          media.hasTitle() ? media.getTitle().toString() : null,
          media.getWidth(),
          media.getHeight(),
          media.getFormat().toString(),
          media.getDuration(),
          media.getSize(),
          media.getBitrate(),
          media.getHasBitrate(),
          reversePersons(media.getPersons()),
          reversePlayer(media.getPlayer()),
          media.hasCopyright() ? media.getCopyright().toString() : null
      );
    }

    public data.media.Media.Player reversePlayer(Mediacontent.Media.Player p) {
      switch (p) {
        case JAVA:
          return data.media.Media.Player.JAVA;
        case FLASH:
          return data.media.Media.Player.FLASH;
        default:
          throw new AssertionError("invalid case: " + p);
      }
    }

    private List<String> reversePersons(TextList.Reader reader) {
      List<String> result = new ArrayList<>(reader.size());
      for (int i = 0; i < reader.size(); i++) {
        result.add(reader.get(i).toString());
      }
      return result;
    }

    private data.media.Image reverseImage(Mediacontent.Image.Reader reader) {
      return new data.media.Image(
          reader.getUri().toString(),
          reader.hasTitle() ? reader.getTitle().toString() : null,
          reader.getWidth(),
          reader.getHeight(),
          reverseSize(reader.getSize()));
    }

    public data.media.Image.Size reverseSize(Mediacontent.Image.Size s) {
      switch (s) {
        case SMALL:
          return data.media.Image.Size.SMALL;
        case LARGE:
          return data.media.Image.Size.LARGE;
        default:
          throw new AssertionError("invalid case: " + s);
      }
    }

    public data.media.MediaContent shallowReverse(Mediacontent.MediaContent.Reader mc) {
      return new data.media.MediaContent(reverseMedia(mc.getMedia()),
          Collections.<data.media.Image>emptyList());
    }
  }
}
