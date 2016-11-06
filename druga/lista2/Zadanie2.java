import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;
import javax.crypto.NoSuchPaddingException;
import javazoom.jl.decoder.JavaLayerException;

public class Zadanie2
{
	private static Konfiguracja konfig;
	private static Scanner skaner;
	
	public Zadanie2()
	{
	}
	
	public static void main(String[] args)
	{
		skaner = new Scanner(System.in);
		konfig = new Konfiguracja(skaner);
		
		try 
		{
			//wczytaj klucz z pliku konfiguracyjnego
			konfig.odszyfrujKonfiguracje();
		} 
		catch (KeyStoreException | FileNotFoundException e) 
		{
			System.out.println("Nie udalo sie odszyfrowac pliku konfiguracyjnego.");
		}
		
		String path = new String();
		System.out.println("Podaj dokladna sciezke do zaszyfrowanego pliku z muzyka: ");
		path = skaner.nextLine();
		
		//wczytaj muzyke
		File plik = new File(path);
		
		if (plik.canRead())
		{
			Zadanie1 zadanie1 = null;
			
			try 
			{
				zadanie1 = new Zadanie1("ECB");
			} catch (NoSuchAlgorithmException | NoSuchProviderException
					| NoSuchPaddingException e) 
			{
				System.out.println("Wybrano niewlasciwy tryb dzialania.");
			}
			
			System.out.println("Gotowy");
			System.out.println("Od ktorego momentu mam zaczac odtwarzanie?");
			String temp = new String();
			temp = skaner.nextLine();
			
			int czas = 0;
			czas = Integer.parseInt(temp);
			Odtwarzacz odtwarzacz = null;
			
			try 
			{
				//wczytaj i wlacz muzyke
				odtwarzacz = new Odtwarzacz(zadanie1.decrypt(konfig.zwrocKlucz(), plik, true), czas);
				odtwarzacz.play();
			} catch (Exception e) 
			{
				System.out.println("Nie udalo sie otworzyc pliku.");
			}
			
			String command = new String();
			System.out.println("Podaj \"stop\" by zakonczyc: ");
			command = skaner.nextLine();
			
			//jesli wpisano stop to zakoncz odtwarzanie
			if (command.equals("stop"))
			{
				try 
				{
					odtwarzacz.stop();
				} catch (JavaLayerException e) 
				{
					System.out.println("Przerwano odtwarzanie.");
				}
			}
		}
		
		if (skaner != null)
			skaner.close();
	}
}