package Gas.content;

import Gas.type.Gas;
import arc.graphics.Color;
import mindustry.ctype.ContentList;
import mindustry.ctype.ContentType;

public class Gasses implements ContentList {
    public static Gas oxygen;
    public static Gas none;
    public void load() {
        none=new Gas("none"){

            @Override
            public ContentType getContentType() {
                return ContentType.mech_UNUSED;
            }
            {
                this.color=new Color(0,0,0,0);
            }
        };
        oxygen =new Gas("Oxygen"){
            {
                this.color=Color.valueOf("70FFF8");
            }
        };
    }
}
