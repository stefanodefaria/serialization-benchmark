Simple benchmark project to test serialization time and output size using some simple template classes (check code for details).
The tools used in this test must not require custom serialization logic, and must support polymorphism out of the box. 

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
kryo | 695,236 ± 30,189  ops/ms
jackson | 682,157 ± 37,413  ops/ms
smile |655,561 ±  8,850  ops/ms
msgPack | 508,310 ± 63,484  ops/ms
java | 89,435 ±  1,844  ops/ms

### Serialized output size:
Serialization tool | Output size
--- | ---
msgpack | 81 bytes
smile | 93 bytes
kryo | 105 bytes
jackson | 113 bytes
java | 323 bytes
