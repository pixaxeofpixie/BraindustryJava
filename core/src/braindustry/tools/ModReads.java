package braindustry.tools;

import arc.util.io.Reads;
import arc.util.io.ReusableByteInStream;
import arc.util.io.ReusableByteOutStream;

import java.io.DataInput;
import java.io.DataInputStream;

public class ModReads extends Reads {
    final ReusableByteInStream r = new ReusableByteInStream();

    public ModReads() {
        super(null);
        input=new DataInputStream(r);
    }
    public ModReads(byte[] bytes) {
        this();
        setBytes(bytes);
    }
    public void setBytes(byte[] bytes){
        r.setBytes(bytes);
    }
}
