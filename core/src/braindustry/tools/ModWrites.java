package braindustry.tools;

import arc.util.io.ReusableByteOutStream;
import arc.util.io.Writes;

import java.io.DataOutputStream;

public class ModWrites extends Writes {
    final ReusableByteOutStream r = new ReusableByteOutStream(8192);

    public ModWrites() {
        super(null);
        output=new DataOutputStream(r);
    }
    public byte[] getBytes(){
        return r.getBytes();
    }
}
