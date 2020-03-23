package svarog.language;

public class LanguageLoader {
    static int numberOfInstances = 0;
    InterfaceTranslations interfaceTranslations;

    public LanguageLoader(InterfaceTranslations.languages option){
        if(numberOfInstances>0){
            System.out.println("To many instances!!!");
            System.exit(-100);
        }else {
            interfaceTranslations = new InterfaceTranslations(option);
            numberOfInstances++;
        }
    }

    public void ReloadInterfaceTranslations(InterfaceTranslations.languages option){
        interfaceTranslations.Destroy();
        interfaceTranslations = new InterfaceTranslations(option);
    }

    public String getValue(String key){
        return interfaceTranslations.getValue(key);
    }

    public String getKey(String value){
        return interfaceTranslations.getKey(value);
    }
}