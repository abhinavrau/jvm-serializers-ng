

## Project Outcome

* Command line application that can compare open source serialization
libraries:
  * Compare one or more libraries with each other based on their own needs
    * Specify their own schema or POJO
    * optimize based on
        * size
        * speed
        * memory used
        * Customize OS and JVM version
    * Build a docker image to run the test
    * Run on cloud server to some $$$


## Process

- User supplies feature set they are interested in:
    - Schema first:
        - For binary - proto, avro, flatbuffer, capnproto
        - For text based - XML Schema
    - Code first
        - POJO

* Serialization Feature set for given serializer ( Need to document matrix)
    - Binary
    - XML
    - JSON
    - Schema support
        - Schema Type (XML, JSON, Proto, Colfer... etc.)
        - Generated Serialization Code
        - Field Mapping
            - Can use mapping framework vs. Manual
    - Code First support (POJO)
    - Cyclic reference support (no support, supports, unknown)
    - Backward and Forward Compatibility
    - Cross Langauage support (Supports anything other than JVM)




- Dev effort
- Zero knowledge


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

## MVP

### Done
- Gradle support
- Unit tests
- Use ReflectionAssert and support for different class types

### Next Actions
- Document feature set of Serializers
- Prints out feature set of all serilizers or a particular serializer
- Specify own schema with random data (Protobuf, Avro , XML and JSON first)
    - Why do we need an intermediate pojo?
    - Use Mapping framework to auto map to intermediate pojo?
    - Put data.media files to test profile



### Inbox
- Use specific version - Need to put all version numbers in one file or use gradle??
- Use latest version of libraries - Use gradle's maven version's support always
- Organize by type (schemaless vs. schema), language support, format of
   framework
- Use JMH for performance measurement
- Learn from https://github.com/fabienrenaud/java-json-benchmark
- Use CLI framework to run code
- Better visualization of results



### SomeDay/Maybe
* How to run it using web page and see results in real time?



* flatbuffers only works on osx and linux. Fix it for windows by using
 .zip file in pom.xml


* Integrate JSON benchmark
https://github.com/fabienrenaud/java-json-benchmark project somehow.





