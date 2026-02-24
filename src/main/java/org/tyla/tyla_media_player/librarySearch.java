package org.tyla.tyla_media_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class librarySearch {

    public static List<String> searchLibrary(AVLibrary library, String keyword) {

        List<String> results = new ArrayList<>();
        int total = library.getDirTotal();
        HashMap<String, HashMap<String, Object>> audioLib = library.getAudioMap();
        HashMap<String, HashMap<String, Object>> videoLib = library.getVideoMap();



        return results;
    }
}
