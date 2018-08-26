package serializers.dslplatform.data.media;

import com.dslplatform.json.CompiledJson;
import data.media.Image;
import data.media.Media;
import data.media.MediaContent;

import java.util.List;

@CompiledJson
public class DSLMediaContent extends MediaContent {

	public DSLMediaContent() {}

	public DSLMediaContent (Media media, List<Image> images) {
		super(media,images);
	}
}
