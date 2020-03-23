package svarog.language;

public class LanguageLoader {
    private InterfaceTranslations interfaceTranslations;

    private static LanguageLoader languageLoader;
    
    private LanguageLoader(InterfaceTranslations.languages option) {
        interfaceTranslations = new InterfaceTranslations(option);
    }

    public static LanguageLoader getInstance(InterfaceTranslations.languages option) {
    	if(languageLoader == null)
    		languageLoader = new LanguageLoader(option);
    	
    	return languageLoader;
    }

    public String getValue(String key) {
        return interfaceTranslations.getValue(key);
    }

    public String getKey(String value) {
        return interfaceTranslations.getKey(value);
    }
}