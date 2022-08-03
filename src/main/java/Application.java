import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * Copyright (C) 2022 Urban Compass, Inc.
 */

public class Application {

  public static void main(String[] args) throws IOException {
    String pdfName = "rfc793-TCP.pdf";
    URL pdfURL = Application.class.getClassLoader().getResource(pdfName);

    // Loading PDF
    PDDocument document = PDDocument.load(new File(pdfURL.getPath()));

    // Splitter Class
    Splitter splitting = new Splitter();

    // Splitting the pages into multiple PDFs
    List<PDDocument> pages = splitting.split(document);

    List<Integer[]> sampleSplitResults = Arrays.asList(
        new Integer[] {1, 1},
        new Integer[] {2, 3},
        new Integer[] {4, 6}
    );

    for (Integer[] splitPage : sampleSplitResults) {
      Integer startPage = splitPage[0];
      Integer endPage = splitPage[1];
      PDFMergerUtility mergerUtility = new PDFMergerUtility();

      for (int i = startPage; i <= endPage; i++) {
        PDDocument sourceDocument = pages.get(i - 1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        sourceDocument.save(outputStream);
        sourceDocument.close();
        mergerUtility.addSource(new ByteArrayInputStream(outputStream.toByteArray()));
      }
      String fileName = String.format("%s-%d-%d.pdf", pdfName, startPage, endPage);
      mergerUtility.setDestinationFileName(fileName);
      // save
      mergerUtility.mergeDocuments();
    }

    System.out.println("Split Pdf Successfully.");
    document.close();
  }
}
