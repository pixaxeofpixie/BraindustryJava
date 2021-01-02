package braindustry.content;
import Gas.type.Gas;
import arc.graphics.Color;

public class ModGasses {
    public static Gas methane;
    public void load(){
        methane =new Gas("methane"){
            {
                this.color= Color.valueOf("bcf9ff");
            }
        };
    }
}
