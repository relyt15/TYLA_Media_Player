package org.tyla.tyla_media_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class librarySearch {

    public static List<Song> searchLibrary(ArrayList<Song> list, String keyword) {

        List<Song> results = new ArrayList<>();

        for (Song s : list) {

            HashMap<String, String> mappies = s.metaMap();
            for(Map.Entry<String, String> e : mappies.entrySet()){

                if (e.getValue().toLowerCase().contains((keyword.toLowerCase()))){

                    results.add(s);
                }
            }
        }

        return results;
    }
}
