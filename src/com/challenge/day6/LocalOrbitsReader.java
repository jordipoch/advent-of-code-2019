package com.challenge.day6;

import com.challenge.day6.exception.LocalOrbitsReadingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalOrbitsReader {
    public static final Path baseDirectory = Paths.get("resources", "com", "challenge", "day6");

    public static LinkedHashMap<String, String> readLocalOrbits(String localOrbitsFileName) throws LocalOrbitsReadingException {
        LinkedHashMap<String, String> localOrbits;
        Path filePath = baseDirectory.resolve(Paths.get(localOrbitsFileName));

        try (Stream<String> lines = Files.lines(filePath)) {
            localOrbits = lines.map(n -> n.split("\\)"))
                    .collect(Collectors.toMap(n -> n[1], n -> n[0], (n, m) -> "duplicate", LinkedHashMap::new));
        } catch (IOException e) {
            throw new LocalOrbitsReadingException(filePath, e);
        } catch (Exception e) {
            throw new LocalOrbitsReadingException("Unexpected error processing file " + filePath.getFileName().toString(), e);
        }

        long nDuplicates = localOrbits.values().stream().filter("duplicate"::equals).count();
        if (nDuplicates > 0) {
            throw new LocalOrbitsReadingException(nDuplicates);
        }

        return localOrbits;
    }
}
