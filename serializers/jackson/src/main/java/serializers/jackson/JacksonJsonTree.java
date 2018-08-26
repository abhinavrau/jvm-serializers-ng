package serializers.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

/**
 * Driver that uses Jackson for manual tree processing (to/from byte[]).
 */
public class JacksonJsonTree extends BaseJacksonDataBind<MediaContent>
{
  public static void register(MediaContentTestGroup groups)
  {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(SerializerProperties.Format.JSON)
            .apiStyle(REFLECTION)
            .mode(CODE_FIRST)
            .valueType(POJO)
            .name("jackson")
            .feature(OPTIMIZED)
            .projectURL("https://github.com/FasterXML/jackson-databind")
            .build();

    groups.media.add(JavaBuiltIn.mediaTransformer, new JacksonJsonTree(
                    properties,new ObjectMapper()));
  }

  public JacksonJsonTree(SerializerProperties properties, ObjectMapper mapper) {
      super(properties, MediaContent.class, mapper);
  }

  @Override
  public MediaContent deserialize(byte[] array) throws IOException
  {
      return readMediaContent(mapper.readTree(new ByteArrayInputStream(array)));
  }

  @Override
  public byte[] serialize(MediaContent mediaContent) throws IOException
  {
      JsonNode root = asTree(mediaContent, mapper.createObjectNode());
      return mapper.writeValueAsBytes(root);
  }

  @Override
  public void serializeItems(MediaContent[] items, OutputStream out) throws IOException
  {
      JsonGenerator generator = constructGenerator(out);
      // JSON allows simple sequences, so:
      for (MediaContent item : items) {
        mapper.writeValue(generator, asTree(item, mapper.createObjectNode()));
      }
      generator.close();
  }

  @Override
  public MediaContent[] deserializeItems(InputStream in, int numberOfItems) throws IOException 
  {
      JsonParser parser = constructParser(in);
      MediaContent[] result = new MediaContent[numberOfItems];
      for (int i = 0; i < numberOfItems; ++i) {
          result[i] = readMediaContent((JsonNode)mapper.readTree(parser));
      }
      parser.close();
      return result;
  }
  
    // // // Methods for deserializing using intermediate Tree representation
    
    protected static Image readImage(JsonNode node)
    {
      Image image = new Image();
      image.height = node.get("height").intValue();
      image.size = Image.Size.valueOf(node.get("size").textValue());
      image.title = node.get("title").textValue();
      image.uri = node.get("uri").textValue();
      image.width = node.get("width").intValue();
      return image;
    }

    protected static List<Image> readImages(ArrayNode imagesNode)
    {
      int size = imagesNode.size();
      List<Image> images = new ArrayList<Image>(size);
      for (JsonNode image : imagesNode) {
        images.add(readImage(image));
      }
      return images;
    }

    protected static MediaContent readMediaContent(JsonNode root) throws IOException
    {
      MediaContent mediaContent = new MediaContent();
      mediaContent.media = readMedia(root.get("media"));
      mediaContent.images = readImages((ArrayNode) root.get("images"));
      return mediaContent;
    }

    protected static Media readMedia(JsonNode node)
    {
      Media media = new Media();
      JsonNode bitrate = node.get("bitrate");
      if (bitrate != null && !bitrate.isNull()) {
        media.bitrate = bitrate.intValue();
        media.hasBitrate = true;
      }
      media.copyright = node.path("copyright").textValue();
      media.duration = node.path("duration").longValue();
      media.format = node.path("format").textValue();
      media.height = node.path("height").intValue();
      media.player = Media.Player.valueOf(node.get("player").textValue());
      ArrayNode personsArrayNode = (ArrayNode) node.get("persons");
      int size = personsArrayNode.size();
      List<String> persons = new ArrayList<String>(size);
      for (JsonNode person : personsArrayNode) {
        persons.add(person.textValue());
      }
      media.persons = persons;
      media.size = node.get("size").intValue();
      media.title = node.get("title").textValue();
      media.uri = node.get("uri").textValue();
      media.width = node.get("width").intValue();
      return media;
    }

    // // // Methods for serializing using intermediate Tree representation
    
    protected static JsonNode asTree(MediaContent mediaContent, ObjectNode node) throws IOException
    {
        addMedia(mediaContent.media, node.putObject("media"));
        addImages(mediaContent.images, node.putArray("images"));
        return node;
    }
    
    protected static ObjectNode addImage(Image image, ObjectNode node)
    {
      node.put("height", image.height);
      node.put("size", image.size.name());
      node.put("title", image.title);
      node.put("uri", image.uri);
      node.put("width", image.width);
      return node;
    }

    protected static ArrayNode addImages(List<Image> images, ArrayNode node)
    {
      for (Image image : images) {
        addImage(image, node.addObject());
      }
      return node;
    }

    protected static ObjectNode addMedia(Media media, ObjectNode node)
    {
      if (media.hasBitrate) {
        node.put("bitrate", media.bitrate);
      }
      node.put("copyright", media.copyright);
      node.put("duration", media.duration);
      node.put("format", media.format);
      node.put("height", media.height);
      ArrayNode persons = node.arrayNode();
      for (String person : media.persons) {
        persons.add(person);
      }
      node.set("persons", persons);
      node.put("player", media.player.name());
      node.put("size", media.size);
      node.put("title", media.title);
      node.put("uri", media.uri);
      node.put("width", media.width);
      return node;
    }
}
