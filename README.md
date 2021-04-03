Simple benchmark project to test serialization time and size using some simple template classes (check code for details).
The tools used in this test must not require custom serialization logic, and must support polymorphism out of the box. 

## Tools tested:
	- Jackson JSON vanilla (jackson)
	- Jackson JSON with msgpack (msgpack)
	- Jackson Smile (smile)
    - Kryo (kryo)
	- Java default serialization (java)

## Initial Results:
Run the unit tests to get the results. They currently run one million iterations of serialization and
serialization of two different objects that share a same parent type.

### Processing time
    - kryio: 1375ms
    - jackson: 1593ms
    - smile: 1734ms
    - msgpack: 3749ms
    - java: 10531ms

### Serialized output size:
    - msgpack: 81 bytes
    - smile: 93 bytes
    - kyio: 105 bytes
    - jackson: 113 bytes
    - java: 323 bytes
