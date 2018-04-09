package serializers.protostuff.media;

@javax.annotation.Generated("src/main/protostuff/media.protostuff.proto")
public final class Image
        implements io.protostuff.Message<Image> {

    private static final Image DEFAULT_INSTANCE = newBuilder().build();

    private String uri;

    private String title;

    private int width;

    private int height;

    private int size;

    private boolean __merge_lock = false;
    private int __bitField0;

    private Image() {
        this.uri = "";
        this.title = "";
        this.width = 0;
        this.height = 0;
        this.size = serializers.protostuff.media.Image.Size.SMALL.getNumber();
    }

    private Image(Builder builder) {
        __merge_lock = true;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Image getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static io.protostuff.Schema<Image> getSchema()
    {
    	return Schema.INSTANCE;
    }

    public String getUri() {
        return uri;
    }

    public boolean hasUri() {
        return (__bitField0 & 1) == 1;
    }

    public Image withUri(String value) {
        return Image.newBuilder()
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

    public Image withTitle(String value) {
        return Image.newBuilder()
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

    public Image withWidth(int value) {
        return Image.newBuilder()
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

    public Image withHeight(int value) {
        return Image.newBuilder()
            .mergeFrom(this)
            .setHeight(value)
            .build();
    }

    public serializers.protostuff.media.Image.Size getSize() {
        return serializers.protostuff.media.Image.Size.valueOf(size);
    }

    public int getSizeValue() {
        return size;
    }

    public boolean hasSize() {
        return (__bitField0 & 16) == 16;
    }

    public Image withSize(serializers.protostuff.media.Image.Size value) {
        return Image.newBuilder()
            .mergeFrom(this)
            .setSize(value)
            .build();
    }



    @Override
    public io.protostuff.Schema<Image> cachedSchema() {
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
        Image that = (Image) obj;
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
        if (!java.util.Objects.equals(this.size, that.size)) {
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
        result = 31 * result + Integer.hashCode(this.size);

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
        if (hasSize()) {
            parts.add("size=" + getSize() + '(' + getSizeValue() + ')');
        }
        return "Image{" + String.join(", ", parts) + "}";
    }

    public static final class Schema implements io.protostuff.Schema<Image>{

        private static final Schema INSTANCE = new Schema();

        private static final java.util.Map<String,Integer> __fieldMap = new java.util.HashMap<>();

        static {
        	__fieldMap.put("uri", 1);
        	__fieldMap.put("title", 2);
        	__fieldMap.put("width", 3);
        	__fieldMap.put("height", 4);
        	__fieldMap.put("size", 5);
        }

        @Override
        public Image newMessage() {
            return new Image();
        }

        @Override
        public Class<Image> typeClass() {
            return Image.class;
        }

        @Override
        public String messageName() {
            return Image.class.getSimpleName();
        }

        @Override
        public String messageFullName() {
            return Image.class.getName();
        }

        @Override
        @Deprecated
        public boolean isInitialized(Image message) {
            return true;
        }

        @Override
        public void mergeFrom(io.protostuff.Input input, Image instance) throws java.io.IOException {
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
                    	instance.size = input.readEnum();
                    	instance.__bitField0 |= 16;
                    	break;
                    default:
                        input.handleUnknownField(number, this);
                }
        	}
        }

        @Override
        public void writeTo(io.protostuff.Output output, Image instance) throws java.io.IOException {
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
            	output.writeEnum(5, instance.size, false);
            }

        }

        @Override
        public String getFieldName(int number) {
        	switch(number) {
        		case 1: return "uri";
        		case 2: return "title";
        		case 3: return "width";
        		case 4: return "height";
        		case 5: return "size";
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

        private int size;

        private int __bitField0;

        private Builder() {
            this.uri = "";
            this.title = "";
            this.width = 0;
            this.height = 0;
            this.size = serializers.protostuff.media.Image.Size.SMALL.getNumber();
        }

        public Builder mergeFrom(Image instance) {
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

            if (instance.hasSize()) {
                this.setSize(instance.getSize());
            }

            return this;
        }

        public String getUri() {
            return uri;
        }

        public Builder setUri(String value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Image#uri to null");
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
                throw new NullPointerException("Cannot set Image#title to null");
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

        public serializers.protostuff.media.Image.Size getSize() {
            return serializers.protostuff.media.Image.Size.valueOf(size);
        }

        public int getSizeValue() {
            return size;
        }

        public Builder setSize(serializers.protostuff.media.Image.Size value) {
            if (value == null) {
                throw new NullPointerException("Cannot set Image#size to null");
            }

            if (value == serializers.protostuff.media.Image.Size.UNRECOGNIZED) {
                throw new IllegalArgumentException("Cannot set Image#size to UNRECOGNIZED");
            }

            this.size = value.getNumber();
            __bitField0 |= 16;
            return this;
        }

        public Builder setSizeValue(int value) {
            this.size = value;
            __bitField0 |= 16;
            return this;
        }

        public Builder clearSize() {
            this.size = 0;
            __bitField0 &= ~16;
            return this;
        }

        public boolean hasSize() {
            return (__bitField0 & 16) == 16;
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
            if (!java.util.Objects.equals(this.size, that.size)) {
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
            result = 31 * result + Integer.hashCode(this.size);

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
            if (hasSize()) {
                parts.add("size=" + getSize() + '(' + getSizeValue() + ')');
            }
            return "Image{" + String.join(", ", parts) + "}";
        }

        public Image build() {
            serializers.protostuff.media.Image result = new serializers.protostuff.media.Image();
            result.__bitField0 = __bitField0;
            result.uri = this.uri;
            result.title = this.title;
            result.width = this.width;
            result.height = this.height;
            result.size = this.size;
            return result;
        }

    }


    public enum Size implements io.protostuff.EnumLite<Size>{

        SMALL(0),
        LARGE(1),
        UNRECOGNIZED(-1);

        private final int value;

        private Size(int n) {
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

        public static final Size valueOf(int tag) {
            switch(tag) {
                case 0: return SMALL;
                case 1: return LARGE;
                default: return UNRECOGNIZED;
            }
        }
    }

}