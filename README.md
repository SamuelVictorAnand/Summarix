# SmartDocAI Document Summarizer (Java Swing)

## Project Description
SmartDocAI is a Java Swing application providing document summarization for plain text files.
It features a modern UI, asynchronous background processing, and report saving.

## Features
- Upload TXT files via a clean GUI
- Progress bar during summarization
- Summarizes sections of text by extracting key sentences
- Download summary as text report

## Requirements
- Java SE 8+
- No external dependencies

## Build and Run
1. Compile:  
   `javac -d bin src/com/smartdocai/**/*.java`

2. Run:  
   `java -cp bin com.smartdocai.MainApp`

## Usage
- Click 'Upload TXT File' to select a document.
- Click 'Summarize' to process.
- View summary on screen.
- Click 'Download Report' to save summary.
