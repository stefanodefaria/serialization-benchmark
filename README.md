# Serialization benchmark test

Simple benchmark project to test serialization time and output size using
some simple template classes (check code for details).
The tools used in this test must not require implementation of serialization
logic and must support polymorphism out of the box.

## Tools tested:
- Jackson JSON vanilla (jackson)
- Jackson JSON with msgpack (msgpack)
- Jackson Smile (smile)
- Kryo (kryo)
- Java native serialization (java)

## Initial Results:
Run the unit tests to get the results.
The benchmarked operation runs the serialization and deserialization of two different objects that share a same parent type.

### Processing time
Serialization tool | Average operation throughput (higher is better)
--- | ---
jackson | 704 ± 7  ops/ms
kryo | 690 ± 2  ops/ms
smile |669 ±  9  ops/ms
msgPack | 535 ± 8  ops/ms
java | 92 ±  1  ops/ms

### Serialized output size:
Serialization tool | Output size (lower is better)
--- | ---
msgpack | 81 bytes
smile | 93 bytes
kryo | 105 bytes
jackson | 113 bytes
java | 323 bytes
