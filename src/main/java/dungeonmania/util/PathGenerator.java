package dungeonmania.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathGenerator {
/**
     * Written by Alvin Cherk, taken from https://edstem.org/au/courses/8675/discussion/945088
     * Returns the path of a new file to be created that is relative to resources/.
     * Will add a `/` prefix to path if it's not specified.
     *
     * @precondition the `resources/directory` MUST exist before, otherwise throws NullPointerException
     * @param directory Relative to resources/ will add an implicit `/` prefix if
     *                  not given.
     * @param newFile   file name
     * @return the full path as a string
     * @throws NullPointerException directory does not exist
     */
    public static String getPathForNewFile(String directory, String newFile) throws IOException, NullPointerException {
        if (!directory.startsWith("/"))
            directory = "/" + directory;
        try {
            Path root = Paths.get(FileLoader.class.getResource(directory).toURI());
            return root.toString() + "/" + newFile;
        } catch (URISyntaxException e) {
            throw new FileNotFoundException(directory);
        }
    }
}
