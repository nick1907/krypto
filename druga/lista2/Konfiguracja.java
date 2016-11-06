import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;

public  class Konfiguracja 
{
	private static byte klucz1[];
	
	//klucz do pliku konfiguracyjnego
	private final byte klucz2[] = {(byte)11011100, (byte)11011101, (byte)11011111, (byte)11011000,
								   (byte)11010100, (byte)11001100, (byte)11011100, (byte)01011100,
								   (byte)11010011, (byte)11011101, (byte)11111100, (byte)00011100,
								   (byte)11101111, (byte)11110000, (byte)11010100, (byte)10000000};
	
	public Konfiguracja(Scanner s)
	{
		Scanner skaner = s;
		try 
		{	
			File plik = new File("zadanie_config.txt");
			if (plik.canRead())
			{
				String tryb = new String();
				String path = new String();
				String keyID = new String();
				String alias = new String();
				String password = new String();
				
				System.out.println("Podaj tryb kodowania: ");
				tryb = skaner.nextLine();

				System.out.println("Podaj sciezke do pliku z kluczem: ");
				path = skaner.nextLine();
				
				System.out.println("Podaj nazwe pliku z kluczem: ");
				keyID = skaner.nextLine();
				
				System.out.println("Podaj alias do pliku z kluczem: ");
				alias = skaner.nextLine();
				
				System.out.println("Podaj haslo do pliku z kluczem: ");
				password = skaner.nextLine();
				
				//zapisanie danych do pliku konfiguracyjnego
				//plik konfiguracyjny (zadanie_config.txt):
				//tryb_kodowania sciezka_do_klucza nazwa_klucza alias_do_klucza haslo_do_klucza
				PrintWriter zapis = new PrintWriter(plik);
				zapis.write(tryb + " " + path + " " + keyID + " " + alias + " " + password);
				zapis.flush(); //upewnij sie ze przeniesiono dane do pliku konfiguracyjnego
				
				//zaszyfruj plik konfiguracyjny
				Zadanie1 zadanie = new Zadanie1("CTR");
				zadanie.encrypt(klucz2, plik);
				
				if (zapis != null)
					zapis.close();
			}
		} catch (KeyStoreException e) 
		{
			e.printStackTrace();
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		} catch (CertificateException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void odszyfrujKonfiguracje() throws KeyStoreException, FileNotFoundException
	{
		try 
		{
			File plik = new File("zadanie_config.txt");
			if (plik.canRead())
			{
				//odszyfruj plik konfiguracyjny
				Zadanie1 zadanie = new Zadanie1("CTR");
				zadanie.decrypt(klucz2, plik, false);
//						0				1			2			3				4
//				trybKodowania, sciezkaDoKlucza, nazwaKlucza, aliasKlucza, hasloDoKlucza;
				String temp, tab[];

				//zaszyfruj plik konfiguracyjny
				Scanner sk = new Scanner(plik);			
				temp = sk.nextLine();
				zadanie.encrypt(klucz2, plik);
				if (sk != null)
					sk.close();

				//plik konfiguracyjny (zadanie_config.txt):
				//tryb_kodowania sciezka_do_klucza nazwa_klucza alias_do_klucza haslo_do_klucza
				
				tab = temp.split(" ");//rozdziel dane z pliku konfiguracyjnego

				if (tab.length == 5)
				{
					//Wczytanie klucza (BouncyCastle) z pliku kofiguracyjnego
					KeyStore key = KeyStore.getInstance("JCEKS");
					InputStream is = new FileInputStream(tab[1].concat(tab[2]));
					//pobranie z klucza do muzyki z klucza (BouncyCastle)
					if (is != null)
					{
						key.load(is, tab[4].toCharArray());
						klucz1 = key.getKey(tab[3], tab[4].toCharArray()).getEncoded();
					}
				}
			}
		}
		catch (UnrecoverableKeyException | KeyStoreException
				| NoSuchAlgorithmException e1) 
		{
			e1.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//funkcja zwracajaca klucz do zaszyfrowanej muzyki
	public byte [] zwrocKlucz()
	{
		return klucz1;
	}
}