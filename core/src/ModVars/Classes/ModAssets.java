package ModVars.Classes;

import ModVars.modVars;
import arc.Events;
import arc.files.Fi;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import braindustry.gen.AssetsFolders;

import static ModVars.modFunc.print;

public class ModAssets {
    ObjectMap<String,Seq<Fi>> folderMap=new ObjectMap<>();
    public ModAssets(){
        Events.run(ModEventType.ModContentLoad.class, this::init);
    }
private void addAll(Seq<Fi> seq,Fi folder){
        Seq.with(folder.list()).each(subFolder->{
            if (subFolder.isDirectory()){
                addAll(seq,subFolder);
            } else {
                seq.add(subFolder);

            }
        });
}
public Fi get(String folder,String filename){
        return !folderMap.containsKey(folder)?null: folderMap.get(folder).find(fi->fi.name().equals(filename));
}
    public void init() {
        if (modVars.modInfo!=null){
            Fi root=modVars.modInfo.root;
            Seq<String> assetsNames= AssetsFolders.getFolders();
            Seq.with(root.list()).each(folder->{
                String folderName = folder.name();
                if (folderName.contains("sprites") || folderName.contains("bundles") || !folder.isDirectory() || !assetsNames.contains(folderName))return;
                Seq<Fi> value = new Seq<>();
                addAll(value,folder);
//                print("folder: @,value: \n\t@",folder,value.toString("\n\t"));

//                Seq.with(folder.list())
                folderMap.put(folderName, value);
            });
        }
    }
}
