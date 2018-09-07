package serializers.dslplatform;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Format.JSON;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO_WITH_ANNOTATIONS;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.runtime.Settings;
import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import data.media.MediaTransformer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;
import serializers.dslplatform.data.media.DSLMediaContent;
import serializers.dslplatform.full.ImageFull;
import serializers.dslplatform.full.MediaContentFull;
import serializers.dslplatform.full.MediaFull;
import serializers.dslplatform.minified.ImageMinified;
import serializers.dslplatform.minified.MediaContentMinified;
import serializers.dslplatform.minified.MediaMinified;
import serializers.dslplatform.shared.Player;
import serializers.dslplatform.shared.Size;

/**
 * A test harness for DSL Platform (<a href="x`">http://dsl-platform.com</a>) generated classes.
 *
 * Uses pregenerated classes (the schema is in {@code schema/serializers.media.dsl}).
 */
public class DSLPlatform {

  public static void register(final MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties JSON_properties = builder
        .format(JSON)
        .mode(SCHEMA_FIRST)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .valueType(POJO)
        .name("dsl-platform")
        .projectUrl("https://github.com/ngs-doo/dsl-json")
        .build();

    groups.media
        .add(new DSLPlatformFullMediaTransformer(), new DSLPlatformFullSerializer(JSON_properties));

    SerializerProperties JSON_Minified_properties = JSON_properties.toBuilder()
        .feature(OPTIMIZED)
        .optimizedDescription("minified")
        .build();

    groups.media.add(new DSLPlatformMinifiedMediaTransformer(),
        new DSLPlatformMinifiedSerializer(JSON_Minified_properties));


  }

  public static void registerAnnotationBased(final MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties JSON_Java8Annotation_properties = builder
        .format(JSON)
        .mode(CODE_FIRST)
        .apiStyle(BUILD_TIME_CODE_GENERATION)
        .valueType(POJO_WITH_ANNOTATIONS)
        .name("dsl-platform")
        .projectUrl("https://github.com/ngs-doo/dsl-json")
        .build();

    groups.media.add(new MediaTransformer<DSLMediaContent>() {

      @Override
      public DSLMediaContent[] resultArray(int size) {
        return new DSLMediaContent[size];
      }

      @Override
      public DSLMediaContent forward(MediaContent content) {
        return copy(content);
      }

      @Override
      public MediaContent reverse(DSLMediaContent a) {
        return copy(a);
      }

      @Override
      public MediaContent shallowReverse(DSLMediaContent a) {
        return new DSLMediaContent(copy(a.media), Collections.<Image>emptyList());
      }

      private DSLMediaContent copy(MediaContent mc) {
        ArrayList<Image> images = new ArrayList<Image>(mc.images.size());
        for (Image i : mc.images) {
          images.add(new Image(i.uri, i.title, i.width, i.height, i.size));
        }
        return new DSLMediaContent(copy(mc.media), images);
      }


      private Media copy(Media m) {
        return new Media(m.uri, m.title, m.width, m.height, m.format, m.duration, m.size, m.bitrate,
            m.hasBitrate, new ArrayList<String>(m.persons), m.player, m.copyright);
      }
    }, new DSLPlatformSerializer(JSON_Java8Annotation_properties));

  }

  static class DSLPlatformSerializer extends Serializer<DSLMediaContent> {

    public DSLPlatformSerializer(SerializerProperties properties) {
      super(properties);
    }

    private static DslJson<Object> dslJson = new DslJson<>(
        Settings.withRuntime().allowArrayFormat(true).includeServiceLoader());
    private static JsonWriter writer = dslJson.newWriter();
    private final char[] tmp = new char[64];


    @Override
    public DSLMediaContent deserialize(final byte[] array) throws Exception {
      return dslJson.deserialize(DSLMediaContent.class, array, array.length);
    }

    @Override
    public byte[] serialize(final DSLMediaContent content) throws Exception {
      writer.reset();
      dslJson.serialize(writer, content);
      return writer.toByteArray();
    }
  }

  static class DSLPlatformFullSerializer extends Serializer<MediaContentFull> {

    public DSLPlatformFullSerializer(SerializerProperties properties) {
      super(properties);
    }

    private static DslJson<Object> dslJson = new DslJson<>();
    private static JsonWriter writer = dslJson.newWriter();
    private final char[] tmp = new char[64];


    @Override
    public MediaContentFull deserialize(final byte[] array) throws Exception {
      return (MediaContentFull) MediaContentFull.deserialize(dslJson.newReader(array));
    }

    @Override
    public byte[] serialize(final MediaContentFull content) throws Exception {
      writer.reset();
      content.serialize(writer, false);
      return writer.toByteArray();
    }
  }

  static class DSLPlatformMinifiedSerializer extends Serializer<MediaContentMinified> {

    public DSLPlatformMinifiedSerializer(SerializerProperties properties) {
      super(properties);
    }

    private static DslJson<Object> dslJson = new DslJson<>();
    private static JsonWriter writer = dslJson.newWriter();
    private final char[] tmp = new char[64];

    @Override
    public MediaContentMinified deserialize(final byte[] array) throws Exception {
      return (MediaContentMinified) MediaContentMinified.deserialize(dslJson.newReader(array));
    }

    @Override
    public byte[] serialize(final MediaContentMinified content) throws Exception {
      writer.reset();
      content.serialize(writer, true);
      return writer.toByteArray();
    }
  }


  static Player forward(final data.media.Media.Player player) {
    return player == data.media.Media.Player.JAVA
        ? Player.JAVA
        : Player.FLASH;
  }

