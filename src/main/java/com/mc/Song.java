package com.mc;

import java.io.File;

public class Song {

    private final File file;

    public Song(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file.getName();
    }

    @Override
    public String toString() {
        return getName();
    }

}
