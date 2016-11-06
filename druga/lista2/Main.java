import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;
//import org.bouncycastle.crypto.BlockCipher;
//import org.bouncycastle.crypto.engines.AESEngine;
//import org.bouncycastle.crypto.modes.CBCBlockCipher;
//import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

public class Main
{
	
    static byte[] encrypt(byte[] cleartext, byte klucz[])
    {
        StreamCipher rc4 = new RC4Engine();
        KeyParameter keyParam = new KeyParameter(klucz);
        rc4.init(true, keyParam);
        byte[] ciphertext = new byte[cleartext.length];
        rc4.processBytes(cleartext, 0, cleartext.length, ciphertext, 0);
        return ciphertext;
    }
    
    static byte[] decrypt(byte [] encrypted, byte klucz[])
    {
    	return encrypt(/*encrypt(*/encrypted, klucz)/*, klucz)*/;
    }
    
    private static void porownaj(byte tekst[], byte zaszyfrowany[], byte klucz[])
    {
    	System.out.println("Tekst oryginalny: " + show(tekst));
    	System.out.println("Klucz to: " + klucz);
    	byte [] odszyfrowany = decrypt(/*encrypt(*/zaszyfrowany, klucz)/*, klucz)*/;
    	int i = 0;
    	
    	if (tekst.length == zaszyfrowany.length)
    	{
    		while (i < tekst.length && odszyfrowany[i] == tekst[i])
    		{
    			++i;
    		}
    		
    		if (i == tekst.length)
    		{
    			System.out.println("\nOdszyfrowany: " + show(odszyfrowany)); 
    			System.out.println("Udalo sie rozszyfrowywanie.\n");
    		}
    		else
    			System.out.println("\nNie udalo sie rozszyfrowywanie.\n");
    	}
    	else
    		System.out.println("Podane dwa parametry sa roznej dlugosci\n");
    }
    
    public static String show(byte tablica [])
    {
    	String napis = new String();
    	
    	for (int i = 0; i < tablica.length; ++i)
    		napis += (char) tablica[i];

    	return napis;
    }
	
    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException 
    {
    	File plik = null;
//    	byte[] cleartext1 = null;
    	PrintWriter zapis = new PrintWriter("zaszyfrowany.txt");
    	KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
    	Scanner skaner = null;// = new Scanner(new File(args[0]));
    	if (args.length == 1)
    	{
    		plik = new File(args[0]);
    		skaner = new Scanner(plik);
    	}
    	
    	java.io.FileInputStream fis = null;

    	if (plik.canRead())
    	{
    		try
			{
    			try
    			{
    				byte cleartext1 [] = skaner.nextLine().getBytes("utf-8");
//					byte cleartext1 [] = "Witaj swiecie(Hello world)!".getBytes();
    				fis = new java.io.FileInputStream("key.bks");
//					key = KeyStore.getInstance("BKS");
					key.load(fis, "haslohaslo".toCharArray());
			    	byte klucz [] = key.getKey("certificatekey", "haslohaslo".toCharArray()).getEncoded();
					//cleartext1 = skaner.nextLine().getBytes( "utf-8");
			        zapis.print(encrypt(cleartext1, klucz));
			        porownaj(cleartext1, /*decrypt(encrypt(*/encrypt(cleartext1, klucz), klucz/*))*/);
    			}
    			catch (FileNotFoundException fnfe)
    			{
    				System.out.println(fnfe.getMessage());
    			}
    			finally
    			{
    				if (fis != null)
    					fis.close();
    			}
			}
    		catch (UnsupportedEncodingException e)
			{
				System.out.println("Nie udalo sie zapisac do pliku.\n");
			}
    	}
         
         zapis.close();
         skaner.close();
    }
}