package com.example.cmunte.chatapp;

import java.util.ArrayList;
import java.util.List;

public class ProfilePictures {

    public static String[] pictures = {"https://www.iconspng.com/uploads/orc-6/orc-6.png", "https://www.iconspng.com/uploads/570616/570616.png","https://www.iconspng.com/uploads/bebe/bebe.png", "https://www.iconspng.com/uploads/570605/570605.png"};
    public static String[] titles = {"Orc","Boy","Bebe", "Punk"};

    public static int getPictureIndex(String url){

        for (int i = 0; i < pictures.length; i++){
            if (pictures[i] == url){
                return i;
            }
        }
        return -1;
    }


}
