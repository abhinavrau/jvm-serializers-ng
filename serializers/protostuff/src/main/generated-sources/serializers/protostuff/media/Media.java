package serializers.protostuff.media;

@javax.annotation.Generated("src/main/protostuff/media.protostuff.proto")
public final class Media
        implements io.protostuff.Message<Media> {

    private static final Media DEFAULT_INSTANCE = newBuilder().build();

    private String uri;

    private String title;

    private int width;

    private int height;

    private String format;

    private long duration;

    private long size;

    private int bitrate;

    private java.util.List<String> person;

    private int player;

    private String copyright;

    private boolean __merge_lock = false;
    private int __bitField0;

    private Media() {
        this.uri = "";
        this.title = "";
        this.width = 0;
        this.height = 0;
        this.format = "";
        this.duration = 0L;
        this.size = 0L;
        this.bitrate = 0;
        this.person = java.util.Collections.emptyList();
        this.player = serializers.protostuff.media.Media.Player.JAVA.getNumber();
        this.copyright = "";
    }

    private Media(Builder builder) {
        __merge_lock = true;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Media getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static io.protostuff.Schema<Media> getSchema()
    {
    	return Schema.INSTANCE;
    }

    public String getUri() {
        return uri;
    }

    public boolean hasUri() {
        return (__bitField0 & 1) == 1;
    }

    public Media withUri(String value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setUri(value)
            .build();
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTitle() {
        return (__bitField0 & 2) == 2;
    }

    public Media withTitle(String value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setTitle(value)
            .build();
    }

    public int getWidth() {
        return width;
    }

    public boolean hasWidth() {
        return (__bitField0 & 4) == 4;
    }

    public Media withWidth(int value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setWidth(value)
            .build();
    }

    public int getHeight() {
        return height;
    }

    public boolean hasHeight() {
        return (__bitField0 & 8) == 8;
    }

    public Media withHeight(int value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setHeight(value)
            .build();
    }

    public String getFormat() {
        return format;
    }

    public boolean hasFormat() {
        return (__bitField0 & 16) == 16;
    }

    public Media withFormat(String value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setFormat(value)
            .build();
    }

    public long getDuration() {
        return duration;
    }

    public boolean hasDuration() {
        return (__bitField0 & 32) == 32;
    }

    public Media withDuration(long value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setDuration(value)
            .build();
    }

    public long getSize() {
        return size;
    }

    public boolean hasSize() {
        return (__bitField0 & 64) == 64;
    }

    public Media withSize(long value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setSize(value)
            .build();
    }

    public int getBitrate() {
        return bitrate;
    }

    public boolean hasBitrate() {
        return (__bitField0 & 128) == 128;
    }

    public Media withBitrate(int value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setBitrate(value)
            .build();
    }

    public java.util.List<String> getPersonList() {
        return person;
    }

    public int getPersonCount() {
        return person.size();
    }

    public String getPerson(int index) {
        return person.get(index);
    }

    public Media withPerson(java.util.List<String> value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .clearPerson()
            .addAllPerson(value)
            .build();
    }

    public serializers.protostuff.media.Media.Player getPlayer() {
        return serializers.protostuff.media.Media.Player.valueOf(player);
    }

    public int getPlayerValue() {
        return player;
    }

    public boolean hasPlayer() {
        return (__bitField0 & 512) == 512;
    }

    public Media withPlayer(serializers.protostuff.media.Media.Player value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setPlayer(value)
            .build();
    }

    public String getCopyright() {
        return copyright;
    }

    public boolean hasCopyright() {
        return (__bitField0 & 1024) == 1024;
    }

    public Media withCopyright(String value) {
        return Media.newBuilder()
            .mergeFrom(this)
            .setCopyright(value)
            .build();
    }



    @Override
    public io.protostuff.Schema<Media> cachedSchema() {
        return Schema.INSTANCE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Media that = (Media) obj;
        if (!java.util.Objects.equals(this.uri, that.uri)) {
            return false;
        }
        if (!java.util.Objects.equals(this.title, that.title)) {
            return false;
        }
        if (!java.util.Objects.equals(this.width, that.width)) {
            return false;
        }
        if (!java.util.Objects.equals(this.height, that.height)) {
            return false;
        }
        if (!java.util.Objects.equals(this.format, that.format)) {
            return false;
        }
        if (!java.util.Objects.equals(this.duration, that.duration)) {
            return false;
        }
        if (!java.util.Objects.equals(this.size, that.size)) {
            return false;
        }
        if (!java.util.Objects.equals(this.bitrate, that.bitrate)) {
            return false;
        }
        if (!java.util.Objects.equals(this.person, that.person)) {
            return false;
        }
        if (!java.util.Objects.equals(this.player, that.player)) {
            return false;
        }
        if (!java.util.Objects.equals(this.copyright, that.copyright)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.uri == null ? 0 : this.uri.hashCode());
        result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
        result = 31 * result + Integer.hashCode(this.width);
        result = 31 * result + Integer.hashCode(this.height);
        result = 31 * result + (this.format == null ? 0 : this.format.hashCode());
        result = 31 * result + Long.hashCode(this.duration);
        result = 31 * result + Long.hashCode(this.size);
        result = 31 * result + Integer.hashCode(this.bitrate);
        result = 31 * result + (this.person == null ? 0 : this.person.hashCode());
        result = 31 * result + Integer.hashCode(this.player);
        result = 31 * result + (this.copyright == null ? 0 : this.copyright.hashCode());

        return result;
    }

    @Override
    public String toString() {
        java.util.List<String> parts = new java.util.ArrayList<>();
        if (hasUri()) {
            parts.add("uri=" + getUri());
        }
        if (hasTitle()) {
            parts.add("title=" + getTitle());
        }
        if (hasWidth()) {
            parts.add("width=" + getWidth());
        }
        if (hasHeight()) {
            parts.add("height=" + getHeight());
        }
        if (hasFormat()) {
            parts.add("format=" + getFormat());
        }
        if (hasDuration()) {
            parts.add("duration=" + getDuration());
        }
        if (hasSize()) {
            parts.add("size=" + getSize());
        }
        if (hasBitrate()) {
            parts.add("bitrate=" + getBitrate());
        }
        if (!person.isEmpty()) {
            parts.add("person=" + getPersonList());
        }
        if (hasPlayer()) {
            parts.add("player=" + getPlayer() + '(' + getPlayerValue() + ')');
        }
        if (hasCopyright()) {
            parts.add("copyright=" + getCopyright());
        }
        return "Media{" + String.join(", ", parts) + "}";
    }

    public static final class Schema implements io.protostuff.Schema<Media>{

        private static final Schema INSTANCE = new Schema();

        private static final java.util.Map<String,Integer> __fieldMap = new java.util.HashMap<>();

        static {
        	__fieldMap.put("uri", 1);
        	__fieldMap.put("title", 2);
        	__fieldMap.put("width", 3);
        	__fieldMap.put("height", 4);
        	__fieldMap.put("format", 5);
        	__fieldMap.put("duration", 6);
        	__fieldMap.put("size", 7);
        	__fieldMap.put("bitrate", 8);
        	__fieldMap.put("person", 9);
        	__fieldMap.put("player", 10);
        	__fieldMap.put("copyright", 11);
        }

        @Override
        public Media newMessage() {
            return new Media();
        }

        @Override
        public Class<Media> typeClass() {
            return Media.class;
        }

        @Override
        public String messageName() {
            return Media.class.getSimpleName();
        }

        @Override
        public String messageFullName() {
            return Media.class.getName();
        }

        @Override
        @Deprecated
        public boolean isInitialized(Media message) {
            return true;
        }

        @Override
        public void mergeFrom(io.protostuff.Input input, Media instance) throws java.io.IOException {
        	if (instance.__merge_lock) {
        		throw new IllegalStateException("Cannot reuse message instance");
        	} else {
        		instance.__merge_lock = true;
        	}
        	while(true) {
        		int number = input.readFieldNumber(this);
                if (number == 0) {
                    break;
                }
                switch(number) {
                    case 1:
                    	instance.uri = input.readString();
                    	instance.__bitField0 |= 1;
                    	break;
                    case 2:
                    	instance.title = input.readString();
                    	instance.__bitField0 |= 2;
                    	break;
                    case 3:
                    	instance.width = input.readInt32();
                    	instance.__bitField0 |= 4;
                    	break;
                    case 4:
                    	instance.height = input.readInt32();
                    	instance.__bitField0 |= 8;
                    	break;
                    case 5:
                    	instance.format = input.readString();
                    	instance.__bitField0 |= 16;
                    	break;
                    case 6:
                    	instance.duration = input.readInt64();
                    	instance.__bitField0 |= 32;
                    	break;
                    case 7:
                    	instance.size = input.readInt64();
                    	instance.__bitField0 |= 64;
                    	break;
                    case 8:
                    	instance.bitrate = input.readInt32();
                    	instance.__bitField0 |= 128;
                    	break;
                    case 9:
                    	if(!((instance.__bitField0 & 256) == 256)) {
                    	    instance.person = new java.util.ArrayList<>();
                    	    instance.__bitField0 |= 256;
                    	}
                    	instance.person.add(input.readString());

                    	break;
                    case 10:
                    	instance.player = input.readEnum();
                    	instance.__bitField0 |= 512;
                    	break;
                    case 11:
                    	instance.copyright = input.readString();
                    	instance.__bitField0 |= 1024;
                    	break;
                    default:
                        input.handleUnknownField(number, this);
                }
        	}
            if((instance.__bitField0 & 256) == 256) {
                instance.person = java.util.Collections.unmodifiableList(instance.person);
            }



        }

        @Override
        public void writeTo(io.protostuff.Output output, Media instance) throws java.io.IOException {
            if((instance.__bitField0 & 1) == 1) {
                output.writeString(1, instance.uri, false);
            }

            if((instance.__bitField0 & 2) == 2) {
                output.writeString(2, instance.title, false);
            }

            if((instance.__bitField0 & 4) == 4) {
                output.writeInt32(3, instance.width, false);
            }

            if((instance.__bitField0 & 8) == 8) {
                output.writeInt32(4, instance.height, false);
            }

            if((instance.__bitField0 & 16) == 16) {
                output.writeString(5, instance.format, false);
            }

            if((instance.__bitField0 & 32) == 32) {
                output.writeInt64(6, instance.duration, false);
            }

            if((instance.__bitField0 & 64) == 64) {
                output.writeInt64(7, instance.size, false);
            }

            if((instance.__bitField0 & 128) == 128) {
                output.writeInt32(8, instance.bitrate, false);
            }

            for(String person : instance.person) {
                output.writeString(9, person, true);
            }

            if((instance.__bitField0 & 512) == 512) {
            	output.writeEnum(10, instance.player, false);
            }

            if((instance.__bitField0 & 1024) == 1024) {
                output.writeString(11, instance.copyright, false);
            }

        }

        @Override
        public String getFieldName(int number) {
        	switch(number) {
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

        @Override
        public int getFieldNumber(String name) {
        	final Integer number = __fieldMap.get(name);
        	return number == null ? 0 : number.intValue();
        }

    }

    public static final class Builder  {

        private String uri;

        private String title;

        private int width;

        private int height;

        private String format;

        private long duration;

        private long size;

        private int bitrate;

        private java.util.List<String> person;

        private int player;

        private String copyright;

        private int __bitField0;

        private Builder() {
            this.uri = "";
            this.title = "";
            this.width = 0;
            this.height = 0;
            this.format = "";
            this.duration = 0L;
            this.size = 0L;
            this.bitrate = 0;
            this.person = java.util.Collections.emptyList();
            this.player = serializers.protostuff.media.Media.Player.JAVA.getNumber();
            this.copyright = "";
        }

        public Builder mergeFrom(Media instance) {
            if (instance.hasUri()) {
                this.setUri(instance.getUri());
            }

            if (instance.hasTitle()) {
                this.setTitle(instance.getTitle());
            }

            if (instance.hasWidth()) {
                this.setWidth(instance.getWidth());
            }

            if (instance.hasHeight()) {
                this.setHeight(instance.getHeight());
            }

            if (instance.hasFormat()) {
                this.setFormat(instance.getFormat());
            }

            if (instance.hasDuration()) {
                this.setDuration(instance.getDuration());
            }

            if (instance.hasSize()) {
                this.setSize(instance.getSize());
            }

            if (instance.hasBitrate()) {
                this.setBitrate(instance.getBitrate());
            }

            this.addAllPerson(instance.getPersonList());

            if (instance.hasPlayer()) {
                this.setPlayer(instance.getPlayer());
            }

            if (instance.hasCopyright()) {
                this.setCopyright(instance.getCopyright());
            }

            return this;
        }

        public String getUri() {
            return uri;
        }

        public Builder setUri(String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#uri to null");
            }

            this.uri = value;
            __bitField0 |= 1;
            return this;
        }

        public Builder clearUri() {
            this.uri = "";
            __bitField0 &= ~1;
            return this;
        }

        public boolean hasUri() {
            return (__bitField0 & 1) == 1;
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#title to null");
            }

            this.title = value;
            __bitField0 |= 2;
            return this;
        }

        public Builder clearTitle() {
            this.title = "";
            __bitField0 &= ~2;
            return this;
        }

        public boolean hasTitle() {
            return (__bitField0 & 2) == 2;
        }

        public int getWidth() {
            return width;
        }

        public Builder setWidth(int value) {
            this.width = value;
            __bitField0 |= 4;
            return this;
        }

        public Builder clearWidth() {
            this.width = 0;
            __bitField0 &= ~4;
            return this;
        }

        public boolean hasWidth() {
            return (__bitField0 & 4) == 4;
        }

        public int getHeight() {
            return height;
        }

        public Builder setHeight(int value) {
            this.height = value;
            __bitField0 |= 8;
            return this;
        }

        public Builder clearHeight() {
            this.height = 0;
            __bitField0 &= ~8;
            return this;
        }

        public boolean hasHeight() {
            return (__bitField0 & 8) == 8;
        }

        public String getFormat() {
            return format;
        }

        public Builder setFormat(String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#format to null");
            }

            this.format = value;
            __bitField0 |= 16;
            return this;
        }

        public Builder clearFormat() {
            this.format = "";
            __bitField0 &= ~16;
            return this;
        }

        public boolean hasFormat() {
            return (__bitField0 & 16) == 16;
        }

        public long getDuration() {
            return duration;
        }

        public Builder setDuration(long value) {
            this.duration = value;
            __bitField0 |= 32;
            return this;
        }

        public Builder clearDuration() {
            this.duration = 0L;
            __bitField0 &= ~32;
            return this;
        }

        public boolean hasDuration() {
            return (__bitField0 & 32) == 32;
        }

        public long getSize() {
            return size;
        }

        public Builder setSize(long value) {
            this.size = value;
            __bitField0 |= 64;
            return this;
        }

        public Builder clearSize() {
            this.size = 0L;
            __bitField0 &= ~64;
            return this;
        }

        public boolean hasSize() {
            return (__bitField0 & 64) == 64;
        }

        public int getBitrate() {
            return bitrate;
        }

        public Builder setBitrate(int value) {
            this.bitrate = value;
            __bitField0 |= 128;
            return this;
        }

        public Builder clearBitrate() {
            this.bitrate = 0;
            __bitField0 &= ~128;
            return this;
        }

        public boolean hasBitrate() {
            return (__bitField0 & 128) == 128;
        }

        public java.util.List<String> getPersonList() {
            return person;
        }

        public Builder setPerson(int index, String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#person to null");
            }

            if(!((__bitField0 & 256) == 256)) {
                this.person = new java.util.ArrayList<>();
                __bitField0 |= 256;
            }
            this.person.set(index, value);
            return this;
        }

        public Builder addPerson(String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#person to null");
            }

            if(!((__bitField0 & 256) == 256)) {
                this.person = new java.util.ArrayList<>();
                __bitField0 |= 256;
            }
            this.person.add(value);
            return this;
        }

        public Builder addAllPerson(java.lang.Iterable<String> values) {
            if (values == null) {
                throw new NullPointerException("Cannot set Media#person to null");
            }
            if(!((__bitField0 & 256) == 256)) {
                this.person = new java.util.ArrayList<>();
                __bitField0 |= 256;
            }
            for (final String value : values) {
                if (value == null) {
                   throw new NullPointerException("Cannot set Media#person to null");
                }
                this.person.add(value);
            }
            return this;
        }

        public Builder clearPerson() {
            this.person = java.util.Collections.emptyList();
            __bitField0 &= ~256;
            return this;
        }

        public int getPersonCount() {
            return person.size();
        }

        public String getPerson(int index) {
            return person.get(index);
        }

        public serializers.protostuff.media.Media.Player getPlayer() {
            return serializers.protostuff.media.Media.Player.valueOf(player);
        }

        public int getPlayerValue() {
            return player;
        }

        public Builder setPlayer(serializers.protostuff.media.Media.Player value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#player to null");
            }

            if (value == serializers.protostuff.media.Media.Player.UNRECOGNIZED) {
                throw new IllegalArgumentException("Cannot set Media#player to UNRECOGNIZED");
            }

            this.player = value.getNumber();
            __bitField0 |= 512;
            return this;
        }

        public Builder setPlayerValue(int value) {
            this.player = value;
            __bitField0 |= 512;
            return this;
        }

        public Builder clearPlayer() {
            this.player = 0;
            __bitField0 &= ~512;
            return this;
        }

        public boolean hasPlayer() {
            return (__bitField0 & 512) == 512;
        }

        public String getCopyright() {
            return copyright;
        }

        public Builder setCopyright(String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Media#copyright to null");
            }

            this.copyright = value;
            __bitField0 |= 1024;
            return this;
        }

        public Builder clearCopyright() {
            this.copyright = "";
            __bitField0 &= ~1024;
            return this;
        }

        public boolean hasCopyright() {
            return (__bitField0 & 1024) == 1024;
        }




        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            Builder that = (Builder) obj;
            if (!java.util.Objects.equals(this.uri, that.uri)) {
                return false;
            }
            if (!java.util.Objects.equals(this.title, that.title)) {
                return false;
            }
            if (!java.util.Objects.equals(this.width, that.width)) {
                return false;
            }
            if (!java.util.Objects.equals(this.height, that.height)) {
                return false;
            }
            if (!java.util.Objects.equals(this.format, that.format)) {
                return false;
            }
            if (!java.util.Objects.equals(this.duration, that.duration)) {
                return false;
            }
            if (!java.util.Objects.equals(this.size, that.size)) {
                return false;
            }
            if (!java.util.Objects.equals(this.bitrate, that.bitrate)) {
                return false;
            }
            if (!java.util.Objects.equals(this.person, that.person)) {
                return false;
            }
            if (!java.util.Objects.equals(this.player, that.player)) {
                return false;
            }
            if (!java.util.Objects.equals(this.copyright, that.copyright)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + (this.uri == null ? 0 : this.uri.hashCode());
            result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
            result = 31 * result + Integer.hashCode(this.width);
            result = 31 * result + Integer.hashCode(this.height);
            result = 31 * result + (this.format == null ? 0 : this.format.hashCode());
            result = 31 * result + Long.hashCode(this.duration);
            result = 31 * result + Long.hashCode(this.size);
            result = 31 * result + Integer.hashCode(this.bitrate);
            result = 31 * result + (this.person == null ? 0 : this.person.hashCode());
            result = 31 * result + Integer.hashCode(this.player);
            result = 31 * result + (this.copyright == null ? 0 : this.copyright.hashCode());

            return result;
        }

        @Override
        public String toString() {
            java.util.List<String> parts = new java.util.ArrayList<>();
            if (hasUri()) {
                parts.add("uri=" + getUri());
            }
            if (hasTitle()) {
                parts.add("title=" + getTitle());
            }
            if (hasWidth()) {
                parts.add("width=" + getWidth());
            }
            if (hasHeight()) {
                parts.add("height=" + getHeight());
            }
            if (hasFormat()) {
                parts.add("format=" + getFormat());
            }
            if (hasDuration()) {
                parts.add("duration=" + getDuration());
            }
            if (hasSize()) {
                parts.add("size=" + getSize());
            }
            if (hasBitrate()) {
                parts.add("bitrate=" + getBitrate());
            }
            if (!person.isEmpty()) {
                parts.add("person=" + getPersonList());
            }
            if (hasPlayer()) {
                parts.add("player=" + getPlayer() + '(' + getPlayerValue() + ')');
            }
            if (hasCopyright()) {
                parts.add("copyright=" + getCopyright());
            }
            return "Media{" + String.join(", ", parts) + "}";
        }

        public Media build() {
            serializers.protostuff.media.Media result = new serializers.protostuff.media.Media();
            result.__bitField0 = __bitField0;
            result.uri = this.uri;
            result.title = this.title;
            result.width = this.width;
            result.height = this.height;
            result.format = this.format;
            result.duration = this.duration;
            result.size = this.size;
            result.bitrate = this.bitrate;
            result.person = java.util.Collections.unmodifiableList(this.person);
            result.player = this.player;
            result.copyright = this.copyright;
            return result;
        }

    }


    public enum Player implements io.protostuff.EnumLite<Player>{

        JAVA(0),
        FLASH(1),
        UNRECOGNIZED(-1);

        private final int value;

        private Player(int n) {
            this.value = n;
        }

        @Override
        public int getNumber() {
            if (value == -1) {
              throw new java.lang.IllegalStateException(
                  "Can't get the number of an unknown enum value.");
            }
            return value;
        }

        public static final Player valueOf(int tag) {
            switch(tag) {
                case 0: return JAVA;
                case 1: return FLASH;
                default: return UNRECOGNIZED;
            }
        }
    }

}