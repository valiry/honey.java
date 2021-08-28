# honey

Java library for reading and writing honey data

## Usage

```java
class MyClass {

    public void read() {
        HoneyWorld world;

        InputStream in = null; // This is not how you should 'create' an InputStream
        world = HoneyReaders.detectAndRead(in); // Throws IOException

        byte[] bytes = new byte[0];
        world = HoneyReaders.detectAndRead(bytes);

        // See also:
        // HoneyReaders.get(short version);
    }

    public void write(HoneyWorld world) {
        HoneyWriter writer = HoneyWriters.get(0x0001);
        byte[] data = writer.write(world); // Throws IOException

        // See also:
        // HoneyWriters.writeWithLatest(HoneyWorld world);
    }

}
```