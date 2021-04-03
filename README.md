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
Serialization tool | Average time per operation
--- | ---
jackson | 1.458 ±  0.120 us/op
smile | 1.461 ± 0.072 us/op
kryo | 1.491 ± 0.113 us/op
msgPack | 3.615 ± 0.021 us/op
java | 10.931 ± 1.340 us/op

### Serialized output size:
Serialization tool | Output size
--- | ---
msgpack | 81 bytes
smile | 93 bytes
kryo | 105 bytes
jackson | 113 bytes
java | 323 bytes
