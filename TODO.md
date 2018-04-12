

# TODO

List of things to TODO for this project

## InBox

### Build related

* Done - Figure out why debugging JUnit test cases does not work

* Done - dslplatform requires Mono framework to generate code. Detect
Mono framework to enbale code generation

* Done - dslplatform only needs this - Generated code that needs to checked in should
not be in the target or build directories

* Standardize generated-code directory paths

* Have a simple way to enable/disable projects not needed

* flatbuffers only works on osx and linux. Fix it for windows by using
 .zip file in pom.xml

* Integrate asserts and reflection based comparison of objects when
testing correctness

* colfer fails correctness tests since transformation code needed to be
adjusted for the way it handles null stirngs.Figure out a way a way to
handle that. Maybe using reflection based comparison would help.


### Enhancements

* Integrate JMH framework
    ** Learn from https://github.com/fabienrenaud/java-json-benchmark

* Better visualization of results

* How to run it using web page and see results in real time?

* Specify own schema

* Organize by type (schemaless vs. schema), language support, format of
framework


## SomeDay/Maybe

* Integrate JSON benchmark
https://github.com/fabienrenaud/java-json-benchmark project somehow.



