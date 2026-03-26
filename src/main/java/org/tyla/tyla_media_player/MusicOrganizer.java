//package org.tyla.tyla_media_player;
//
//import java.util.*;
//
//public class MusicOrganizer {
//
//    private HashMap<String, Artist> artists = new HashMap<>();
//
//    public MusicOrganizer(HashMap<String, HashMap<String, String>> audioMap) {
//
//        for (Map.Entry<String, HashMap<String, String>> entry : audioMap.entrySet()) {
//
//            String path = entry.getKey();
//            HashMap<String, String> meta = entry.getValue();
//
//            Song song = new Song(path, meta);
//
//            String artistKey = normalize(song.artist);
//            String albumKey = normalize(song.album);
//
//            Artist artist = artists.computeIfAbsent(
//                    artistKey,
//                    k -> new Artist(song.artist)
//            );
//
//            Album album = artist.albums.computeIfAbsent(
//                    albumKey,
//                    k -> new Album(song.album)
//            );
//
//            album.songs.add(song);
//            album.songs.sort(Comparator.comparingInt(s -> s.trackNumber));
//        }
//    }
//
//    private String normalize(String s) {
//        return s.trim().toLowerCase();
//    }
//
//    public HashMap<String, Artist> getArtists() {
//        return artists;
//    }
//
//    public void printArtistView() {
//        System.out.println("Artist View");
//        for (Artist artist : artists.values()) {
//            System.out.println("Artist: " + artist.name);
//
//            List<Song> allSongs = new ArrayList<>();
//            for (Album album : artist.albums.values()) {
//                allSongs.addAll(album.songs);
//            }
//
//            allSongs.sort(Comparator
//                    .comparing((Song s) -> s.album)
//                    .thenComparingInt(s -> s.trackNumber));
//
//            for (Song song : allSongs) {
//                System.out.printf("    %02d - %s (Album: %s)%n",
//                        song.trackNumber,
//                        song.title,
//                        song.album
//                );
//            }
//
//            System.out.println();
//        }
//    }
//
//    public List<Song> getSongsByArtist(String artistName) {
//
//        String key = artistName.trim().toLowerCase();
//        Artist artist = artists.get(key);
//
//        List<Song> allSongs = new ArrayList<>();
//
//        if (artist == null) return allSongs;
//
//        for (Album album : artist.albums.values()) {
//            allSongs.addAll(album.songs);
//        }
//
//        return allSongs;
//    }
//
//    public List<Album> getAllAlbums() {
//
//        List<Album> allAlbums = new ArrayList<>();
//
//        for (Artist artist : artists.values()) {
//            allAlbums.addAll(artist.albums.values());
//        }
//
//        return allAlbums;
//    }
//
//    public void printAlbumView() {
//        System.out.println("Album View");
//        for (Artist artist : artists.values()) {
//            for (Album album : artist.albums.values()) {
//                System.out.println("Album: " + album.name + " (Artist: " + artist.name + ")");
//                album.songs.sort(Comparator.comparingInt(s -> s.trackNumber));
//                for (Song song : album.songs) {
//                    System.out.printf("    %02d - %s%n", song.trackNumber, song.title);
//                }
//                System.out.println();
//            }
//        }
//    }
//}