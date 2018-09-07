package serializers.stephenerialization;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_BACKWARD_COMPATIBILITY;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO_WITH_ANNOTATIONS;

import data.media.MediaTransformer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;


public class Stephenerialization {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(BINARY)
        .mode(CODE_FIRST)
        .apiStyle(REFLECTION)
        .valueType(POJO_WITH_ANNOTATIONS)
        .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .name("stephenerialization")
        .projectUrl("https://bitbucket.org/enraged_ginger/stephenerialization/wiki/Home")
        .build();

    groups.media.add(mediaTransformer,
        new StephenerializationSerializer<>(properties));
  }

  // ------------------------------------------------------------
  // Serializer (just one)

  public static class StephenerializationSerializer<T> extends Serializer<T> {

    public StephenerializationSerializer(SerializerProperties properties) {
      super(properties);
    }

    public T deserialize(byte[] array) throws Exception {
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(array));
      @SuppressWarnings("unchecked")
      T v = (T) ois.readObject();
      return v;
    }

    public byte[] serialize(T data) throws IOException {
      ByteArrayOutputStream baos = outputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(data);
      return baos.toByteArray();
    }
  }

  // ------------------------------------------------------------
  // MediaTransformer

  public static final MediaTransformer<MediaContent> mediaTransformer = new MediaTransformer<MediaContent>() {
    public MediaContent forward(data.media.MediaContent a) {
      return new MediaContent(forwardMedia(a.media), forwardImages(a.images));
    }

    public data.media.MediaContent reverse(MediaContent a) {
      return new data.media.MediaContent(reverseMedia(a.media), reverseImages(a.images));
    }

    public data.media.MediaContent shallowReverse(MediaContent a) {
      return new data.media.MediaContent(reverseMedia(a.media), new ArrayList<data.media.Image>());
    }

    private List<Image> forwardImages(List<data.media.Image> images) {
      List<Image> convertedImages = new ArrayList<Image>();
      for (data.media.Image image : images) {
        convertedImages.add(forwardImage(image));
      }
      return convertedImages;
    }

    private Image forwardImage(data.media.Image a) {
      return new Image(a.uri, a.title, a.width, a.height, forwardSize(a.size));
    }

    private Image.Size forwardSize(data.media.Image.Size a) {
      return Image.Size.valueOf(a.name());
    }

    private Media forwardMedia(data.media.Media a) {
      return new Media(a.uri, a.title, a.width, a.height, a.format,
          a.duration, a.size, a.bitrate, a.hasBitrate, a.persons,
          forwardPlayer(a.player), a.copyright);
    }

    private Media.Player forwardPlayer(data.media.Media.Player a) {
      return Media.Player.find(a.name());
    }

    private List<data.media.Image> reverseImages(List<Image> images) {
      List<data.media.Image> convertedImages = new ArrayList<data.media.Image>();
      for (Image image : images) {
        convertedImages.add(reverseImage(image));
      }
      return convertedImages;
    }

    private data.media.Image reverseImage(Image a) {
      return new data.media.Image(a.uri, a.title, a.width, a.height, reverseSize(a.size));
    }

    private data.media.Image.Size reverseSize(Image.Size a) {
      return data.media.Image.Size.valueOf(a.name());
    }

    private data.media.Media reverseMedia(Media a) {
      return new data.media.Media(a.uri, a.title, a.width, a.height, a.format,
          a.duration, a.size, a.bitrate, a.hasBitrate, a.persons,
          reversePlayer(a.player), a.copyright);
    }

    private data.media.Media.Player reversePlayer(Media.Player a) {
      return data.media.Media.Player.find(a.name());
    }
  };
}