# Benchmark of Java Serialization Libraries

Command line tool that can compare open source serialization libraries running on the Java Virtual Machine.

## Purpose

This project benchmarks the throughput performance of a variety of Java
serialization libraries using JMH. The main driver is to compare binary
format serializers although the tool does support JSON and XML serializers
for libraries that support multiple output formats and for completeness.
It covers the following libraries:

```
┌──┬───────────────────┬──────────┬──────────────┬───────────────┬────────────┬───────────────┬──────────────┬──────────────────────────────┐
│# │Name               │Format    │ApiStyle      │Value Type     │Mode        │Features       │Optimization  │URL                           │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│1 │avro               │binary    │build-codegen │pojo           │schema-first│cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │              │/master/avro                  │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│2 │avro               │binary    │build-codegen │pojo           │schema-first│embedded-schema│              │https://avro.apache.org/      │
│  │                   │          │              │               │            │cross-lang     │              │                              │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│3 │avro               │binary    │field-by-field│no-domain-model│schema-first│embedded-schema│              │https://avro.apache.org/      │
│  │                   │          │              │               │            │cross-lang     │              │                              │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│4 │capnproto          │binary    │build-codegen │builder        │schema-first│cross-lang     │              │https://capnproto.org/        │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│5 │cbor               │binary    │field-by-field│no-domain-model│code-first  │cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │              │/master/cbor                  │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│6 │cbor               │binary    │rt-reflection │pojo           │code-first  │cross-lang     │uses  bytecode│https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │generation  to│ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │reduce        │/master/cbor                  │
│  │                   │          │              │               │            │optimized      │overhead      │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│7 │cbor               │binary    │rt-reflection │pojo           │code-first  │cross-lang     │uses          │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │positional    │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │(column)      │/master/cbor                  │
│  │                   │          │              │               │            │optimized      │layout      to│                              │
│  │                   │          │              │               │            │               │eliminate  use│                              │
│  │                   │          │              │               │            │               │of names      │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│8 │cbor               │binary    │rt-reflection │pojo           │code-first  │cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │              │/master/cbor                  │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│9 │colfer             │binary    │build-codegen │pojo           │schema-first│cross-lang     │              │https://github.com/pascaldeklo│
│  │                   │          │              │               │            │back-compat    │              │e/colfer                      │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│10│datakernel         │binary    │rt-bytecode   │pojo-with-@    │code-first  │back-compat    │              │http://datakernel.io/docs/modu│
│  │                   │          │              │               │            │               │              │les/serializer.html           │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│11│exi                │XML       │field-by-field│no-domain-model│code-first  │               │              │https://github.com/EXIficient/│
│  │                   │          │              │               │            │               │              │exificient                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│12│externalizor       │binary    │rt-reflection │pojo           │code-first  │               │              │https://github.com/qwazr/exter│
│  │                   │          │              │               │            │               │              │nalizor                       │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│13│externalizor       │binary    │rt-reflection │pojo           │code-first  │optimized      │              │https://github.com/qwazr/exter│
│  │                   │          │              │               │            │               │              │nalizor                       │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│14│fastjson           │json      │rt-reflection │pojo           │code-first  │               │              │https://github.com/alibaba/fas│
│  │                   │          │              │               │            │               │              │tjson                         │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│15│fastjson           │json      │rt-reflection │pojo           │code-first  │optimized      │array         │https://github.com/alibaba/fas│
│  │                   │          │              │               │            │               │              │tjson                         │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│16│flatbuffers        │binary    │build-codegen │custom-type    │schema-first│cross-lang     │              │https://google.github.io/flatb│
│  │                   │          │              │               │            │back-compat    │              │uffers/                       │
│  │                   │          │              │               │            │json-converter │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│17│flexjson           │json      │rt-reflection │pojo           │code-first  │cyclic-ref     │              │http://flexjson.sourceforge.ne│
│  │                   │          │              │               │            │               │              │t                             │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│18│fst                │binary    │rt-reflection │pojo           │code-first  │json-converter │              │https://github.com/RuedigerMoe│
│  │                   │          │              │               │            │optimized      │              │ller/fast-serialization       │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│19│fst                │binary    │rt-reflection │pojo           │code-first  │json-converter │              │https://github.com/RuedigerMoe│
│  │                   │          │              │               │            │               │              │ller/fast-serialization       │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│20│fst                │binary-jdk│rt-reflection │pojo           │code-first  │json-converter │              │https://github.com/RuedigerMoe│
│  │                   │          │              │               │            │cyclic-ref     │              │ller/fast-serialization       │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│21│glassfish          │json      │field-by-field│no-domain-model│code-first  │               │              │https://github.com/javaee/json│
│  │                   │          │              │               │            │               │              │p/tree/master/impl            │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│22│gson               │json      │field-by-field│no-domain-model│code-first  │               │              │https://github.com/google/gson│
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│23│gson               │json      │rt-reflection │pojo           │code-first  │               │              │https://github.com/google/gson│
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│24│hessian            │binary    │rt-reflection │pojo           │code-first  │cross-lang     │              │http://hessian.caucho.com/    │
│  │                   │          │              │               │            │cyclic-ref     │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│25│jackson            │XML       │rt-reflection │pojo           │code-first  │json-converter │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │              │ackson-dataformat-xml         │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│26│jackson            │json      │field-by-field│no-domain-model│code-first  │               │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │              │ackson-databind               │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│27│jackson            │json      │rt-reflection │pojo           │code-first  │               │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │              │ackson-databind               │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│28│jackson            │json      │rt-reflection │pojo           │code-first  │optimized      │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │              │ackson-databind               │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│29│jackson            │json      │rt-reflection │pojo           │code-first  │optimized      │uses  bytecode│https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │generation  to│ackson-databind               │
│  │                   │          │              │               │            │               │reduce        │                              │
│  │                   │          │              │               │            │               │overhead      │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│30│jackson            │json      │rt-reflection │pojo           │code-first  │optimized      │uses          │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │positional    │ackson-databind               │
│  │                   │          │              │               │            │               │(column)      │                              │
│  │                   │          │              │               │            │               │layout      to│                              │
│  │                   │          │              │               │            │               │eliminate  use│                              │
│  │                   │          │              │               │            │               │of names      │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│31│jacksonJr          │json      │rt-reflection │pojo           │code-first  │               │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │               │              │ackson-jr                     │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│32│javolution         │XML       │field-by-field│no-domain-model│code-first  │               │              │https://github.com/javolution/│
│  │                   │          │              │               │            │               │              │javolution                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│33│jaxb               │XML       │rt-reflection │pojo           │code-first  │cyclic-ref     │              │https://github.com/javaee/jaxb│
│  │                   │          │              │               │            │               │              │-v2                           │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│34│jaxb-aalto         │XML       │rt-reflection │pojo           │code-first  │cyclic-ref     │              │https://github.com/FasterXML/a│
│  │                   │          │              │               │            │               │              │alto-xml                      │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│35│jboss-serialization│binary-jdk│rt-reflection │pojo           │code-first  │               │              │http://serialization.jboss.org│
│  │                   │          │              │               │            │               │              │/                             │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│36│json-lib           │json      │rt-reflection │pojo           │code-first  │               │              │http://json-lib.sourceforge.ne│
│  │                   │          │              │               │            │               │              │t                             │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│37│json-simple        │json      │field-by-field│no-domain-model│code-first  │               │              │http://code.google.com/p/json-│
│  │                   │          │              │               │            │               │              │simple                        │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│38│json-simple        │json      │field-by-field│no-domain-model│code-first  │optimized      │use         of│http://code.google.com/p/json-│
│  │                   │          │              │               │            │               │ContentHandler│simple                        │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│39│json-smart         │json      │field-by-field│no-domain-model│code-first  │               │              │https://code.google.com/archiv│
│  │                   │          │              │               │            │               │              │e/p/json-smart/               │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│40│kryo               │binary    │field-by-field│pojo           │code-first  │               │              │https://github.com/EsotericSof│
│  │                   │          │              │               │            │               │              │tware/kryo                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│41│kryo               │binary    │rt-reflection │pojo           │code-first  │               │              │https://github.com/EsotericSof│
│  │                   │          │              │               │            │               │              │tware/kryo                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│42│kryo               │binary    │rt-reflection │pojo           │code-first  │cyclic-ref     │              │https://github.com/EsotericSof│
│  │                   │          │              │               │            │               │              │tware/kryo                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│43│kryo               │binary    │rt-reflection │pojo           │code-first  │optimized      │nullable      │https://github.com/EsotericSof│
│  │                   │          │              │               │            │               │fields        │tware/kryo                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│44│kryo               │binary    │rt-reflection │pojo           │code-first  │user-typeids   │              │https://github.com/EsotericSof│
│  │                   │          │              │               │            │               │              │tware/kryo                    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│45│mongodb            │binary    │field-by-field│pojo           │code-first  │cross-lang     │              │http://bsonspec.org/          │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│46│mongodb            │binary    │rt-reflection │pojo           │code-first  │cross-lang     │              │http://bsonspec.org/          │
│  │                   │          │              │               │            │cyclic-ref     │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│47│msgpack            │binary    │field-by-field│no-domain-model│code-first  │cross-lang     │              │https://github.com/msgpack/msg│
│  │                   │          │              │               │            │json-converter │              │pack-java                     │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│48│msgpack            │binary    │rt-reflection │pojo           │code-first  │cross-lang     │              │https://github.com/msgpack/msg│
│  │                   │          │              │               │            │json-converter │              │pack-java/tree/develop/msgpack│
│  │                   │          │              │               │            │               │              │-jackson                      │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│49│protobuf           │binary    │build-codegen │builder        │schema-first│cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │              │/master/protobuf              │
│  │                   │          │              │               │            │optimized      │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│50│protobuf           │binary    │build-codegen │builder        │schema-first│cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │cyclic-ref     │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │json-converter │              │/master/protobuf              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│51│protobuf           │binary    │build-codegen │builder        │schema-first│json-converter │              │https://github.com/google/prot│
│  │                   │          │              │               │            │cross-lang     │              │obuf/tree/master/java         │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│52│protobuf           │binary    │build-codegen │pojo           │schema-first│json-converter │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │              │ocs/                          │
│  │                   │          │              │               │            │back-compat    │              │                              │
│  │                   │          │              │               │            │cross-lang     │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│53│protobuf           │binary    │rt-reflection │pojo           │schema-first│json-converter │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │              │ocs/                          │
│  │                   │          │              │               │            │back-compat    │              │                              │
│  │                   │          │              │               │            │cross-lang     │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│54│protobuf           │json      │build-codegen │builder        │schema-first│json-converter │              │https://github.com/google/prot│
│  │                   │          │              │               │            │cross-lang     │              │obuf/tree/master/java         │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│55│protostuff         │XML       │field-by-field│no-domain-model│schema-first│xml-converter  │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │               │              │ocs/                          │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│56│protostuff         │XML       │rt-reflection │pojo           │schema-first│xml-converter  │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │               │              │ocs/                          │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│57│protostuff         │binary    │build-codegen │pojo           │schema-first│json-converter │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │              │ocs/                          │
│  │                   │          │              │               │            │back-compat    │              │                              │
│  │                   │          │              │               │            │cyclic-ref     │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│58│protostuff         │binary    │build-codegen │pojo           │schema-first│json-converter │hand-coded    │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │schema  +   no│ocs/                          │
│  │                   │          │              │               │            │back-compat    │autoboxing    │                              │
│  │                   │          │              │               │            │optimized      │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│59│protostuff         │binary    │build-codegen │pojo           │schema-first│json-converter │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │              │ocs/                          │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│60│protostuff         │binary    │rt-reflection │pojo           │code-first  │json-converter │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │              │ocs/                          │
│  │                   │          │              │               │            │back-compat    │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│61│protostuff         │binary    │rt-reflection │pojo           │schema-first│json-converter │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │xml-converter  │              │ocs/                          │
│  │                   │          │              │               │            │back-compat    │              │                              │
│  │                   │          │              │               │            │cyclic-ref     │              │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│62│protostuff         │json      │field-by-field│no-domain-model│schema-first│xml-converter  │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │               │              │ocs/                          │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│63│protostuff         │json      │rt-reflection │pojo           │schema-first│xml-converter  │              │https://protostuff.github.io/d│
│  │                   │          │              │               │            │               │              │ocs/                          │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│64│smile              │binary    │build-codegen │pojo           │code-first  │json-converter │              │https://github.com/FasterXML/s│
│  │                   │          │              │               │            │               │              │mile-format-specification     │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│65│smile              │binary    │field-by-field│no-domain-model│code-first  │cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │json-converter │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │               │              │/master/smile                 │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│66│smile              │binary    │field-by-field│pojo           │code-first  │json-converter │              │https://github.com/FasterXML/s│
│  │                   │          │              │               │            │               │              │mile-format-specification     │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│67│smile              │binary    │rt-reflection │pojo           │code-first  │cross-lang     │uses  bytecode│https://github.com/FasterXML/j│
│  │                   │          │              │               │            │json-converter │generation  to│ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │optimized      │reduce        │/master/smile                 │
│  │                   │          │              │               │            │               │overhead      │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│68│smile              │binary    │rt-reflection │pojo           │code-first  │cross-lang     │uses          │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │json-converter │positional    │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │optimized      │(column)      │/master/smile                 │
│  │                   │          │              │               │            │               │layout      to│                              │
│  │                   │          │              │               │            │               │eliminate  use│                              │
│  │                   │          │              │               │            │               │of names      │                              │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│69│smile              │binary    │rt-reflection │pojo           │code-first  │cross-lang     │              │https://github.com/FasterXML/j│
│  │                   │          │              │               │            │json-converter │              │ackson-dataformats-binary/tree│
│  │                   │          │              │               │            │               │              │/master/smile                 │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│70│smile              │binary    │rt-reflection │pojo           │code-first  │json-converter │              │https://github.com/FasterXML/s│
│  │                   │          │              │               │            │               │              │mile-format-specification     │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│71│svenson            │json      │rt-reflection │pojo           │code-first  │               │              │https://github.com/fforw/svens│
│  │                   │          │              │               │            │               │              │on                            │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│72│thrift             │binary    │build-codegen │pojo           │schema-first│               │              │https://thrift.apache.org/    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│73│thrift             │binary    │build-codegen │pojo           │schema-first│optimized      │compact       │https://thrift.apache.org/    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│74│xstream            │XML       │field-by-field│no-domain-model│code-first  │json-converter │              │http://x-stream.github.io/    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│75│xstream-aalto      │XML       │field-by-field│no-domain-model│code-first  │json-converter │              │http://x-stream.github.io/    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│76│xstream-fastinfo   │XML       │field-by-field│no-domain-model│code-first  │json-converter │              │http://x-stream.github.io/    │
├──┼───────────────────┼──────────┼──────────────┼───────────────┼────────────┼───────────────┼──────────────┼──────────────────────────────┤
│77│xstream-woodstox   │XML       │field-by-field│no-domain-model│code-first  │json-converter │              │http://x-stream.github.io/    │
└──┴───────────────────┴──────────┴──────────────┴───────────────┴────────────┴───────────────┴──────────────┴──────────────────────────────┘
```

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

* JDK 8+ or JDK 11
* Maven (Although Gradle is used for the main build using gradle wrapper but some libraries need maven)

### Build

* Clone this repo to your local machine
* Build steps

```
cd cli
./gradlew clean distZip
```
### Run the Cli

```
mkdir tmp
unzip build/distributions/cli-shadow-1.0-SNAPSHOT.zip -d tmp
./tmp/cli-shadow-1.0-SNAPSHOT/bin/cli --help
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

