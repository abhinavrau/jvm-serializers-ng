

# Project Outcome

* Command line application that can compare open source serialization
libraries:
    * Specify their own schema or POJO
    * compare:
        * size
        * speed
        * memory used
        * OS and JVM version
    * Build a docker image to run the test
    * Run on cloud server/provider to earn some $$$


# MVP

- List all serializers and their features
- Run benchmarks on serializers
    - By project name(s)
    - And/OR By one or more features
        - Formats - (XML, JSON, Binary)
        - Mode -( Code first or Schema first)
        - Domain Model  - (None, Plain POJO, POJO with Annotation,
                            Builder Pattern)
        - API Style - Reflection, Type registration, Code generation,
                      Property based
        - Manual Optimization (Yes/No)
        - Backward/Forward compatibility (yes/no)
        - Cyclic reference support (yes, no, unknown)
        - Version mismatch detection (yes/no)
        - Other Language Support (Yes, No)
        - Project URL
        - Maven, Gradle dependency
        - API usage snippet (GitHub Gist??)


- User supplies feature set they are interested in:
    - Schema first (top binary):
        - For binary - proto, avro, flatbuffer, capnproto
        - For text based - XSD
    - Code first
        - POJO

Sample usage:

* Features for given serializer

    - Formats - One or more - (XML, JSON, Binary)
    - Domain Model style - only one - Defines how the domain model
        is defined whether Code first or Schema first

    - API type (POJO, Builder Pattern, Manual code)
    - Backward/Forward compatibility (yes/no)
    - Cyclic reference support (yes, no unknown)


* CLI Commands
    - info : info by serializer
        - text
        - binary
        - project name
        - Mode (code-first/schema-first)
        - Value Type (POJO, Builder etc.)
        - API Method ( Reflection, generated code etc.)
        - by Feature(s)

* Number of serializers I got from some grepping
  * Numer of serializers 74
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

* Fix all broken serializers that fail unit tests (See all TODOs in code)

## GTD List

### Done

- Gradle support
- Unit tests
- Use ReflectionAssert and support for different class types
- Organize by type (schemaless vs. schema), language support, format of
   framework
- Use CLI framework to run code


### Next Actions
- List all serializers by features
- Design CLI commands
- Prints out feature set of all serilizers or a particular serializer


### Inbox
- Use specific version - Need to put all version numbers in one file or use gradle??
- Use latest version of libraries - Use gradle's maven version's support always

- Use JMH for performance measurement
- Learn from https://github.com/fabienrenaud/java-json-benchmark
- Better visualization of results




### SomeDay/Maybe
- Specify own schema with random data (Protobuf, Avro , XML and JSON first)
    - Why do we need an intermediate pojo?
    - Use Mapping framework to auto map to intermediate pojo?
    - Put data.media files to test profile
* How to run it using web page and see results in real time?

* flatbuffers build only works on osx and linux. Fix it for windows by using
 .zip file in pom.xml
* Integrate JSON benchmark
https://github.com/fabienrenaud/java-json-benchmark project somehow.
* Optimize Gradle build to not build other maven based projects when no changes in them.





