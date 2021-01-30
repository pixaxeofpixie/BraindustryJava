package ModVars;

public class ModEnums {
    public static <T extends Enum<T>> boolean contains(Class<T> enumClass,String name){
       Object obj=null;
       try {
           obj= Enum.valueOf(enumClass,name);
       } catch (Exception ignored){
       }
       return obj!=null;
    }
    public static enum CheatLevel{
        off,onlyAdmins,all
    }
}
