package serializers.java;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import org.mapstruct.Mapper;


@Mapper
public interface MediaMapper {

  MediaContent map(MediaContent content);

  Media map(Media content);

  Image map(Image content);

}
