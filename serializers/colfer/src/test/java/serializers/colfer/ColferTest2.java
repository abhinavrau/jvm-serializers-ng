package serializers.colfer;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializers.TestGroups;

import serializers.colfer.media.*;
import serializers.core.Validator;


import java.lang.reflect.InvocationTargetException;



public class ColferTest2 {

	static final Logger logger = LoggerFactory.getLogger(ColferTest2.class);

	public static class ImageConverter implements Converter
	{
		@Override
		public <T> T convert(Class<T> type, Object value) {
			if(value instanceof data.media.Image || value instanceof data.media.Media)
			{
				T object = null;
				try {
					object = type.newInstance();
					BeanUtils.copyProperties(object, value);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
					logger.error("Exception converting Image", e);
				}
				return object;
			}
			return null;
		}
	}
	public static class EnumConverter implements Converter
	{

		@Override
		public <T> T convert(Class<T> type, Object value) {
//			if(value instanceof data.media.Media.Player)
//			{
//				data.media.Media.Player player = (data.media.Media.Player) value;
//				Player object = new Player();
//				switch (player)
//				{
//					case JAVA: object.setJava(true);
//								break;
//					case FLASH: object.setFlash(true);
//								break;
//					default:
//
//				}
//				return (T) object;
//			}
//			if(value instanceof data.media.Image.Size)
//			{
//				data.media.Image.Size size = (data.media.Image.Size) value;
//				Size object = new Size();
//				switch (size)
//				{
//					case SMALL: object.setSmall(true);
//						break;
//					case LARGE: object.setLarge(true);
//						break;
//					default:
//				}
//				return (T) object;
//
//			}
			return null;
		}
	}

	static
	{
		ConvertUtils.register(new ArrayConverter(Image[].class, new ImageConverter()), Image[].class);
		ConvertUtils.register(new ImageConverter(), Media.class);
		ConvertUtils.register(new EnumConverter(), Player.class);
		ConvertUtils.register(new EnumConverter(), Size.class);
	}

	@Test
	public void TestCorrectness() throws Throwable
	{



		TestGroups groups = new TestGroups();
		Validator<data.media.MediaContent, MediaContent> val =
				new Validator<>(data.media.MediaContent.class, MediaContent.class);

		Colfer.register(groups);

		//val.checkForCorrectness(groups, "data");

	}
}
