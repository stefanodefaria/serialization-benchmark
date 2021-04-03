# Serialization benchmark test

Simple benchmark project to test serialization throughput and output size using
some simple template classes (check code for details).
The tools used in this test must not require implementation of serialization
logic and must support class-inheritance/polymorphism out of the box.

## Tools tested:
- Jackson JSON vanilla (jackson)
- Jackson JSON with msgpack (msgpack)
- Jackson Smile (smile)
- Kryo (kryo)
- Java native serialization (java)

## Benchmark Results
The following values were collected after running the unit tests on my local machine.
The benchmarked operation runs the serialization and deserialization of two different
objects that share a same parent type.

### Serialization  throughput:
Serialization tool | Average operation throughput (higher is better)
--- | ---
jackson | 704 ± 7  ops/ms
kryo | 690 ± 2  ops/ms
smile |669 ±  9  ops/ms
msgpack | 535 ± 8  ops/ms
java | 92 ±  1  ops/ms

### Serialization output size:
Serialization tool | Output size (lower is better)
--- | ---
msgpack | 81 bytes
smile | 93 bytes
kryo | 105 bytes
jackson | 113 bytes
java | 323 bytes
