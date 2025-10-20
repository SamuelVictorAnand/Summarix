package com.smartdocai;
package com.smartdocai.ui;

import com.smartdocai.logic.DocumentProcessor;
import com.smartdocai.model.Section;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private final JButton uploadBtn = new JButton("Upload TXT File");
    private final JButton summarizeBtn = new JButton("Summarize");
    private final JButton downloadBtn = new JButton("Download Report");
    private final JLabel fileNameLabel = new JLabel("No file uploaded");
    private final JTextArea outputArea = new JTextArea();
    private final JProgressBar progressBar = new JProgressBar();

    private File currentFile;
    private List<Section> sections;

    public MainFrame() {
        setTitle("SmartDocAI - Document Summarizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Buttons panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(new Color(45, 52, 54));

        uploadBtn.setBackground(new Color(9, 132, 227));
        uploadBtn.setForeground(Color.WHITE);

        summarizeBtn.setBackground(new Color(0, 184, 148));
        summarizeBtn.setForeground(Color.WHITE);
        summarizeBtn.setEnabled(false);

        downloadBtn.setBackground(new Color(108, 92, 231));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setEnabled(false);

        fileNameLabel.setForeground(Color.WHITE);
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);

        controlPanel.add(uploadBtn);
        controlPanel.add(summarizeBtn);
        controlPanel.add(downloadBtn);
        controlPanel.add(fileNameLabel);
        controlPanel.add(progressBar);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                fileNameLabel.setText("File: " + currentFile.getName());
                outputArea.setText("");
                sections = null;
                summarizeBtn.setEnabled(true);
                downloadBtn.setEnabled(false);
            }
        });

        summarizeBtn.addActionListener(e -> {
            if (currentFile != null) {
                runSummarization();
            }
        });

        downloadBtn.addActionListener(e -> {
            if (sections != null) {
                JFileChooser saver = new JFileChooser();
                saver.setDialogTitle("Save summary report");
                int saveResult = saver.showSaveDialog(this);
                if (saveResult == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = saver.getSelectedFile();
                    saveReport(fileToSave);
                }
            }
        });
    }

    private void runSummarization() {
        summarizeBtn.setEnabled(false);
        uploadBtn.setEnabled(false);
        downloadBtn.setEnabled(false);
        progressBar.setVisible(true);
        outputArea.setText("");

        SwingWorker<List<Section>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Section> doInBackground() throws Exception {
                DocumentProcessor processor = new DocumentProcessor();
                sections = processor.processDocument(currentFile);
                return sections;
            }

            @Override
            protected void done() {
                try {
                    List<Section> result = get();
                    String summaryText = result.stream()
                        .map(s -> "Section " + s.getNumber() + ":\n" + s.getSummary())
                        .collect(Collectors.joining("\n\n"));
                    outputArea.setText(summaryText);
                    downloadBtn.setEnabled(true);
                    fileNameLabel.setText("File: " + currentFile.getName() + " (Summarized)");
                } catch (Exception e) {
                    outputArea.setText("Error during summarization: " + e.getMessage());
                } finally {
                    progressBar.setVisible(false);
                    summarizeBtn.setEnabled(true);
                    uploadBtn.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void saveReport(File saveFile) {
        try(PrintWriter pw = new PrintWriter(saveFile)) {
            for (Section s : sections) {
                pw.println("Section " + s.getNumber() + ":");
                pw.println(s.getSummary());
                pw.println();
            }
            JOptionPane.showMessageDialog(this, "Report saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to save report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
