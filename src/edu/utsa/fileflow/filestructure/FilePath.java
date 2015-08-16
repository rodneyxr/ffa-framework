package edu.utsa.fileflow.filestructure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilePath {

    private static final Pattern PATTERN_PATH_TO_FILE = Pattern.compile("(.*)[/\\\\].+[/\\\\]");
    private static final Pattern PATTERN_FILE_SEPARATOR = Pattern.compile("[/\\\\]");

    private String filepath;
    private boolean isdir;

//    public FilePath(String filepath) {
//        this.filepath = clean(filepath);
//        this.isdir = filepath.matches("[/\\\\]$");
//    }

    public FilePath(String filepath, boolean isdir) {
        this.filepath = clean(filepath);
        this.isdir = isdir;
    }

    public boolean isDir() {
        return isdir;
    }

    /**
     * Get the directory that a file is in. If the file is already a directory then the directory will be returned.
     *
     * @return the directory that the file is in
     */
    public FilePath getPathToFile() {
        if (isdir) return this;
        Matcher matcher = PATTERN_PATH_TO_FILE.matcher(filepath);
        return new FilePath(matcher.group(0), true);
//        return new FilePath(filepath.replaceFirst("[/\\\\][^/\\\\]+[/\\\\]?$", ""), true);
//        return new FilePath(matcher.group(1), true);
    }

    public String[] getTokens() {
        if (filepath.length() == 0)
            return new String[0];
//        return filepath.split("[/\\\\]");
        return PATTERN_FILE_SEPARATOR.split(filepath);
    }

    public String getFileName() {
        return filepath.replaceFirst("^.*[/\\\\]", "");
    }

    public String getFilePath() {
        return filepath;
    }

    @Override
    public String toString() {
        return filepath;
    }

    /**
     * Cleans a string to represents a filepath by removing redundant slashes.
     *
     * @param path the path to clean.
     * @return the String that was passed in for chaining methods.
     */
    private String clean(String path) {
        path = path.trim();
        path = path.replaceAll("[/\\\\]", "/");
        return path;
    }

}
