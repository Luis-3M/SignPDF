package PDFSignature;

import java.io.IOException;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

/**
 * An example of creating an AcroForm and an empty signature field from scratch.
 * 
 * An actual signature can be added by clicking on it in Adobe Reader.
 * 
 */
public final class CreateEmptySignatureForm {
	private CreateEmptySignatureForm() {
	}

	public static void main(String[] args) throws IOException {
		// Create a new document with an empty page.
		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			// Adobe Acrobat uses Helvetica as a default font and
			// stores that under the name '/Helv' in the resources dictionary
			PDFont font = PDType1Font.HELVETICA;
			PDResources resources = new PDResources();
			resources.put(COSName.getPDFName("Helv"), font);

			// Add a new AcroForm and add that to the document
			PDAcroForm acroForm = new PDAcroForm(document);
			document.getDocumentCatalog().setAcroForm(acroForm);

			// Add and set the resources and default appearance at the form
			// level
			acroForm.setDefaultResources(resources);

			// Acrobat sets the font size on the form level to be
			// auto sized as default. This is done by setting the font size to
			// '0'
			String defaultAppearanceString = "/Helv 0 Tf 0 g";
			acroForm.setDefaultAppearance(defaultAppearanceString);
			// --- end of general AcroForm stuff ---

			// Create empty signature field, it will get the name "Signature1"
			PDSignatureField signatureField = new PDSignatureField(acroForm);
			PDAnnotationWidget widget = signatureField.getWidgets().get(0);
			PDRectangle rect = new PDRectangle(50, 650, 200, 50);
			widget.setRectangle(rect);
			widget.setPage(page);
			page.getAnnotations().add(widget);

			acroForm.getFields().add(signatureField);

			document.save("target/EmptySignatureForm.pdf");
		}
	}
}
