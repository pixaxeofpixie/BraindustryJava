package Gas.content;

import Gas.type.Gas;
import arc.graphics.Color;
import mindustry.ctype.ContentList;
import mindustry.ctype.ContentType;

public class Gasses implements ContentList {
    public static Gas oxygen;
    public static Gas methane;
    public void load() {
       /* oxygen =new Gas("oxygen"){
            {
                this.explosiveness=0.1f;
                this.flammability=1;
                this.localizedName="Oxygen";
                this.color=Color.valueOf("70FFF8");
            }
        };*/
        methane =new Gas("methane"){
            {
                this.localizedName="Methane";
                this.color= Color.valueOf("bcf9ff");
                this.flammability=0.7f;
                this.explosiveness=0.9f;
            }
        };
    }
}
