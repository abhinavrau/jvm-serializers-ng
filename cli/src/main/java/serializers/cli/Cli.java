package serializers.cli;

import picocli.CommandLine;
import serializers.MediaContentTestGroup;
import serializers.Thrift;
import serializers.avro.AvroGeneric;
import serializers.avro.AvroSpecific;
import serializers.capnproto.CapNProto;
import serializers.colfer.Colfer;
import serializers.datakernel.DataKernelSerializer;
import serializers.dslplatform.DSLPlatform;
import serializers.externalizor.Externalizors;
import serializers.flatbuffers.FlatBuffers;
import serializers.fst.FastSerialization;
import serializers.hessian.Hessian;
import serializers.jackson.*;
import serializers.javaxjson.JavaxJsonStreamGlassfish;
import serializers.jboss.JBossSerialization;
import serializers.json.*;
import serializers.kryo.Kryo;
import serializers.mongodb.MongoDB;
import serializers.mongodb.MongoDBManual;
import serializers.msgpack.MsgPack;
import serializers.obser.Obser;
import serializers.protobuf.Protobuf;
import serializers.protobuf.ProtobufJson;
import serializers.protostuff.Protostuff;
import serializers.protostuff.ProtostuffJson;
import serializers.protostuff.ProtostuffSmile;
import serializers.protostuff.ProtostuffXml;
import serializers.scala.Scala;
import serializers.stephenerialization.Stephenerialization;
import serializers.wobly.Wobly;
import serializers.xml.ExiExificient;
import serializers.xml.Jaxb;
import serializers.xml.JaxbAalto;
import serializers.xml.XmlXStream;
import serializers.xml.javolution.XmlJavolution;

import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "jmser",
		sortOptions = false,
		headerHeading = "Usage:%n%",
		synopsisHeading = "%n",
		descriptionHeading = "%nDescription:%n%",
		parameterListHeading = "%nParameters:%n",
		optionListHeading = "%nOptions:%n",
		header = "JVM Micro-benchmarking for Serializers",
		mixinStandardHelpOptions = true,
		description = "Uses jhm to benchmark java serialization libraries. See Commands section on available operations",
		version = "0.1")
public class Cli implements Callable<Void> {

	@Override
	public Void call() {


		return null;

	}

	public MediaContentTestGroup registerAllSerializers() {
		MediaContentTestGroup groups = new MediaContentTestGroup();
		AvroGeneric.register(groups);
		AvroSpecific.register(groups);
		CapNProto.register(groups);
		Colfer.register(groups);
		DataKernelSerializer.register(groups);
		DSLPlatform.register(groups);
		Externalizors.register(groups);
		FlatBuffers.register(groups);
		FastSerialization.register(groups);
		Hessian.register(groups);

		//Jackson-Binary
		JacksonAvroDatabind.register(groups);
		JacksonCBORDatabind.register(groups);
		JacksonProtobufDatabind.register(groups);
		JacksonSmileManual.register(groups);
		JacksonSmileDatabind.register(groups);

		// JavaxJSON
		JacksonJrDatabind.register(groups);
		JacksonJsonDatabind.register(groups);
		JacksonJsonTree.register(groups);
		JacksonJsonManual.register(groups);

		// Jackson databind with Afterburner; add-on module that uses bytecode gen for speed
		JacksonWithAfterburner.registerAll(groups);
		// Jackson's column-oriented variants for formats that usually use key/value notation
		JacksonWithColumnsDatabind.registerAll(groups);

		// XML
		JacksonXmlDatabind.register(groups);

		JavaxJsonStreamGlassfish.register(groups);

		// TODO: Fix this - fails correctness check: JavaxJsonTreeGlassfish.register(groups);

		//TODO: Fix this - fails correctness check: JBossMarshalling.register(groups);
		JBossSerialization.register(groups);

		//JSON
		//TODO: Fix All commented lines below which fail correctness check with null on media.2.json
		FastJSONArrayDatabind.register(groups);
		FastJSONDatabind.register(groups);
		FlexjsonDatabind.register(groups);
		//JsonArgoTree.register(groups);
		//JsonDotOrgManualTree.register(groups);
		JsonGsonDatabind.register(groups);
		JsonGsonManual.register(groups);
		//JsonGsonTree.register(groups);
		//JsonijJpath.register(groups);
		//JsonijManualTree.register(groups);
		JsonLibJsonDatabind.register(groups);

		// JsonPathDeserializerOnly.register(groups);
		JsonSimpleManualTree.register(groups);
		JsonSimpleWithContentHandler.register(groups);
		JsonSmartManualTree.register(groups);
		JsonSvensonDatabind.register(groups);

		Kryo.register(groups);
		MongoDBManual.register(groups);
		MongoDB.register(groups);
		MsgPack.register(groups);
		Obser.register(groups);
		Protobuf.register(groups);
		ProtobufJson.register(groups);

		Protostuff.register(groups);
		ProtostuffJson.register(groups);
		ProtostuffSmile.register(groups);
		ProtostuffXml.register(groups);

		Scala.register(groups);
		Stephenerialization.register(groups);

		Thrift.register(groups);
		Wobly.register(groups);


		ExiExificient.register(groups);
		Jaxb.register(groups);
		JaxbAalto.register(groups);
		XmlJavolution.register(groups);

		XmlXStream.register(groups);

		//TODO: XmlStax does not like media.2.json
		//XmlStax.register(groups, true, true, true);

		return groups;
	}

	public static void main(String[] args) throws Exception {

		CommandLine cmd = new CommandLine(new Cli())
				.addSubcommand("info", new InfoCmd())
				.addSubcommand("bench", new BenchmarkCmd());
		List<Object> result = cmd.parseWithHandler(new CommandLine.RunAll(), args);
	}
}

