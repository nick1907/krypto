import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Zadanie1
{
	private String metodaSzyfrowania;
	private String keyPath;
	private String keyID;
	private String password;
	private int length;
	private Cipher cipher = null;
	static Scanner skaner = new Scanner(System.in);
	
	public void encrypt(byte klucz[], File plik) throws Exception
	{
		SecretKeySpec klucze = new SecretKeySpec(klucz, "AES");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		byte daneZPliku[] = null;
		
		if (metodaSzyfrowania.equals("ECB"))
			cipher.init(Cipher.ENCRYPT_MODE, klucze);
		
		else if (metodaSzyfrowania.equals("OFB") || metodaSzyfrowania.equals("CTR"))
		{
			//zainicjalizuj wektor ktory posluzy do xorowania
			byte initializationVector [] = new byte[]{ 0x09, 0x12, 0x00, 0x17, 0x01, 0x10, 0x00, 0x0f,
								0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x01};
			IvParameterSpec ivPS = new IvParameterSpec(initializationVector);
		
			cipher.init(Cipher.ENCRYPT_MODE, klucze, ivPS);
		}

		if (plik.canRead())
		{
				//wczytanie pliku do szyfrowania
				fis = new FileInputStream(plik);
				daneZPliku = new byte [(int) plik.length()];
				fis.read(daneZPliku);
				
				//szyfrowanie pliku
				byte szyfrogram [] = new byte [cipher.getOutputSize(daneZPliku.length)];
				length = 0;
				length = cipher.update(daneZPliku, 0, daneZPliku.length, szyfrogram, 0);
				length += cipher.doFinal(szyfrogram, length);
				
				//zapisywanie szyfrogramu do pliku
				plik.delete();
				fos = new FileOutputStream(plik);
				fos.write(szyfrogram);
		}
	}
	
	public byte[] decrypt(byte klucz[], File plik, boolean flaga) throws Exception
	{
		SecretKeySpec klucze = new SecretKeySpec(klucz, "AES");
		
		if (metodaSzyfrowania.equals("ECB"))
			cipher.init(Cipher.DECRYPT_MODE, klucze);
		
		else if (metodaSzyfrowania.equals("OFB") || metodaSzyfrowania.equals("CTR"))
		{
			//zainicjalizuj wektor ktory posluzy do xorowania
			byte initializationVector [] = new byte[]{ 0x09, 0x12, 0x00, 0x17, 0x01, 0x10, 0x00, 0x0f,
								0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x01};
			IvParameterSpec ivPS = new IvParameterSpec(initializationVector);
		
			cipher.init(Cipher.DECRYPT_MODE, klucze, ivPS);
		}
		
		if (plik.canRead())
		{
			FileInputStream fis = new FileInputStream(plik);
			
			//wczytanie zaszyfrowanego pliku
			byte szyfrogram[] = new byte [(int) plik.length()];
			fis.read(szyfrogram);
			
			//odszyfrowywanie pliku
			byte dane [] = new byte [szyfrogram.length];//[cipher.getOutputSize(length)];
			int ptLength = cipher.update(szyfrogram, 0, szyfrogram.length, dane);
			ptLength += cipher.doFinal(dane, ptLength);
			fis.close();
			
			if (flaga)	
				return dane;
			
			//zapisanie odszyfrowanego pliku
			FileOutputStream fos = new FileOutputStream(plik);
			fos.write(dane);
			fos.flush();
			fos.close();
		}
		return null;
	}
	
	public Zadanie1(String tryb, String sciezka, String ID) throws Exception
	{
		if (tryb.equals("ECB"))
		{
			metodaSzyfrowania = "ECB";
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
		}
		else if (tryb.equals("CTR"))
		{
			cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
			metodaSzyfrowania = "CTR";
		}
		else if (tryb.equals("OFB"))
		{
			cipher = Cipher.getInstance("AES/OFB/PKCS5Padding", "BC");
			metodaSzyfrowania = "OFB";
		}
		else
			throw new Exception("Podano nieprawidlowy typ pracy.");
		
		keyPath = sciezka;
		keyID = ID;
	}
	
	public Zadanie1(String tryb) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
	{
		if (tryb.equals("ECB"))
		{
			metodaSzyfrowania = "ECB";
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
		}
		else if (tryb.equals("CTR"))
		{
			cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
			metodaSzyfrowania = "CTR";
		}
		else if (tryb.equals("OFB"))
		{
			cipher = Cipher.getInstance("AES/OFB/PKCS5Padding", "BC");
			metodaSzyfrowania = "OFB";
		}
	}
	
	public Zadanie1()
	{
		
	}
	
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, UnrecoverableKeyException, CertificateException, IOException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException
	{
		KeyStore key = KeyStore.getInstance("JCEKS");//KeyStore.getInstance(KeyStore.getDefaultType());
			
		Zadanie1 zadanie = null;
		if (args.length == 3)
		{
			try 
			{
				zadanie = new Zadanie1(args[0], args[1], args[2]);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Nie podano wszystkich wymaganych parametrow!\n");
			System.exit(1);
		}
		
		System.out.println("Podaj haslo do pliku z haslem: ");
		zadanie.password = skaner.nextLine();

		InputStream keystoreStream = new FileInputStream(zadanie.keyPath.concat(zadanie.keyID));
		
		if (keystoreStream != null)
		{
			System.out.println(zadanie.password);
			key.load(keystoreStream, zadanie.password.toCharArray());
		}
		
		byte klucz [] = key.getKey("alias", zadanie.password.toCharArray()).getEncoded();
		zadanie.menu(klucz);
	}
	
	private void menu(byte klucz[])//, SecretKeySpec klucze)
	{
		int opcja = 0;
		do
		{
			System.out.println("Wybierz odpowiednia opcje menu (cyfra calkowita): ");
			System.out.println("1. szyfrowanie pliku,");
			System.out.println("2. deszyfrowanie pliku,");
			System.out.println("3. wyswietlenie wybranego trybu szyfrowania,");
			System.out.println("4 lub inna niż poprzednie wyjście z programu.");
			
			skaner.reset();
			opcja = Integer.parseInt(skaner.nextLine());
			
			if (opcja == 1)
			{
				String nazwaPliku = null;
				System.out.println("Podaj nazwe pliku do zaszyfrowania: ");
				File plik;
				skaner.reset();
				if (skaner.hasNextLine())
					nazwaPliku = skaner.nextLine();
				plik = new File(nazwaPliku);
				if (nazwaPliku != null)
				{
					try
					{
						encrypt(klucz, plik);
					} catch (Exception e)
					{
						System.out.println("Nie udalo sie zaszyfrowanie pliku.");
					}
				}
			}
			else if (opcja == 2)
			{
				String nazwaPliku = null;
				System.out.println("Podaj nazwe pliku do odszyfrowania: ");
				File plik;
				skaner.reset();
				if (skaner.hasNextLine())
					nazwaPliku = skaner.nextLine();
				plik = new File(nazwaPliku);
				if (nazwaPliku != null)
				{
					try
					{
						decrypt(klucz, plik, false);
					} catch (Exception e)
					{
						System.out.println("Nie udalo sie odszyfrowanie pliku.");
					}
				}
			}
			else if (opcja == 3)
				System.out.println("Tryb szyfrowania to: " + metodaSzyfrowania);
		} while (opcja < 4 && opcja > 0);
		
		if (skaner != null)
			skaner.close();
	}		
}