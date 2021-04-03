Simple benchmark project to test serialization time and size using some simple template classes.

## Tools tested:
	- Jackson JSON vanilla (jackson)
	- Jackson JSON with msgpack (msgpack)
	- Jackson Smile (smile)
	- Java default serialization (java)

## Initial Results:
Run the unit tests to get the results. They currently run one million iterations of serialization and serialization of two different objects.

### Processing time
	- smile time: 1646ms
	- java time: 10443ms
	- msgpack time: 3861ms
	- jackson time: 1556ms

### Serialized output size:
	- msgpack: 81 bytes
	- smile: 93 bytes
	- jackson: 113 bytes
	- java: 299 bytes
