package com.mephone.fontello;

public class Main {

    public static void main(String[] args) {

        //translate  ( translateX  / scaleX, translateY  / scaleY)
        //scale( 1000 /( width   / scaleX) , 1000 / ( height  / scaleY))
        FontelloService mService = FontelloService.getInstance();
        mService.doButtonCmd(FontelloService.CMD_GEN_CONFIG, new String[]{});
    }
}