  static Size forward(final data.media.Image.Size size) {
    return size == data.media.Image.Size.SMALL
        ? Size.SMALL
        : Size.LARGE;
  }

  static data.media.Media.Player reverse(final Player player) {
    return player == Player.JAVA
        ? data.media.Media.Player.JAVA
        : data.media.Media.Player.FLASH;
  }

  static data.media.Image.Size reverse(final Size size) {
    return size == Size.SMALL
        ? data.media.Image.Size.SMALL
        : data.media.Image.Size.LARGE;
  }


  static final class DSLPlatformFullMediaTransformer extends MediaTransformer<MediaContentFull> {

    @Override
    public MediaContentFull[] resultArray(final int size) {
      return new MediaContentFull[size];
    }

    @Override
    public MediaContentFull forward(final data.media.MediaContent commonMediaContent) {
      return new MediaContentFull(forward(commonMediaContent.media),
          forward(commonMediaContent.images));
    }

    private static MediaFull forward(final data.media.Media media) {
      return new MediaFull(
          media.uri,
          media.title,
          media.width,
          media.height,
          media.format,
          media.duration,
          media.size,
          media.bitrate,
          media.hasBitrate,
          media.persons,
          DSLPlatform.forward(media.player),
          media.copyright);
    }

    private static List<ImageFull> forward(final List<data.media.Image> images) {
      final ArrayList<ImageFull> forwardedImgs = new ArrayList<>(images.size());
      for (final data.media.Image image : images) {
        forwardedImgs.add(forward(image));
      }
      return forwardedImgs;
    }

    private static ImageFull forward(final data.media.Image image) {
      return new ImageFull(
          image.uri,
          image.title,
          image.width,
          image.height,
          DSLPlatform.forward(image.size));
    }

    @Override
    public data.media.MediaContent reverse(final MediaContentFull mc) {
      return new data.media.MediaContent(reverse(mc.getMedia()), reverse(mc.getImages()));
    }

    private static data.media.Media reverse(final MediaFull media) {
      return new data.media.Media(
          media.getUri(),
          media.getTitle(),
          media.getWidth(),
          media.getHeight(),
          media.getFormat(),
          media.getDuration(),
          media.getSize(),
          media.getBitrate(),
          media.getHasBitrate(),
          media.getPersons(),
          DSLPlatform.reverse(media.getPlayer()),
          media.getCopyright());
    }

    private static List<data.media.Image> reverse(final List<ImageFull> images) {
      final ArrayList<data.media.Image> reversed = new ArrayList<>(images.size());
      for (final ImageFull image : images) {
        reversed.add(reverse(image));
      }
      return reversed;
    }

    private static data.media.Image reverse(final ImageFull image) {
      return new data.media.Image(
          image.getUri(),
          image.getTitle(),
          image.getWidth(),
          image.getHeight(),
          DSLPlatform.reverse(image.getSize()));
    }

    @Override
    public data.media.MediaContent shallowReverse(final MediaContentFull mc) {
      return new data.media.MediaContent(reverse(mc.getMedia()), Collections.emptyList());
    }
  }

  static final class DSLPlatformMinifiedMediaTransformer extends
      MediaTransformer<MediaContentMinified> {

    @Override
    public MediaContentMinified[] resultArray(final int size) {
      return new MediaContentMinified[size];
    }

    @Override
    public MediaContentMinified forward(final data.media.MediaContent commonMediaContent) {
      return new MediaContentMinified(forward(commonMediaContent.media),
          forward(commonMediaContent.images));
    }

    private static MediaMinified forward(final data.media.Media media) {
      return new MediaMinified(
          media.uri,
          media.title,
          media.width,
          media.height,
          media.format,
          media.duration,
          media.size,
          media.bitrate,
          media.hasBitrate,
          media.persons,
          DSLPlatform.forward(media.player),
          media.copyright);
    }

    private static List<ImageMinified> forward(final List<data.media.Image> images) {
      final ArrayList<ImageMinified> forwardedImgs = new ArrayList<>(images.size());
      for (final data.media.Image image : images) {
        forwardedImgs.add(forward(image));
      }
      return forwardedImgs;
    }

    private static ImageMinified forward(final data.media.Image image) {
      return new ImageMinified(
          image.uri,
          image.title,
          image.width,
          image.height,
          DSLPlatform.forward(image.size));
    }

    @Override
    public data.media.MediaContent reverse(final MediaContentMinified mc) {
      return new data.media.MediaContent(reverse(mc.getMedia()), reverse(mc.getImages()));
    }

    private static data.media.Media reverse(final MediaMinified media) {
      return new data.media.Media(
          media.getUri(),
          media.getTitle(),
          media.getWidth(),
          media.getHeight(),
          media.getFormat(),
          media.getDuration(),
          media.getSize(),
          media.getBitrate(),
          media.getHasBitrate(),
          media.getPersons(),
          DSLPlatform.reverse(media.getPlayer()),
          media.getCopyright());
    }

    private static List<data.media.Image> reverse(final List<ImageMinified> images) {
      final ArrayList<data.media.Image> reversed = new ArrayList<data.media.Image>(images.size());
      for (final ImageMinified image : images) {
        reversed.add(reverse(image));
      }
      return reversed;
    }

    private static data.media.Image reverse(final ImageMinified image) {
      return new data.media.Image(
          image.getUri(),
          image.getTitle(),
          image.getWidth(),
          image.getHeight(),
          DSLPlatform.reverse(image.getSize()));
    }

    @Override
    public data.media.MediaContent shallowReverse(final MediaContentMinified mc) {
      return new data.media.MediaContent(reverse(mc.getMedia()), Collections.emptyList());
    }
  }
}
