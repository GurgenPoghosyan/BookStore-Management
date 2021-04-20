package com.internship.bookstore.common.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Gurgen Poghosyan
 */
@Component
public class FileSplitter {

    public List<File> splitFile(File file, int mb) throws IOException {
        int counter = 0;
        List<File> files = new ArrayList<>();
        int sizeOfChunk = 1024 * 1024 * mb;
        String eof = System.lineSeparator();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String title = br.readLine();
            String line = br.readLine();
            while (Objects.nonNull(line)) {
                File newFile = new File(file.getParent(), String.format("%03d_%s", ++counter, file.getName()));
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    int fileSize = 0;
                    line = title + eof + line;
                    StringBuilder builder = new StringBuilder();
                    while (Objects.nonNull(line)) {
                        byte[] bytes = (line + eof).getBytes(Charset.defaultCharset());
                        if (fileSize + bytes.length > sizeOfChunk) {
                            try {
                                out.write(builder.toString().getBytes(Charset.defaultCharset()));
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        } else {
                            builder.append(line).append(eof);
                            fileSize += bytes.length;
                            try {
                                line = br.readLine();
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    files.add(newFile);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return files;
    }
}
