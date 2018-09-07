package serializers.fst;

import static serializers.core.metadata.SerializerProperties.APIStyle.REFLECTION;
import static serializers.core.metadata.SerializerProperties.Features.JSON_CONVERTER;
import static serializers.core.metadata.SerializerProperties.Features.OPTIMIZED;
import static serializers.core.metadata.SerializerProperties.Features.SUPPORTS_CYCLIC_REFERENCES;
import static serializers.core.metadata.SerializerProperties.Format.BINARY;
import static serializers.core.metadata.SerializerProperties.Format.BINARY_JDK_COMPATIBLE;
import static serializers.core.metadata.SerializerProperties.Mode.CODE_FIRST;
import static serializers.core.metadata.SerializerProperties.ValueType.POJO;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectInputNoShared;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.FSTObjectOutputNoShared;
import serializers.JavaBuiltIn;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.TestGroup;
import serializers.Transformer;
import serializers.core.metadata.SerializerProperties;

/**
 * Copyright (c) 2012, Ruediger Moeller. All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 * <p/>
 * Date: 01.03.14 Time: 23:59 To change this template use File | Settings | File Templates.
 */
public class FastSerialization {

  public static void register(MediaContentTestGroup groups) {

    register(groups.media, JavaBuiltIn.mediaTransformer);
  }

  private static <T, S> void register(TestGroup<T> group, Transformer<T, S> transformer) {

    SerializerProperties.SerializerPropertiesBuilder builder = SerializerProperties.builder();
    SerializerProperties properties = builder.format(BINARY)
        .apiStyle(REFLECTION)
        .mode(CODE_FIRST)
        .valueType(POJO)
        .name("fst")
        .feature(JSON_CONVERTER)
        .projectUrl("https://github.com/RuedigerMoeller/fast-serialization")
        .build();

    group.add(transformer, new BasicSerializer<>(properties, true, false));

    SerializerProperties prereg_properties = properties.toBuilder()
        .feature(OPTIMIZED)
        .build();

    group.add(transformer, new BasicSerializer<>(prereg_properties, true, true));

    SerializerProperties jdkcompat_properties = properties.toBuilder()
        .format(BINARY_JDK_COMPATIBLE)
        .feature(SUPPORTS_CYCLIC_REFERENCES)
        .build();

    group.add(transformer, new BasicSerializer<>(jdkcompat_properties, false, false));

  }

  // ------------------------------------------------------------
  // Serializers

  /**
   * setup similar to kryo
   */
  public static class BasicSerializer<T> extends Serializer<T> {

    final static FSTConfiguration confUnsharedUnregistered;
    final static FSTConfiguration confUnsharedRegister;
    final static FSTConfiguration confShared;

    static {
//            System.setProperty("fst.unsafe", "true");
      confUnsharedUnregistered = FSTConfiguration.createDefaultConfiguration();
      confUnsharedUnregistered.setShareReferences(false);

      confUnsharedRegister = FSTConfiguration.createDefaultConfiguration();
      confUnsharedRegister.setShareReferences(false);
      confUnsharedRegister.registerClass(
          Image.Size.class,
          Image.class,
          Media.Player.class,
          Media.class,
          MediaContent[].class,
          MediaContent.class,
          MediaContent.class);

      confShared = FSTConfiguration.createDefaultConfiguration();
    }

    FSTObjectInput objectInput;
    FSTObjectOutput objectOutput;
    boolean unshared;
    String name;
    Class type[] = {MediaContent.class};

    public BasicSerializer(SerializerProperties properties, boolean flat, boolean register) {
      super(properties);
      this.unshared = flat;
      if (flat) {
        if (register) {
          objectInput = new FSTObjectInputNoShared(confUnsharedRegister);
          objectOutput = new FSTObjectOutputNoShared(confUnsharedRegister);
        } else {
          objectInput = new FSTObjectInputNoShared(confUnsharedUnregistered);
          objectOutput = new FSTObjectOutputNoShared(confUnsharedUnregistered);
        }
      } else {
        objectInput = new FSTObjectInput(confShared);
        objectOutput = new FSTObjectOutput(confShared);
      }
    }

    @SuppressWarnings("unchecked")
    public T deserialize(byte[] array) {
      return (T) deserializeInternal(array);
    }

    private Object deserializeInternal(byte[] array) {
      try {
        objectInput.resetForReuseUseArray(array);
        return objectInput.readObject(type);
      } catch (Throwable e) {
        e.printStackTrace();
      }
      return null;
    }

    public byte[] serialize(T content) {
      try {
        objectOutput.resetForReUse();
        objectOutput.writeObject(content, type);
        return objectOutput.getCopyOfWrittenBuffer();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    public void serializeItems(T[] items, OutputStream outStream) throws Exception {
      objectOutput.resetForReUse();
      for (T item : items) {
        objectOutput.writeObject(item);
      }
      outStream.write(objectOutput.getBuffer(), 0, objectOutput.getWritten()); // avoid copy
    }

    @SuppressWarnings("unchecked")
    public T[] deserializeItems(InputStream inStream, int numberOfItems) throws IOException {
      try {
        MediaContent[] result = new MediaContent[numberOfItems];
        objectInput.resetForReuse(inStream);
        for (int i = 0; i < numberOfItems; i++) {
          result[i] = (MediaContent) objectInput.readObject();
        }
        return (T[]) result;
      } catch (Throwable e) {
        e.printStackTrace();
      }
      return null;
    }

  }

}
