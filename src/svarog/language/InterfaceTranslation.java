package svarog.language;

public class InterfaceTranslation {
    private String key;
    private String value;

    InterfaceTranslation(String key, String value){
        this.value = value;
        this.key = key;
    }

    String getKey() {
        return key;
    } 
    
    void setKey(String key) {
        this.key = key;
    }
    
    String getValue() {
        return value;
    }
    
    void setValue(String value) {
        this.value = value;
    }
    
    void setVariables(String key, String value) {
        this.value = value;
        this.key = key;
    }
}
