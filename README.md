# SignPDF
Java application that signs a PDF file using a Certificate stored on a Smart Card.

Instructions
------------

For You to be able to run this code You'll need a Smart Card with a valid Signing Certificate. Then You need to add to your Java Project multiple JAR files including Apache PDFBox:

https://pdfbox.apache.org/download.cgi

The main class SignPDF has my PIN on it so You need to change that according to your Smart Card information and You also need to install the opensc-tool so You can communicate with your Smart Card.
