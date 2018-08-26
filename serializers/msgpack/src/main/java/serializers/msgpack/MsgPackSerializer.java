package serializers.msgpack;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import serializers.Serializer;
import serializers.core.metadata.SerializerProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MsgPackSerializer extends Serializer<MediaContent> {

  
    
    public MsgPackSerializer(SerializerProperties properties) {
        super(properties);
        
     }
    
     @Override
     public byte[] serialize(MediaContent content) throws Exception {
         MessageBufferPacker packer =  MessagePack.newDefaultBufferPacker();
         packMedia(packer, content.getMedia());
         packer.packArrayHeader(content.getImages().size());
         for(Image img: content.getImages()) {
             packImages(packer, img);
         }
         packer.close();
         return packer.toByteArray();
     }

    private void packImages(MessageBufferPacker packer, Image img) throws IOException {

        packString(packer, img.getUri());
        packString(packer, img.getTitle());
        packer.packInt(img.getWidth());
        packer.packInt(img.getHeight());
        packer.packInt(img.getSize().ordinal());

    }

    private void packMedia(MessageBufferPacker packer, Media media) throws IOException {

        packString(packer, media.getUri());
        packString(packer, media.getTitle());
        packer.packInt(media.getWidth());
        packer.packInt(media.getHeight());
        packString(packer, media.getFormat());
        packer.packLong(media.getDuration());
        packer.packLong(media.getSize());
        packer.packInt(media.getBitrate());

        packer.packBoolean(media.hasBitrate);
        packer.packArrayHeader(media.getPersons().size());
        for (String person : media.getPersons()) {
            packString(packer, person);
        }
        packer.packInt(media.getPlayer().ordinal());
        packString(packer, media.getCopyright());
    }

    private void packString(MessageBufferPacker packer, String string) throws IOException {
        if(string!=null)
        {
            packer.packString(string);
        }
        else
        {
            packer.packNil();
        }
    }



    @Override
     public MediaContent deserialize(byte[] array) throws Exception {

         MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(array);
         Media media  = unpackMedia(unpacker);
         int numImages = unpacker.unpackArrayHeader();
         List<Image> list = new ArrayList<>();
         for(int count = 0; count < numImages; count++)
         {
             list.add(unpackImage(unpacker));
         }
         return new MediaContent(media, list);
     }

    private Image unpackImage(MessageUnpacker unpacker) throws IOException {

        Image img = new Image();
        img.setUri(unpackString(unpacker));
        img.setTitle(unpackString(unpacker));
        img.setWidth(unpacker.unpackInt());
        img.setHeight(unpacker.unpackInt());
        img.setSize(Image.Size.values()[unpacker.unpackInt()]);
        return img;

    }

    private Media unpackMedia(MessageUnpacker unpacker) throws IOException {
        Media media = new Media();
        media.setUri(unpackString(unpacker));
        media.setTitle(unpackString(unpacker));
        media.setWidth(unpacker.unpackInt());
        media.setHeight(unpacker.unpackInt());
        media.setFormat(unpackString(unpacker));
        media.setDuration(unpacker.unpackLong());
        media.setSize(unpacker.unpackLong());
        media.setBitrate(unpacker.unpackInt());

        media.hasBitrate = unpacker.unpackBoolean();
        int countPersons =  unpacker.unpackArrayHeader();
        List<String> persons = new ArrayList<>(countPersons);
        for (int index=0 ; index < countPersons; index++) {
            persons.add(unpackString(unpacker));
        }
        media.setPersons(persons);
        media.setPlayer(Media.Player.values()[unpacker.unpackInt()]);
        media.setCopyright(unpackString(unpacker));
        return media;
    }

    private String unpackString(MessageUnpacker unpacker) throws IOException {


        if(unpacker.getNextFormat().getValueType().isNilType())
        {
            unpacker.unpackNil();
            return null;
        }
        else
        {
            return unpacker.unpackString();
        }
    }
}