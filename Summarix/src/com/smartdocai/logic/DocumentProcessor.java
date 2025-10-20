package com.smartdocai;
package com.smartdocai.logic;

import com.smartdocai.model.Section;
import java.io.*;
import java.util.*;

public class DocumentProcessor {
    public List<Section> processDocument(File file) throws IOException {
        List<String> rawSections = splitIntoSections(file);
        List<Section> sections = new ArrayList<>();

        int index = 1;
        for (String raw : rawSections) {
            // Use Summarizer utility to create summary
            String summary = Summarizer.summarizeText(raw);
            sections.add(new Section(index++, raw, summary));
        }
        return sections;
    }

    private List<String> splitIntoSections(File file) throws IOException {
        List<String> sections = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (sb.length() > 0) {
                        sections.add(sb.toString().trim());
                        sb = new StringBuilder();
                    }
                } else {
                    sb.append(line).append("\n");
                }
            }
        }
        if (sb.length() > 0) sections.add(sb.toString().trim());
        return sections;
    }
}
