# Benchmark of Java Serialization Libraries

Command line tool that can compare open source serialization libraries running on the Java Virtual Machine.

## Purpose

This project benchmarks the throughput performance of a variety of Java
serialization libraries using JMH. The main driver is to compare binary
format serializers although the tool does support JSON and XML serializers
for libraries that support multiple output formats and for completeness.
It covers the following libraries:

* avro
* capnproto
* cbor
* colfer
* datakernel
* dsl-platform
* exi
* externalizor
* fastjson
* flatbuffers
* flexjson
* fst
* glassfish
* gson
* hessian
* jackson
* jacksonJr
* javolution
* jaxb
* jaxb-aalto
* jboss-serialization
* json-lib
* json-simple
* json-smart
* kryo
* mongodb
* msgpack
* obser
* protobuf
* protostuff
* scala
* scala-sbinary
* smile
* stephenerialization
* svenson
* thrift
* wobly
* xstream
* xstream-aalto
* xstream-fastinfo
* xstream-woodstox


For a JSON serializer only benchmark that is more thorough take a look at https://github.com/fabienrenaud/java-json-benchmark


## Usage

To see available subcommands

To output meta data about supported serializers
```
jmser --help
```


### Info

To output meta data about supported serializers
```
jmser info --help
```

### Benchmark

To output meta data about supported serializers
```
jmser bench --help
```


### Prerequisites

* JDK 8+
* Maven (Although Gradle is used for the main build but some libraries need maven)


### Installing

* Clone this repo to your local machine
* Build steps

```
cd cli
./gradlew clean shadowJar
```


## Built With

* [Gradle](https://gradle.org)
* [Picocli](https://picocli.info/)

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning.


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Inspired by [jvm-serializers](https://github.com/eishay/jvm-serializers)
* Additional inspiration [java-json-benchmark](https://github.com/fabienrenaud/java-json-benchmark)

