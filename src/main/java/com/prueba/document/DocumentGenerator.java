package com.prueba.document;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class DocumentGenerator {

	public byte[] htmlToPdf(String processedHtml, String headerHtml, String footer) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);

			DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, false);

			ConverterProperties converterProperties = new ConverterProperties();
			converterProperties.setFontProvider(defaultFont);

			// Create a new pdf document.
			PdfDocument pdf = new PdfDocument(pdfwriter);
			Document document = new Document(pdf);

			// Set the event handler.
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderFooterEventHandler(document, headerHtml, footer));

			// Convert the html to pdf.
			HtmlConverter.convertToPdf(processedHtml, pdf, converterProperties);

			byteArrayOutputStream.close();
			byteArrayOutputStream.flush();

			return byteArrayOutputStream.toByteArray();
		} catch(Exception ex) {
			// Maneja la excepción según tus necesidades
		}

		return null;
	}

}
