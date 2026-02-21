package org.tyla.tyla_media_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class librarySearch {

    public static List<String> searchLibrary(HashMap<String, Map<String, Object>> library, String keyword) {

        List<String> results = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> mediaEntry : library.entrySet()) {

            String mediaID = mediaEntry.getKey();
            Map<String, Object> mdata = mediaEntry.getValue();

            for(Object value : mdata.values()){

                if(value != null && value.toString().toLowerCase().contains(keyword.toLowerCase())){

                    results.add(mediaID);
                    break;
                }
            }
        }

        return results;
    }
}
