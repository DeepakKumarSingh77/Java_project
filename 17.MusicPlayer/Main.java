import java.util.*;

interface Playable {
    List<Song> getSongs();
}

class Song implements Playable {
    String title;
    Artist artist;

    public Song(String title, Artist artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public List<Song> getSongs() {
        return List.of(this);
    }
}

class Playlist implements Playable {
    List<Song> songs = new ArrayList<>();

    public void addSong(Song song) {
        songs.add(song);
    }

    @Override
    public List<Song> getSongs() {
        return songs;
    }
}

class Album implements Playable {
    List<Song> songs = new ArrayList<>();

    public void addSong(Song song) {
        songs.add(song);
    }

    @Override
    public List<Song> getSongs() {
        return songs;
    }
}

class Artist {
    String name;
    List<Album> albums = new ArrayList<>();

    public Artist(String name) {
        this.name = name;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }
}


abstract class User {
    String name;

    public User(String name) {
        this.name = name;
    }

    abstract boolean canSkip();
}


class FreeUser extends User {

    public FreeUser(String name) {
        super(name);
    }

    @Override
    boolean canSkip() {
        return false;
    }
}


class PremiumUser extends User {

    public PremiumUser(String name) {
        super(name);
    }

    @Override
    boolean canSkip() {
        return true;
    }
}


class MusicPlayer {
    List<Song> currentQueue;
    int currentIndex;

    public void play(Playable item) {
        currentQueue = item.getSongs();
        currentIndex = 0;
        playCurrent();
    }

    private void playCurrent() {
        if (currentQueue == null || currentQueue.isEmpty()) {
            System.out.println("No songs to play");
            return;
        }
        System.out.println("Playing: " + currentQueue.get(currentIndex).title);
    }

    public void pause() {
        System.out.println("Paused");
    }

    public void next() {
        if (currentQueue == null || currentIndex >= currentQueue.size() - 1) {
            System.out.println("No next song");
            return;
        }
        currentIndex++;
        playCurrent();
    }
}

public class Main {
    public static void main(String[] args) {

        // Artist
        Artist artist = new Artist("Arijit Singh");

        // Songs
        Song s1 = new Song("Tum Hi Ho", artist);
        Song s2 = new Song("Channa Mereya", artist);

        // Album
        Album album = new Album();
        album.addSong(s1);
        album.addSong(s2);
        artist.addAlbum(album);

        // Playlist
        Playlist playlist = new Playlist();
        playlist.addSong(s1);
        playlist.addSong(s2);

        // User
        User user = new FreeUser("John");

        // Music Player
        MusicPlayer player = new MusicPlayer();

        // Play Playlist
        player.play(playlist);
        player.pause();
        player.play(playlist);
        player.next();
        // Play Album
        player.play(album);
    }
}