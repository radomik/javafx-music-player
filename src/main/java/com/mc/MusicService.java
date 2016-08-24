package com.mc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicService {

    private static final Logger log = LoggerFactory.getLogger(MusicService.class);
    private static final MusicService INSTANCE = new MusicService();
    private InputStream playStream;
    private AdvancedPlayer player;
    private boolean playing = false;
    private int index = -1;
    private List<Song> songs = null;

    private MusicService() {
    }

    public static MusicService getInstance() {
        return INSTANCE;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void stop() {
        if (!isPlaying()) {
            return;
        }
        log.info("Stop player");
        playing = false;
        player.stop();
    }

    public void next() {
        //TODO
    }

    private final PlaybackListener playbackListener = new PlaybackListener() {
        @Override
        public void playbackFinished(PlaybackEvent evt) {
            log.info("Playback finished, id={} frame={}", evt.getId(), evt.getFrame());
            if (playing) {
                index++;
                index %= songs.size();
                play(index, false);
            }
        }

        @Override
        public void playbackStarted(PlaybackEvent evt) {
            log.info("Playback started, id={} frame={}", evt.getId(), evt.getFrame());
            playing = true;
        }

    };

    public void play(int index) {
        play(index, true);
    }

    public void play(int index, boolean doStop) {
        if (songs == null) {
            playing = false;
            return;
        }
        Song song = songs.get(index);
        this.index = index;

        log.info("Starting to play '{}'", song.getFile());
        if (doStop) {
            stop();
        }
        new Thread(() -> {
            log.info("Opening file stream");
            try (InputStream stream = new FileInputStream(song.getFile())) {
                try {
                    log.info("Creating player");
                    player = new AdvancedPlayer(stream);
                } catch (JavaLayerException ex) {
                    log.error("Error creating player [{}]", ex.getMessage());
                    return;
                }
                try {
                    player.setPlayBackListener(playbackListener);
                    try {
                        log.info("Starting playback");
                        player.play();
                        log.info("Finished playback");
                    } catch (JavaLayerException ex) {
                        log.error("Error starting playback [{}]", ex.getMessage());
                    }
                } finally {
                    log.info("Closing player");
                    player.close();
                }
            } catch (IOException ex) {
                log.error("I/O exception [{}]", ex.getMessage());
            }
        }).start();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
