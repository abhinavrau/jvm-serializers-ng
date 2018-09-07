package serializers.json;


import static argo.jdom.JsonNodeFactories.array;
import static argo.jdom.JsonNodeFactories.field;
import static argo.jdom.JsonNodeFactories.nullNode;
import static argo.jdom.JsonNodeFactories.number;
import static argo.jdom.JsonNodeFactories.object;
import static argo.jdom.JsonNodeFactories.string;
import static argo.jdom.JsonNodeType.NULL;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.NONE;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;
import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

/**
 * Driver that uses Argo [http://argo.sourceforge.net], with manual tree processing.
 *
 * Implementation provided by Mark Slater, admin and committer on the Argo project.
 */
public class JsonArgoTree {

  public static void register(MediaContentTestGroup groups) {
    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder
        .format(SerializerProperties.Format.JSON)
        .apiStyle(FIELD_BASED)
        .mode(CODE_FIRST)
        .valueType(NONE)
        .name("argo")
        .projectUrl("http://argo.sourceforge.net")
        .build();

    groups.media.add(JavaBuiltIn.mediaTransformer,
        new ManualTreeSerializer(properties));
  }

  static class ManualTreeSerializer extends Serializer<MediaContent> {


    public ManualTreeSerializer(SerializerProperties properties) {
      super(properties);
    }

    public MediaContent deserialize(byte[] array) throws Exception {
      String mediaContentJsonInput = new String(array, "UTF-8");
      return readMediaContent(mediaContentJsonInput);
    }

    public byte[] serialize(MediaContent mediaContent) throws IOException {
      return writeMediaContent(mediaContent).getBytes("UTF-8");
    }

    private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();
    private static final JdomParser JDOM_PARSER = new JdomParser();

    private static final JsonStringNode MEDIA_KEY = string("media");
    private static final JsonStringNode IMAGES_KEY = string("images");
    private static final JsonStringNode URI_KEY = string("uri");
    private static final JsonStringNode WIDTH_KEY = string("width");
    private static final JsonStringNode HEIGHT_KEY = string("height");
    private static final JsonStringNode SIZE_KEY = string("size");
    private static final JsonStringNode COPYRIGHT_KEY = string("copyright");
    private static final JsonStringNode DURATION_KEY = string("duration");
    private static final JsonStringNode TITLE_KEY = string("title");
    private static final JsonStringNode PLAYER_KEY = string("player");
    private static final JsonStringNode FORMAT_KEY = string("format");
    private static final JsonStringNode PERSONS_KEY = string("persons");
    private static final JsonStringNode BIT_RATE_KEY = string("bitrate");

    private String writeMediaContent(MediaContent mediaContent) {
      return JSON_FORMATTER.format(
          object(
              field(MEDIA_KEY, createMediaObject(mediaContent.media)),
              field(IMAGES_KEY, createImagesArray(mediaContent.images))
          ));
    }

    private JsonNode createImagesArray(List<Image> images) {
      List<JsonNode> jsonNodes = new ArrayList<JsonNode>(images.size() * 2);
      for (Image image : images) {
        jsonNodes.add(object(
            new JsonField(HEIGHT_KEY, number(String.valueOf(image.height))),
            new JsonField(SIZE_KEY, string(image.size.name())),
            new JsonField(TITLE_KEY, string(image.title)),
            new JsonField(URI_KEY, string(image.uri)),
            new JsonField(WIDTH_KEY, number(String.valueOf(image.width)))
        ));
      }
      return array(jsonNodes);
    }

    private JsonNode createMediaObject(Media media) {
      List<JsonNode> persons = new ArrayList<JsonNode>(media.persons.size() * 2);
      for (String person : media.persons) {
        persons.add(string(person));
      }
      JsonNode value = media.copyright == null ? nullNode() : string(media.copyright);
      List<JsonField> jsonFields = new ArrayList<JsonField>(asList(
          new JsonField(URI_KEY, string(media.uri)),
          new JsonField(TITLE_KEY, string(media.title)),
          new JsonField(WIDTH_KEY, number(String.valueOf(media.width))),
          new JsonField(HEIGHT_KEY, number(String.valueOf(media.height))),
          new JsonField(FORMAT_KEY, string(media.format)),
          new JsonField(DURATION_KEY, number(String.valueOf(media.duration))),
          new JsonField(SIZE_KEY, number(String.valueOf(media.size))),
          new JsonField(PLAYER_KEY, string(media.player.name())),
          new JsonField(COPYRIGHT_KEY, value),
          new JsonField(PERSONS_KEY, array(persons))
      ));
      if (media.hasBitrate) {
        jsonFields.add(new JsonField(BIT_RATE_KEY, number(String.valueOf(media.bitrate))));
      }
      return object(jsonFields);
    }

    MediaContent readMediaContent(String mediaContentJsonInput) throws Exception {
      MediaContent mediaContent = new MediaContent();
      Map<JsonStringNode, JsonNode> fields = JDOM_PARSER.parse(mediaContentJsonInput).getFields();
      mediaContent.media = readMedia(fields.get(MEDIA_KEY));
      mediaContent.images = readImages(fields.get(IMAGES_KEY));
      return mediaContent;
    }

    private Media readMedia(JsonNode jsonNode) {
      Media media = new Media();
      Map<JsonStringNode, JsonNode> fields = jsonNode.getFields();
      media.copyright = getValueOrNull(fields.get(COPYRIGHT_KEY));
      media.duration = parseLong(fields.get(DURATION_KEY).getText());
      media.format = getValueOrNull(fields.get(FORMAT_KEY));
      media.player = Media.Player.valueOf(fields.get(PLAYER_KEY).getText());
      media.title = getValueOrNull(fields.get(TITLE_KEY));
      media.uri = getValueOrNull(fields.get(URI_KEY));
      media.size = parseLong(fields.get(SIZE_KEY).getText());
      media.height = parseInt(fields.get(HEIGHT_KEY).getText());
      media.width = parseInt(fields.get(WIDTH_KEY).getText());

      List<JsonNode> personNodesElements = fields.get(PERSONS_KEY).getElements();
      List<String> persons = new ArrayList<String>(personNodesElements.size() * 2);
      for (JsonNode personNode : personNodesElements) {
        persons.add(personNode.getText());
      }
      media.persons = persons;
      JsonNode bitRate = fields.get(BIT_RATE_KEY);
      if (bitRate != null && bitRate.getType() != NULL) {
        media.bitrate = parseInt(bitRate.getText());
        media.hasBitrate = true;
      }
      return media;
    }

    private static String getValueOrNull(JsonNode textNode) {
      return textNode.getType() == NULL ? null : textNode.getText();
    }

    private List<Image> readImages(JsonNode node) {
      List<JsonNode> elements = node.getElements();
      List<Image> images = new ArrayList<Image>(elements.size() * 2);
      for (JsonNode jsonNode : elements) {
        Map<JsonStringNode, JsonNode> fields = jsonNode.getFields();
        images.add(new Image(
            fields.get(URI_KEY).getText(),
            fields.get(TITLE_KEY).getText(),
            parseInt(fields.get(WIDTH_KEY).getText()),
            parseInt(fields.get(HEIGHT_KEY).getText()),
            Image.Size.valueOf(fields.get(SIZE_KEY).getText())
        ));
      }
      return images;
    }
  }
}
