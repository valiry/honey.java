# honey

Java library for reading honey data

## Usage

```java
class MyClass {

    public void read() {
        HoneyWorld world;

        InputStream in = null; // This is not how you should 'create' an InputStream
        world = HoneyReaders.read(in); // Throws IOException

        byte[] bytes = new byte[0];
        world = HoneyReaders.read(bytes);

        // See also:
        // HoneyReaders.get(short version);
    }

    public void write(HoneyWorld world) {
        HoneyWriter writer = new HoneyWriterV1();
        byte[] data = writer.write(world); // Throws IOException

        // See also:
        // HoneyWriters.get(short version);
        // HoneyWriters.writeWithLatest(HoneyWorld world);
    }

}
```