package PDFSignature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import com.sun.xml.internal.txw2.Document;

public class SignPDF extends CreateSignatureBase {

	public SignPDF(KeyStore keystore, char[] pin) throws KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException, IOException, CertificateException {
		super(keystore, pin);
	}

	private static char[] password = "1111".toCharArray();
	private static KeyStore keystore;
	private static Provider provider;

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		// Setup PKCS11 Provider
		String pkcs11conf = "/home/l3m/javaApp/pkcs11.cfg";
		// provider = new sun.security.pkcs11.SunPKCS11(pkcs11conf);
		provider = new sun.security.pkcs11.SunPKCS11(pkcs11conf);
		Security.addProvider(provider);
		// Load keystore
		keystore = KeyStore.getInstance("PKCS11", provider);
		keystore.load(null, password);

		SignPDF sign = new SignPDF(keystore, password);

		File inFile = new File("/home/l3m/Smart_Card.pdf");
		String name = inFile.getName();
		String substring = name.substring(0, name.lastIndexOf('.'));

		File outFile = new File(inFile.getParent(), substring + "_signed.pdf");
		sign.signDocument(inFile, outFile);
	}

	private void signDocument(File inFile, File outFile) throws IOException {
		if (inFile == null || !inFile.exists()) {
			throw new FileNotFoundException("Document for signing does not exist");
		}
		FileOutputStream fileOS = new FileOutputStream(outFile);
		System.out.println("[?] Loading PDF...");
		try (PDDocument doc = PDDocument.load(inFile)) {
			System.out.println("[+] Successfully Loaded");
			try {
				System.out.println("[?] Signing PDF...");
				PDSignature signature = new PDSignature();
				signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
				signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
				signature.setName("Luis Moreira");
				signature.setLocation("Massarelos, Porto");
				signature.setReason("To get the job done.");
				signature.setSignDate(Calendar.getInstance());
				doc.addSignature(signature, this);
				doc.saveIncremental(fileOS);
				System.out.println("[+] Successfully Signed");
				System.out.println();
				System.out.println(outFile.getAbsolutePath());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("[-] Unsuccessfully Signed");
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("[-] Unsuccessfully Loaded");
			e.printStackTrace();
		}
	}
}
