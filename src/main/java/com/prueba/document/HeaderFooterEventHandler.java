package com.prueba.document;


import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.IOException;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.util.List;

class HeaderFooterEventHandler implements IEventHandler {
        protected Document doc;
        protected String headerHtml;

        protected String footer;

        public HeaderFooterEventHandler(Document doc, String headerHtml, String footer) {
            this.doc = doc;
            this.headerHtml = headerHtml;
            this.footer = footer;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);


            // Set the header
            try {
                List<IElement> elements = HtmlConverter.convertToElements(headerHtml);
                for (IElement element : elements) {
                    if (element instanceof IBlockElement) {
                        Canvas canvas = new Canvas(pdfCanvas, pdfDoc, new Rectangle(pageSize.getX() + 50, pageSize.getTop() - 200, pageSize.getWidth() - 100, 200));
                        canvas.add((IBlockElement)element);
                    } else if (element instanceof Image) {
                        doc.add((Image)element);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            try {
                List<IElement> elements = HtmlConverter.convertToElements(footer);
                for (IElement element : elements) {
                    if (element instanceof IBlockElement) {
                        Canvas canvas = new Canvas(pdfCanvas, pdfDoc, new Rectangle(pageSize.getX() + 40, 30, pageSize.getWidth() - 100, 50));
                        canvas.add((IBlockElement)element);
                    } else if (element instanceof Image) {
                        doc.add((Image)element);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            // Set the footer
            Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pageSize);
            canvas.showTextAligned(new Paragraph("Página: "+pageNumber + "/" + pdfDoc.getNumberOfPages()), pageSize.getRight() - 30, 30, TextAlignment.RIGHT);

            pdfCanvas.release();
        }
    }

