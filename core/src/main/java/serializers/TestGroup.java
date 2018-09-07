package serializers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import serializers.core.metadata.SerializerProperties;

public final class TestGroup<J> {
  //public final Map<String,Entry<J,Object>> entries = new LinkedHashMap<>();

  private Table<String, SerializerProperties, Entry<J, Object>> entries = HashBasedTable.create();

  public Table getTable() {
    return entries;
  }

  public <S> void add(Transformer<J, S> transformer, Serializer<? extends S> serializer) {
    add_(transformer, serializer);

  }


  private <S> Entry<J, Object> add_(Transformer<J, S> transformer,
      Serializer<? extends S> serializer) {
    Entry<J, S> entry = new Entry<>(transformer, serializer);

    @SuppressWarnings("unchecked")
    Entry<J, Object> entry_ = (Entry<J, Object>) entry;

    String name = entry_.serializer.getName();
    Object displaced = entries.put(name, serializer.serializerProperties, entry_);
    if (displaced != null) {
      throw new AssertionError("duplicate serializer: \"" + name + "\"");
    }

    return entry_;
  }


  public static final class Entry<J, S> {

    public final Transformer<J, S> transformer;
    public final Serializer<S> serializer;

    public Entry(Transformer<J, S> transformer, Serializer<? extends S> serializer) {
      this.transformer = transformer;
      this.serializer = (Serializer<S>) serializer;
    }
  }
}
