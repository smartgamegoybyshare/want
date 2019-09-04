package com.sport.want.Language;

public class SetLanguage {

    private LanguageListener languageListener;

    public void setListener(LanguageListener mLanguageListener){
        languageListener = mLanguageListener;
    }

    public void isSet(){
        if(languageListener != null){
            languageListener.setLanguage();
        }
    }
}
