package com.smartdocai;
package com.smartdocai.logic;

public class Summarizer {
    /**
     * This method implements a simple heuristic summarizer:
     * It returns the first and last sentences joined by "...".
     * You can expand with NLP or ML models later.
     */
    public static String summarizeText(String text) {
        String[] sentences = text.split("(?<=[.!?])\\s+");
        if (sentences.length == 0) return "";
        if (sentences.length == 1) return sentences[0];
        return sentences[0] + " ... " + sentences[sentences.length - 1];
    }
}
