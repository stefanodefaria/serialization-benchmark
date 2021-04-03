Simple benchmark project to test serialization time and size using some simple template classes (check code for details).
The tools used in this test must not require custom serialization logic, and must support polymorphism out of the box. 

## Tools tested:
	- Jackson JSON vanilla (jackson)
	- Jackson JSON with msgpack (msgpack)
	- Jackson Smile (smile)
    - Kryo (kryo)
	- Java default serialization (java)

## Initial Results:
Run the unit tests to get the results.
The operation benchmarked is the serialization and serialization of
two different objects that share a same parent type.

### Processing time
Serialization tool | Average time per operation
--- | ---
jackson | 1.495 ±  0.164 us/op
kyro | 1.554 ± 1.013 us/op
smile | 1.550 ± 0.327 us/op
msgPack | 3.794 ± 2.169 us/op
java | 11.436 ± 12.455 us/op

### Serialized output size:
Serialization tool | Output size
--- | ---
msgpack | 97 bytes
smile | 111 bytes
kyio | 123 bytes
jackson | 129 bytes
java | 341 bytes
