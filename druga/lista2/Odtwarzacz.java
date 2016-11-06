import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Odtwarzacz
{
	private Thread watek;
	private Player odtwarzacz;
	
	public Odtwarzacz(byte plik[], final int czas)
	{
		InputStream myInputStream = new ByteArrayInputStream(plik);//zamiana byte [] na strumien
		BufferedInputStream bis = new BufferedInputStream(myInputStream);//zbuforowanie strumienia
		
 		try 
 		{ 			
			odtwarzacz = new Player(bis); //wczytanie strumienia do odtwarzania
		} catch (JavaLayerException e) 
		{
			e.printStackTrace();
		}
 		
 		//utworzenie watku odtwarzacza
		watek = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				try 
				{
					odtwarzacz.play(); //odtwarzanie muzyki
				} catch (JavaLayerException e) 
				{
					System.out.println("Przerwano odtwarzanie.");
				}
			}
		}, "Watek odtwarzacza");
	}
	
	//rozpoczyna odtwarzanie muzyki
	public void play()
	{
 		watek.start();
	}
	
	//przerywa odtwarzanie muzyki
	public void stop() throws JavaLayerException
	{
		System.out.println("Wyslano stop");
		odtwarzacz.close();
	}
}
