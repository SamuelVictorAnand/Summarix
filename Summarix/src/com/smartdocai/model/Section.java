package com.smartdocai;
package com.smartdocai.model;

public class Section {
    private final int number;
    private final String text;
    private final String summary;

    public Section(int number, String text, String summary) {
        this.number = number;
        this.text = text;
        this.summary = summary;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public String getSummary() {
        return summary;
    }
}
