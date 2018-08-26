package serializers.core.metadata;


import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the features of a given Serializer
 */
@Builder(toBuilder = true)
public class SerializerProperties {

	/**
	 * Format of the serializer output
	 */
	public enum Format {
		XML("XML"), JSON("json"), BINARY("binary"), BINARY_JDK_COMPATIBLE("binary-jdk");

		private final String description;

		@Override
		public String toString() {
			return description;
		}

		Format(String desc)
		{
			this.description = desc;
		}
	}


	/**
	 * Serializer Mode whether it is <b>Code first</b> or <b>Schema First</b>
	 */
	public enum Mode {
		CODE_FIRST("code-first") , SCHEMA_FIRST("schema-first");

		private final String description;

		@Override
		public String toString() {
			return description;
		}

		Mode(String desc)
		{
			this.description = desc;
		}
	}

	/**
	 * The type of Value type the serializer supports that can be passed or used with
	 * a given serializer API
	 *
	 */
	public enum ValueType {
		/**
		 * No support for a domain model. Usually used with  {@link APIStyle#FIELD_BASED}
		 */
		NONE("no-domain-model") ,
		/**
		 * Supports Plain Old Java Objects (POJO)
		 */
		POJO("pojo"),
		/**
		 * Support POJO's with additional Annotations that generate byte code at runtime
		 */
		POJO_WITH_ANNOTATIONS("pojo-with-@"),
		/**
		 * Uses Builder Pattern to represent the domain model
		 */
		BUILDER_PATTERN("builder"),
		/**
		 * Custom value type
		 */
		CUSTOM_TYPE("custom-type");


		private final String description;

		@Override
		public String toString() {
			return description;
		}

		ValueType(String desc)
		{
			this.description = desc;
		}
	}

	/**
	 * The type of API that the serializer is using
	 */
	public enum APIStyle {
		/**
		 *  Uses Reflection to serialize the fields at runtime
		 */
		REFLECTION("rt-reflection") ,
		/**
		 * Uses Byte Code generation at runtime
		 * Usually common with annotations that to mark fields to be serialized
		 */
		RUNTIME_BYTECODE_GENERATION("rt-bytecode"),
		/**
		 * Code generation is used to generate the serialization code
		 */
		BUILD_TIME_CODE_GENERATION("build-codegen"),
		/**
		 * Serialization is done one field at a time. Usually used with {@link ValueType#NONE}
		 */
		FIELD_BASED("field-by-field");

		private final String description;

		@Override
		public String toString() {
			return description;
		}

		APIStyle(String desc)
		{
			this.description = desc;
		}
	}

	public enum Features
	{
		/**
		 * Optimized is some way to get better speed or smaller payload
		 */
		OPTIMIZED("optimized"),
		/**
		 * Uses Annotations to optimize the serialization/deserialization
		 */
		USER_MANAGED_TYPE_IDS("user-typeids"),
		/**
		 * Support backwards and forward compatibility as domain model changes
		 */
		SUPPORTS_BACKWARD_COMPATIBILITY("back-compat"),
		/**
		 * Supports other languages (not JVM based)
		 */
		SUPPORTS_ADDITIONAL_LANGUAGES("cross-lang"),
		/**
		 *Embeds the schema as part of the message to support versioning and conflict resolution
		 */
		EMBEDDED_SCHEMA("embedded-schema"),
		/**
		 * Optimized to support circular object references so payloads are smaller
		 */
		SUPPORTS_CYCLIC_REFERENCES("cyclic-ref"),
		/**
		 * Does not need to be parsed or unpacked before accessing fields
		 */
		NO_UNPACKING("no-unpack"),
		/**
		 * API has a built-in XML converter (applies only to {@link Format#BINARY} serializers)
		 */
		XML_CONVERTER("xml-converter"),
		/**
		 * API has a built-in JSON converter (applies only to {@link Format#BINARY} serializers)
		 */
		JSON_CONVERTER("json-converter");


		private final String description;
		private String metadata;

		@Override
		public String toString() {
			return metadata==null ? description: description.concat("-").concat(metadata);
		}

		Features(String desc)
		{
			this.description = desc;
		}

	}

	public enum TypeMapper
	{
		CUSTOM_CODED_MAPPER("mapper-custom"),
		MAPSTRUCT("mapper-mapstruct"),
		MODELMAPPER("mapper-modelmapper"),
		DOZER("mapper-dozer"),
		ORIKA("mapper-orika"),
		SELMA("mapper-selma");

		private final String description;

		@Override
		public String toString() {
			return description;
		}

		TypeMapper(String desc)
		{
			this.description = desc;
		}

	}
	@Getter
	private String name;
	@Getter
	private Format format;
	@Getter
	private Mode mode;
	@Getter
	private ValueType valueType;
	@Getter
	private APIStyle apiStyle;
	@Singular
	@Getter
	private Set<Features>       features;
	@Getter
	private String projectURL;
	@Getter
	@Builder.Default
	private TypeMapper typeMapper = TypeMapper.CUSTOM_CODED_MAPPER;
	@Getter
	@Builder.Default
	private String optimizedDescription = "";
	@Getter
	@Builder.Default
	private String relatedSerializer ="";
	private short locSerializer;
	private short locTransformer;

	public String getShortName()
	{
		Joiner joiner = Joiner.on("/").skipNulls();
		return joiner.join(name, format, apiStyle, valueType, mode, features, optimizedDescription);
	}

	public String getFormattedFeatures()
	{
		Joiner joiner = Joiner.on(" ").skipNulls();
		return joiner.join(features);
	}

	public String values()
	{
		HashSet<String> values = Sets.newHashSet(
				name,
				format.toString(),
				mode.toString(),
				valueType.toString(),
				apiStyle.toString(),
				features.toString(),
				projectURL,
				optimizedDescription,
				optimizedDescription);
		Joiner joiner = Joiner.on(" ").useForNull("N/A");
		return joiner.join(values);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("format", format)
				.add("mode", mode)
				.add("valueType", valueType)
				.add("apiStyle", apiStyle)
				.add("features", features)
				.add("projectURL", projectURL)
				.add("typeMapper", typeMapper)
				.add("optimizedDescription", optimizedDescription)
				.add("relatedSerializer", relatedSerializer)
				.add("locSerializer", locSerializer)
				.add("locTransformer", locTransformer)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SerializerProperties that = (SerializerProperties) o;
		return locSerializer == that.locSerializer &&
				locTransformer == that.locTransformer &&
				Objects.equals(name, that.name) &&
				format == that.format &&
				mode == that.mode &&
				valueType == that.valueType &&
				apiStyle == that.apiStyle &&
				Objects.equals(features, that.features) &&
				Objects.equals(projectURL, that.projectURL) &&
				typeMapper == that.typeMapper &&
				Objects.equals(optimizedDescription, that.optimizedDescription) &&
				Objects.equals(relatedSerializer, that.relatedSerializer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, format, mode, valueType, apiStyle, features, projectURL, typeMapper, optimizedDescription, relatedSerializer, locSerializer, locTransformer);
	}
}

