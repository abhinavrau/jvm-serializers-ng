package serializers.protostuff;

import data.media.MediaTransformer;
import io.protostuff.*;
import io.protostuff.runtime.RuntimeSchema;
import serializers.JavaBuiltIn;
import serializers.Serializer;
import serializers.MediaContentTestGroup;
import serializers.core.metadata.SerializerProperties;
import serializers.protostuff.media.Image;
import serializers.protostuff.media.Media;
import serializers.protostuff.media.MediaContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static serializers.core.metadata.SerializerProperties.APIStyle.BUILD_TIME_CODE_GENERATION;
import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.*;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.Mode.SCHEMA_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

public final class Protostuff
{
    
    public static void register(MediaContentTestGroup groups)
    {
        SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
        SerializerProperties properties = builder
                .format(BINARY)
                .mode(SCHEMA_FIRST)
                .apiStyle(BUILD_TIME_CODE_GENERATION)
                .valueType(POJO)
                .feature(JSON_CONVERTER)
                .feature(XML_CONVERTER)
                .feature(SUPPORTS_BACKWARD_COMPATIBILITY)
                .name("protostuff")
                .projectURL("https://protostuff.github.io/docs/")
                .build();
        // generated code
        groups.media.add(mediaTransformer, new ProtostuffMediaSerializer(properties));

        SerializerProperties handCoded_properties = properties.toBuilder()
                .feature(OPTIMIZED)
                .optimizedDescription("hand-coded schema + no autoboxing")
                .build();
        // manual (hand-coded schema, no autoboxing)
        groups.media.add(JavaBuiltIn.mediaTransformer,
                new ProtostuffManualMediaSerializer(handCoded_properties));

        SerializerProperties reflection_properties = properties.toBuilder()
                .mode(CODE_FIRST)
                .apiStyle(REFLECTION)
                .build();
        // runtime (reflection)
        groups.media.add(JavaBuiltIn.mediaTransformer,
                new ProtostuffRuntimeMediaSerializer(reflection_properties));


        SerializerProperties protobuf_properties = properties.toBuilder()
                .feature(SUPPORTS_ADDITIONAL_LANGUAGES)
                .name("protobuf")
                .relatedSerializer("protostuff")
                .build();
        // protobuf serialization + generated code
        groups.media.add(mediaTransformer, new ProtobufMediaSerializer(protobuf_properties));

        SerializerProperties protobuf_reflection_properties = protobuf_properties.toBuilder()
                .apiStyle(REFLECTION)
                .build();
        // protobuf serialization + runtime
        groups.media.add(JavaBuiltIn.mediaTransformer,
                new ProtobufRuntimeMediaSerializer(protobuf_reflection_properties));

        SerializerProperties graph_properties = properties.toBuilder()
                .feature(SUPPORTS_CYCLIC_REFERENCES)
                .build();
        // graph
        groups.media.add(mediaTransformer, new ProtostuffGraphMediaSerializer(graph_properties));


        SerializerProperties graph_reflection_properties = graph_properties.toBuilder()
                .apiStyle(REFLECTION)
                .build();
        // graph + reflection
        groups.media.add(JavaBuiltIn.mediaTransformer,
                new ProtostuffGraphRuntimeMediaSerializer(graph_reflection_properties));
        
        // exclude protostuff-graph-manual
        /*groups.media.add(JavaBuiltIn.mediaTransformer, ProtostuffGraphManualMediaSerializer,
                new SerFeatures(
                        SerFormat.BINARY,
                        SerGraph.FULL_GRAPH,
                        SerClass.MANUAL_OPT,
                        "graph + manual"
                )
        );*/
    }
    
    public static class ProtostuffMediaSerializer extends Serializer<MediaContent>
    {
        public ProtostuffMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public MediaContent deserialize(byte[] array) throws Exception
        {
            final MediaContent mc = MediaContent.newBuilder().build();
            ProtostuffIOUtil.mergeFrom(array, mc, mc.cachedSchema());
            return mc;
        }

