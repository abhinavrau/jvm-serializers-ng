package serializers.protostuff.media;

@javax.annotation.Generated("src/main/protostuff/media.protostuff.proto")
public final class MediaContent
        implements io.protostuff.Message<MediaContent> {

    private static final MediaContent DEFAULT_INSTANCE = newBuilder().build();

    private java.util.List<serializers.protostuff.media.Image> image;

    private serializers.protostuff.media.Media media;

    private boolean __merge_lock = false;
    private int __bitField0;

    private MediaContent() {
        this.image = java.util.Collections.emptyList();
        this.media = serializers.protostuff.media.Media.getDefaultInstance();
    }

    private MediaContent(Builder builder) {
        __merge_lock = true;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static MediaContent getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static io.protostuff.Schema<MediaContent> getSchema()
    {
    	return Schema.INSTANCE;
    }

    public java.util.List<serializers.protostuff.media.Image> getImageList() {
        return image;
    }

    public int getImageCount() {
        return image.size();
    }

    public serializers.protostuff.media.Image getImage(int index) {
        return image.get(index);
    }

    public MediaContent withImage(java.util.List<serializers.protostuff.media.Image> value) {
        return MediaContent.newBuilder()
            .mergeFrom(this)
            .clearImage()
            .addAllImage(value)
            .build();
    }

    public serializers.protostuff.media.Media getMedia() {
        return media;
    }

    public boolean hasMedia() {
        return (__bitField0 & 2) == 2;
    }

    public MediaContent withMedia(serializers.protostuff.media.Media value) {
        return MediaContent.newBuilder()
            .mergeFrom(this)
            .setMedia(value)
            .build();
    }



    @Override
    public io.protostuff.Schema<MediaContent> cachedSchema() {
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
        MediaContent that = (MediaContent) obj;
        if (!java.util.Objects.equals(this.image, that.image)) {
            return false;
        }
        if (!java.util.Objects.equals(this.media, that.media)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.image == null ? 0 : this.image.hashCode());
        result = 31 * result + (this.media == null ? 0 : this.media.hashCode());

        return result;
    }

    @Override
    public String toString() {
        java.util.List<String> parts = new java.util.ArrayList<>();
        if (!image.isEmpty()) {
            parts.add("image=" + getImageList());
        }
        if (hasMedia()) {
            parts.add("media=" + getMedia());
        }
        return "MediaContent{" + String.join(", ", parts) + "}";
    }

    public static final class Schema implements io.protostuff.Schema<MediaContent>{

        private static final Schema INSTANCE = new Schema();

        private static final java.util.Map<String,Integer> __fieldMap = new java.util.HashMap<>();

        static {
        	__fieldMap.put("image", 1);
        	__fieldMap.put("media", 2);
        }

        @Override
        public MediaContent newMessage() {
            return new MediaContent();
        }

        @Override
        public Class<MediaContent> typeClass() {
            return MediaContent.class;
        }

        @Override
        public String messageName() {
            return MediaContent.class.getSimpleName();
        }

        @Override
        public String messageFullName() {
            return MediaContent.class.getName();
        }

        @Override
        @Deprecated
        public boolean isInitialized(MediaContent message) {
            return true;
        }

        @Override
        public void mergeFrom(io.protostuff.Input input, MediaContent instance) throws java.io.IOException {
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
                    	if(!((instance.__bitField0 & 1) == 1)) {
                    	    instance.image = new java.util.ArrayList<>();
                    	    instance.__bitField0 |= 1;
                    	}
                    	instance.image.add(input.mergeObject(null, serializers.protostuff.media.Image.getSchema()));

                    	break;
                    case 2:
                    	instance.media = input.mergeObject(null, serializers.protostuff.media.Media.getSchema());
                    	instance.__bitField0 |= 2;
                    	break;
                    default:
                        input.handleUnknownField(number, this);
                }
        	}
            if((instance.__bitField0 & 1) == 1) {
                instance.image = java.util.Collections.unmodifiableList(instance.image);
            }


        }

        @Override
        public void writeTo(io.protostuff.Output output, MediaContent instance) throws java.io.IOException {
            for(serializers.protostuff.media.Image image : instance.image) {
                output.writeObject(1, image, serializers.protostuff.media.Image.getSchema(), true);
            }

            if((instance.__bitField0 & 2) == 2) {
            	output.writeObject(2, instance.media, serializers.protostuff.media.Media.getSchema(), false);
            }

        }

        @Override
        public String getFieldName(int number) {
        	switch(number) {
        		case 1: return "image";
        		case 2: return "media";
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

        private java.util.List<serializers.protostuff.media.Image> image;

        private serializers.protostuff.media.Media media;

        private int __bitField0;

        private Builder() {
            this.image = java.util.Collections.emptyList();
            this.media = serializers.protostuff.media.Media.getDefaultInstance();
        }

        public Builder mergeFrom(MediaContent instance) {
            this.addAllImage(instance.getImageList());

            if (instance.hasMedia()) {
                this.setMedia(instance.getMedia());
            }

            return this;
        }

        public java.util.List<serializers.protostuff.media.Image> getImageList() {
            return image;
        }

        public Builder setImage(int index, serializers.protostuff.media.Image value) {
            if (value == null) {
                throw new NullPointerException("Cannot set MediaContent#image to null");
            }

            if(!((__bitField0 & 1) == 1)) {
                this.image = new java.util.ArrayList<>();
                __bitField0 |= 1;
            }
            this.image.set(index, value);
            return this;
        }

        public Builder addImage(serializers.protostuff.media.Image value) {
            if (value == null) {
                throw new NullPointerException("Cannot set MediaContent#image to null");
            }

            if(!((__bitField0 & 1) == 1)) {
                this.image = new java.util.ArrayList<>();
                __bitField0 |= 1;
            }
            this.image.add(value);
            return this;
        }

        public Builder addAllImage(java.lang.Iterable<serializers.protostuff.media.Image> values) {
            if (values == null) {
                throw new NullPointerException("Cannot set MediaContent#image to null");
            }
            if(!((__bitField0 & 1) == 1)) {
                this.image = new java.util.ArrayList<>();
                __bitField0 |= 1;
            }
            for (final serializers.protostuff.media.Image value : values) {
                if (value == null) {
                   throw new NullPointerException("Cannot set MediaContent#image to null");
                }
                this.image.add(value);
            }
            return this;
        }

        public Builder clearImage() {
            this.image = java.util.Collections.emptyList();
            __bitField0 &= ~1;
            return this;
        }

        public int getImageCount() {
            return image.size();
        }

        public serializers.protostuff.media.Image getImage(int index) {
            return image.get(index);
        }

        public serializers.protostuff.media.Media getMedia() {
            return media;
        }

        public Builder setMedia(serializers.protostuff.media.Media value) {
            if (value == null) {
                throw new NullPointerException("Cannot set MediaContent#media to null");
            }

            this.media = value;
            __bitField0 |= 2;
            return this;
        }

        public Builder clearMedia() {
            this.media = serializers.protostuff.media.Media.getDefaultInstance();
            __bitField0 &= ~2;
            return this;
        }

        public boolean hasMedia() {
            return (__bitField0 & 2) == 2;
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
            if (!java.util.Objects.equals(this.image, that.image)) {
                return false;
            }
            if (!java.util.Objects.equals(this.media, that.media)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + (this.image == null ? 0 : this.image.hashCode());
            result = 31 * result + (this.media == null ? 0 : this.media.hashCode());

            return result;
        }

        @Override
        public String toString() {
            java.util.List<String> parts = new java.util.ArrayList<>();
            if (!image.isEmpty()) {
                parts.add("image=" + getImageList());
            }
            if (hasMedia()) {
                parts.add("media=" + getMedia());
            }
            return "MediaContent{" + String.join(", ", parts) + "}";
        }

        public MediaContent build() {
            serializers.protostuff.media.MediaContent result = new serializers.protostuff.media.MediaContent();
            result.__bitField0 = __bitField0;
            result.image = java.util.Collections.unmodifiableList(this.image);
            result.media = this.media;
            return result;
        }

    }



}