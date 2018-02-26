package com.canonical.democlient;

/**
 * Created by 문선호 on 2016-10-07.
 */
public class ClassHandler {

    public static Class getClassByUri( String uri ){
        Class result_class = null;
        switch (uri){
            case "buzzer" :
                result_class = PageBuzzer.class;
                break;
            case "led" :
                result_class = PageLed.class;
                break;
            case "light" :
                result_class = PageLight.class;
                break;
            case "temperature" :
                result_class = PageTemperature.class;
                break;
            case "sound" :
                result_class = PageSound.class;
                break;
            case "button" :
                result_class = PageButton.class;
                break;
            case "touch" :
                result_class = PageTouch.class;
                break;
        }
        return result_class;
    }
}