        public byte[] serialize(MediaContent content) throws Exception
        {
            try
            {
                return ProtostuffIOUtil.toByteArray(content, content.cachedSchema(), buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

        
    };

    public static class ProtobufMediaSerializer extends Serializer<MediaContent>
    {
        ProtobufMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public MediaContent deserialize(byte[] array) throws Exception
        {
            final MediaContent mc = MediaContent.newBuilder().build();
            ProtobufIOUtil.mergeFrom(array, mc, mc.cachedSchema());
            return mc;
        }

        public byte[] serialize(MediaContent content) throws Exception
        {
            try
            {
                return ProtobufIOUtil.toByteArray(content, content.cachedSchema(), buffer);
            }
            finally
            {
                buffer.clear();
            }
        }
        
    };

    public static class ProtostuffRuntimeMediaSerializer extends Serializer<data.media.MediaContent>
    {
        ProtostuffRuntimeMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }


	    final Schema<data.media.MediaContent> schema = RuntimeSchema.getSchema(data.media.MediaContent.class);
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            ProtostuffIOUtil.mergeFrom(array, mc, schema);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return ProtostuffIOUtil.toByteArray(content, schema, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

        
    };

    public static class  ProtobufRuntimeMediaSerializer extends Serializer<data.media.MediaContent>
    {
        ProtobufRuntimeMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }

	    final Schema<data.media.MediaContent> schema = RuntimeSchema.getSchema(data.media.MediaContent.class);
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            ProtobufIOUtil.mergeFrom(array, mc, schema);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return ProtobufIOUtil.toByteArray(content, schema, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

    };

    public static class ProtostuffManualMediaSerializer extends  Serializer<data.media.MediaContent>
    {
        ProtostuffManualMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            ProtostuffIOUtil.mergeFrom(array, mc, MEDIA_CONTENT_SCHEMA);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return ProtostuffIOUtil.toByteArray(content, MEDIA_CONTENT_SCHEMA, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }
        
    };
    
    public static class ProtostuffGraphMediaSerializer extends Serializer<MediaContent>
    {
        ProtostuffGraphMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public MediaContent deserialize(byte[] array) throws Exception
        {
            final MediaContent mc = MediaContent.newBuilder().build();
            GraphIOUtil.mergeFrom(array, mc, mc.cachedSchema());
            return mc;
        }

        public byte[] serialize(MediaContent content) throws Exception
        {
            try
            {
                return GraphIOUtil.toByteArray(content, content.cachedSchema(), buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

    };

    public class ProtostuffGraphManualMediaSerializer extends  Serializer<data.media.MediaContent>
    {
        ProtostuffGraphManualMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            GraphIOUtil.mergeFrom(array, mc, MEDIA_CONTENT_SCHEMA);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return GraphIOUtil.toByteArray(content, MEDIA_CONTENT_SCHEMA, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

    };

    public static class ProtostuffGraphRuntimeMediaSerializer extends Serializer<data.media.MediaContent>
    {
        ProtostuffGraphRuntimeMediaSerializer(SerializerProperties properties)
        {
            super(properties);
        }

        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);
        final Schema<data.media.MediaContent> schema = RuntimeSchema.getSchema(data.media.MediaContent.class);

        public data.media.MediaContent deserialize(byte[] array) throws Exception
        {
            data.media.MediaContent mc = new data.media.MediaContent();
            GraphIOUtil.mergeFrom(array, mc, schema);
            return mc;
        }

        public byte[] serialize(data.media.MediaContent content) throws Exception
        {
            try
            {
                return GraphIOUtil.toByteArray(content, schema, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }

    }

    static final Schema<data.media.MediaContent> MEDIA_CONTENT_SCHEMA = new Schema<data.media.MediaContent>()
    {
        // schema methods

        public data.media.MediaContent newMessage()
        {
            return new data.media.MediaContent();
        }

        public Class<data.media.MediaContent> typeClass()
        {
            return data.media.MediaContent.class;
        }

        public String messageName()
        {
            return data.media.MediaContent.class.getSimpleName();
        }

        public String messageFullName()
        {
            return data.media.MediaContent.class.getName();
        }

        public boolean isInitialized(data.media.MediaContent message)
        {
            return true;
        }

        public void mergeFrom(Input input, data.media.MediaContent message) throws IOException
        {
            for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        if(message.images == null)
                            message.images = new ArrayList<data.media.Image>();
                        message.images.add(input.mergeObject(null, IMAGE_SCHEMA));
                        break;

                    case 2:
                        message.media = input.mergeObject(message.media, MEDIA_SCHEMA);
                        break;

                    default:
                        input.handleUnknownField(number, this);
                }   
            }
        }


        public void writeTo(Output output, data.media.MediaContent message) throws IOException
        {
            for(data.media.Image image : message.images)
                output.writeObject(1, image, IMAGE_SCHEMA, true);

            output.writeObject(2, message.media, MEDIA_SCHEMA, false);

        }

        public String getFieldName(int number)
        {
            switch(number)
            {
                case 1: return "image";
                case 2: return "media";
                default: return null;
            }
        }

        public int getFieldNumber(String name)
        {
            final Integer number = fieldMap.get(name);
            return number == null ? 0 : number.intValue();
        }

        final java.util.HashMap<String,Integer> fieldMap = new java.util.HashMap<String,Integer>();
        {
            fieldMap.put("image", 1);
            fieldMap.put("media", 2);
        }
    };

    static final Schema<data.media.Media> MEDIA_SCHEMA = new Schema<data.media.Media>()
    {
        // schema methods

        public data.media.Media newMessage()
        {
            return new data.media.Media();
        }

        public Class<data.media.Media> typeClass()
        {
            return data.media.Media.class;
        }

        public String messageName()
        {
            return data.media.Media.class.getSimpleName();
        }

        public String messageFullName()
        {
            return data.media.Media.class.getName();
        }

        public boolean isInitialized(data.media.Media message)
        {
            return true;
        }

        public void mergeFrom(Input input, data.media.Media message) throws IOException
        {
            for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        message.uri = input.readString();
                        break;
                    case 2:
                        message.title = input.readString();
                        break;
                    case 3:
                        message.width = input.readInt32();
                        break;
                    case 4:
                        message.height = input.readInt32();
                        break;
                    case 5:
                        message.format = input.readString();
                        break;
                    case 6:
                        message.duration = input.readInt64();
                        break;
                    case 7:
                        message.size = input.readInt64();
                        break;
                    case 8:
                        message.bitrate = input.readInt32();
                        message.hasBitrate = true;
                        break;
                    case 9:
                        if(message.persons == null)
                            message.persons = new ArrayList<String>();
                        message.persons.add(input.readString());
                        break;
                    case 10:
                        message.player = data.media.Media.Player.values()[input.readEnum()];
                        break;
                    case 11:
                        message.copyright = input.readString();
                        break;
                    default:
                        input.handleUnknownField(number, this);
                }   
            }
        }


        public void writeTo(Output output, data.media.Media message) throws IOException
        {
            output.writeString(1, message.uri, false);

            if(message.title != null)
                output.writeString(2, message.title, false);

            output.writeInt32(3, message.width, false);

            output.writeInt32(4, message.height, false);

            output.writeString(5, message.format, false);

            output.writeInt64(6, message.duration, false);

            output.writeInt64(7, message.size, false);

            if(message.hasBitrate)
                output.writeInt32(8, message.bitrate, false);

            for(String person : message.persons)
            {
                output.writeString(9, person, true);  
            }

            output.writeEnum(10, message.player.ordinal(), false);

            if(message.copyright != null)
                output.writeString(11, message.copyright, false);
        }

        public String getFieldName(int number)
        {
            switch(number)
            {
                case 1: return "uri";
                case 2: return "title";
                case 3: return "width";
                case 4: return "height";
                case 5: return "format";
                case 6: return "duration";
                case 7: return "size";
                case 8: return "bitrate";
                case 9: return "person";
                case 10: return "player";
                case 11: return "copyright";
                default: return null;
            }
        }

        public int getFieldNumber(String name)
        {
            final Integer number = fieldMap.get(name);
            return number == null ? 0 : number.intValue();
        }

        final java.util.HashMap<String,Integer> fieldMap = new java.util.HashMap<String,Integer>();
        {
            fieldMap.put("uri", 1);
            fieldMap.put("title", 2);
            fieldMap.put("width", 3);
            fieldMap.put("height", 4);
            fieldMap.put("format", 5);
            fieldMap.put("duration", 6);
            fieldMap.put("size", 7);
            fieldMap.put("bitrate", 8);
            fieldMap.put("person", 9);
            fieldMap.put("player", 10);
            fieldMap.put("copyright", 11);
        }
    };

    static final Schema<data.media.Image> IMAGE_SCHEMA = new Schema<data.media.Image>()
    {
        // schema methods

        public data.media.Image newMessage()
        {
            return new data.media.Image();
        }

        public Class<data.media.Image> typeClass()
        {
            return data.media.Image.class;
        }

        public String messageName()
        {
            return data.media.Image.class.getSimpleName();
        }

        public String messageFullName()
        {
            return data.media.Image.class.getName();
        }

        public boolean isInitialized(data.media.Image message)
        {
            return true;
        }

        public void mergeFrom(Input input, data.media.Image message) throws IOException
        {
            for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        message.uri = input.readString();
                        break;
                    case 2:
                        message.title = input.readString();
                        break;
                    case 3:
                        message.width = input.readInt32();
                        break;
                    case 4:
                        message.height = input.readInt32();
                        break;
                    case 5:
                        message.size = data.media.Image.Size.values()[input.readEnum()];
                        break;
                    default:
                        input.handleUnknownField(number, this);
                }   
            }
        }


        public void writeTo(Output output, data.media.Image message) throws IOException
        {
            output.writeString(1, message.uri, false);

            if(message.title != null)
                output.writeString(2, message.title, false);

            output.writeInt32(3, message.width, false);

            output.writeInt32(4, message.height, false);

            output.writeEnum(5, message.size.ordinal(), false);
        }

        public String getFieldName(int number)
        {
            switch(number)
            {
                case 1: return "uri";
                case 2: return "title";
                case 3: return "width";
                case 4: return "height";
                case 5: return "size";
                default: return null;
            }
        }

        public int getFieldNumber(String name)
        {
            final Integer number = fieldMap.get(name);
            return number == null ? 0 : number.intValue();
        }

        final java.util.HashMap<String,Integer> fieldMap = new java.util.HashMap<String,Integer>();
        {
            fieldMap.put("uri", 1);
            fieldMap.put("title", 2);
            fieldMap.put("width", 3);
            fieldMap.put("height", 4);
            fieldMap.put("size", 5);
        }
    };
    
    public static final MediaTransformer<MediaContent> mediaTransformer = new MediaTransformer<MediaContent>()
    {
        @Override
        public MediaContent[] resultArray(int size) { return new MediaContent[size]; }
        
            // ----------------------------------------------------------
            // Forward

            public MediaContent forward(data.media.MediaContent mc)
            {
                    MediaContent cb = MediaContent.newBuilder().setMedia(forwardMedia(mc.media)).build();
                    ArrayList<Image> ims = new ArrayList<Image>(mc.images.size());
                    for (data.media.Image image : mc.images) {
                            ims.add(forwardImage(image));
                    }
                    
                    return cb.withImage(ims);
            }

            private Media forwardMedia(data.media.Media media)
            {
                    // Media
                    Media.Builder builder =  Media.newBuilder()
                            .setUri(media.uri)
                            .setWidth(media.width)
                            .setHeight(media.height)
                            .setFormat(media.format)
                            .setDuration(media.duration)
                            .setSize(media.size)
                            .setPlayer(forwardPlayer(media.player))
                        .setBitrate(media.bitrate)
                        .addAllPerson(media.persons);

                    if(media.title != null)
                    {
                        builder.setTitle(media.title);
                    }
                    if(media.copyright != null)
                    {
                        builder.setCopyright(media.copyright);
                    }
                    return builder.build();
            }

            public Media.Player forwardPlayer(data.media.Media.Player p)
            {
                    switch (p) {
                            case JAVA: return Media.Player.JAVA;
                            case FLASH: return Media.Player.FLASH;
                            default:
                                    throw new AssertionError("invalid case: " + p);
                    }
            }

            private Image forwardImage(data.media.Image image)
            {
                    Image.Builder builder =  Image.newBuilder()
                            .setUri(image.uri)
                            .setWidth(image.width)
                            .setHeight(image.height)
                            .setSize(forwardSize(image.size));
                    if(image.title!=null) {
                        builder.setTitle(image.title);
                    }
                    return builder.build();
            }

            public Image.Size forwardSize(data.media.Image.Size s)
            {
                    switch (s) {
                            case SMALL: return Image.Size.SMALL;
                            case LARGE: return Image.Size.LARGE;
                            default:
                                    throw new AssertionError("invalid case: " + s);
                    }
            }

            // ----------------------------------------------------------
            // Reverse

            public data.media.MediaContent reverse(MediaContent mc)
            {
                    List<Image> ims = mc.getImageList();
                    List<data.media.Image> images = new ArrayList<>(ims.size());

                    for (Image image : ims) {
                            images.add(reverseImage(image));
                    }

                    return new data.media.MediaContent(reverseMedia(mc.getMedia()), images);
            }

            private data.media.Media reverseMedia(Media media)
            {
                
                Integer bitRate = media.getBitrate();
                    // Media
                    return new data.media.Media(
                            media.getUri(),
                            media.getTitle(),
                            media.getWidth(),
                            media.getHeight(),
                            media.getFormat(),
                            media.getDuration(),
                            media.getSize(),
                            bitRate == null ? 0 : bitRate,
                            bitRate != null,
                            new ArrayList<String>(media.getPersonList()),
                            reversePlayer(media.getPlayer()),
                            media.getCopyright()
                    );
            }

            public data.media.Media.Player reversePlayer(Media.Player p)
            {
                    switch (p) {
                            case JAVA:  return data.media.Media.Player.JAVA;
                            case FLASH: return data.media.Media.Player.FLASH;
                            default:
                                    throw new AssertionError("invalid case: " + p);
                    }
            }

            private data.media.Image reverseImage(Image image)
            {
                    return new data.media.Image(
                            image.getUri(),
                            image.getTitle(),
                            image.getWidth(),
                            image.getHeight(),
                            reverseSize(image.getSize()));
            }

            public data.media.Image.Size reverseSize(Image.Size s)
            {
                    switch (s) {
                            case SMALL: return data.media.Image.Size.SMALL;
                            case LARGE: return data.media.Image.Size.LARGE;
                            default:
                                    throw new AssertionError("invalid case: " + s);
                    }
            }

            public data.media.MediaContent shallowReverse(MediaContent mc)
            {
                    return new data.media.MediaContent(reverseMedia(mc.getMedia()), Collections.<data.media.Image>emptyList());
            }
    };

}
