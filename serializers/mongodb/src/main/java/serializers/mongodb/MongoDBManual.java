package serializers.mongodb;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import org.bson.*;
import org.bson.types.BasicBSONList;
import serializers.JavaBuiltIn;
import serializers.Serializer;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;

import java.util.ArrayList;
import java.util.List;

import static data.media.FieldMapping.*;
import static serializers.core.metadata.SerializerProperties.APIStyle.FIELD_BASED;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_ADDITIONAL_LANGUAGES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

public class MongoDBManual
{
    public static void register(MediaContentTestGroup groups)
    {
	    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
	    SerializerProperties properties = builder.format(BINARY)
			    .apiStyle(FIELD_BASED)
			    .mode(CODE_FIRST)
			    .valueType(POJO)
			    .name("mongodb")
			    .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
			    .projectURL("http://bsonspec.org/")
			    .build();

        groups.media.add(JavaBuiltIn.mediaTransformer, new MongoDBSerializer(properties));
    }
	
    public static final class MongoDBSerializer extends Serializer<MediaContent>
    {
    	MongoDBSerializer(SerializerProperties properties)
	    {
	    	super(properties);
	    }
		
        @Override
        public byte[] serialize(MediaContent data) throws Exception
        {
			BSONObject o = new BasicBSONObject();
			
			BSONObject media = serializeMedia(data.media);
			o.put(FULL_FIELD_NAME_MEDIA, media);
			
			BSONObject[] images = new BSONObject[data.images.size()];
			int j = 0;
			for (Image i : data.images)
			{
				images[j++] = serializeImage(i);
			}
			
			o.put(FULL_FIELD_NAME_IMAGES, images);
			
			BSONEncoder enc = new BasicBSONEncoder();
			return enc.encode(o);
		}
		
		protected BSONObject serializeMedia(Media media)
		{
			BSONObject r = new BasicBSONObject();
			
			r.put(FULL_FIELD_NAME_PLAYER, media.player.name());
			r.put(FULL_FIELD_NAME_URI, media.uri);
			if (media.title != null)
				r.put(FULL_FIELD_NAME_TITLE, media.title);
			r.put(FULL_FIELD_NAME_WIDTH, media.width);
			r.put(FULL_FIELD_NAME_HEIGHT, media.height);
			r.put(FULL_FIELD_NAME_FORMAT, media.format);
			r.put(FULL_FIELD_NAME_DURATION, media.duration);
			r.put(FULL_FIELD_NAME_SIZE, media.size);
			if (media.hasBitrate)
				r.put(FULL_FIELD_NAME_BITRATE, media.bitrate);
			if (media.copyright != null)
				r.put(FULL_FIELD_NAME_COPYRIGHT, media.copyright);
			r.put(FULL_FIELD_NAME_PERSONS, media.persons);
			
			return r;
		}
		
		protected BSONObject serializeImage(Image image)
		{
			BSONObject r = new BasicBSONObject();
			
			r.put(FULL_FIELD_NAME_URI, image.uri);
			if (image.title != null)
				r.put(FULL_FIELD_NAME_TITLE, image.title);
			r.put(FULL_FIELD_NAME_WIDTH, image.width);
			r.put(FULL_FIELD_NAME_HEIGHT, image.height);
			r.put(FULL_FIELD_NAME_SIZE, image.size.name());
			
			return r;
		}
		
		public MediaContent deserialize(byte[] array) throws Exception
		{
			MediaContent r = new MediaContent();
			
			BSONDecoder dec = new BasicBSONDecoder();
			BSONObject o = dec.readObject(array);
			for (String name : o.keySet())
			{
				Integer i = fullFieldToIndex.get(name);
				if (i != null)
				{
					switch (i)
					{
					case FIELD_IX_MEDIA:
						r.media = deserializeMedia((BSONObject)o.get(name));
						continue;
						
					case FIELD_IX_IMAGES:
						r.images = deserializeImages((BasicBSONList)o.get(name));
						continue;
					}
				}
				throw new IllegalStateException("Unexpected field '" + name + "'");
			}
			
			return r;
		}
		
		protected Media deserializeMedia(BSONObject media) {
			Media r = new Media();
			
			for (String name : media.keySet())
			{
				Integer i = fullFieldToIndex.get(name);
				if (i != null)
				{
					switch (i)
					{
					case FIELD_IX_PLAYER:
						r.player = Media.Player.valueOf((String)media.get(name));
						continue;
						
					case FIELD_IX_URI:
						r.uri = (String)media.get(name);
						continue;
						
					case FIELD_IX_TITLE:
						r.title = (String)media.get(name);
						continue;
						
					case FIELD_IX_WIDTH:
						r.width = (Integer)media.get(name);
						continue;
						
					case FIELD_IX_HEIGHT:
						r.height = (Integer)media.get(name);
						continue;
						
					case FIELD_IX_FORMAT:
						r.format = (String)media.get(name);
						continue;
						
					case FIELD_IX_DURATION:
						r.duration = (Long)media.get(name);
						continue;
						
					case FIELD_IX_SIZE:
						r.size = (Long)media.get(name);
						continue;
						
					case FIELD_IX_BITRATE:
						r.bitrate = (Integer)media.get(name);
						r.hasBitrate = true;
						continue;
						
					case FIELD_IX_PERSONS:
						BasicBSONList pl = (BasicBSONList)media.get(name);
						List<String> persons = new ArrayList<>();
						for (Object o : pl) {
							persons.add((String)o);
						}
						r.persons = persons;
						continue;
						
					case FIELD_IX_COPYRIGHT:
						r.copyright = (String)media.get(name);
						continue;
					}
				}
				throw new IllegalStateException("Unexpected field '" + name + "'");
			}
			
			return r;
		}
		
		protected List<Image> deserializeImages(BasicBSONList images) {
			List<Image> r = new ArrayList<>();
			for (Object o : images) {
				r.add(deserializeImage((BSONObject)o));
			}
			return r;
		}
		
		protected Image deserializeImage(BSONObject image) {
			Image r = new Image();
			
			for (String name : image.keySet())
			{
				Integer i = fullFieldToIndex.get(name);
				if (i != null)
				{
					switch (i)
					{
					case FIELD_IX_URI:
						r.uri = (String)image.get(name);
						continue;
					case FIELD_IX_TITLE:
						r.title = (String)image.get(name);
						continue;
					case FIELD_IX_WIDTH:
						r.width = (Integer)image.get(name);
						continue;
					case FIELD_IX_HEIGHT:
						r.height = (Integer)image.get(name);
						continue;
					case FIELD_IX_SIZE:
						r.size = Image.Size.valueOf((String)image.get(name));
						continue;
					}
				}
				throw new IllegalStateException("Unexpected field '" + name + "'");
			}
			
			return r;
		}
	}
}
