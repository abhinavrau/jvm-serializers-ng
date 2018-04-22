

# TODO

List of things to TODO for this project

## InBox


## Project Mission Statement

* Help users compare open source serialization libraries based on the following:

  * Serialization Format
    * Text based (XML - 12, JSON - 31)
    * Binary (Avro, ProtoBuf) - 32
    *  Binary Multi Langauge support - 15
  *  Zero knowledge -35 vs. POJOs with CodeGen - 19 vs. Manual Serialization - 36
  *  Object Graph Cyclic references support - 15 vs. Flat Tree - 70
  *  Backward/Forward compatibility





* Integrate asserts and reflection based comparison of objects when
testing correctness

* colfer fails correctness tests since transformation code needed to be
adjusted for the way it handles null stirngs.Figure out a way a way to
handle that. Maybe using reflection based comparison would help.


### MVP

1. Move to Gradle - Done
2. Use latest version of libraries - Done
3. JUnit tests for verification - Done
4. Use ReflectionAssert
4. Use JMH for performance measurement
  * Learn from https://github.com/fabienrenaud/java-json-benchmark
5. Use CLI framework to run code
6. Better visualization of results

* flatbuffers only works on osx and linux. Fix it for windows by using
 .zip file in pom.xml

## SomeDay/Maybe
* How to run it using web page and see results in real time?

* Specify own schema

* Organize by type (schemaless vs. schema), language support, format of
framework

* Integrate JSON benchmark
https://github.com/fabienrenaud/java-json-benchmark project somehow.





