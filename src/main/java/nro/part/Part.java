package nro.part;

public class Part {
    public Part(byte type) {
        this.type = type;
        if (type == (byte)0) {
            this.pi = new PartImage[3];
        }
        if (type == (byte)1) {
            this.pi = new PartImage[17];
        }
        if (type == (byte)2) {
            this.pi = new PartImage[14];
        }
        if (type == (byte)3) {
            this.pi = new PartImage[2];
        }
    }

    public byte type;
    public PartImage[] pi;
}
