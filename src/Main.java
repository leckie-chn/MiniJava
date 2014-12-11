import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;





public class Main {

	public static void main(String[] args) {
		PrintStream stdout = System.out;
		InputStream stdin = System.in;
		
		boolean debug = false;
		
		// minijava => spiglet
		ByteArrayOutputStream spgStream = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(spgStream));
		
		minijava.minijava2piglet.Main.main(null);
		
		if (debug == true){
			stdout.println("spiglet code:");
			stdout.println(spgStream.toString());
			stdout.println();
		}
		// spiglet => kanga
		ByteArrayInputStream kgInput = new ByteArrayInputStream(spgStream.toByteArray());
		
		System.setIn(kgInput);
		
		ByteArrayOutputStream kgOutput = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(kgOutput));
		
		spiglet.spiglet2kanga.Main.main(null);
		
		if (debug == true){
			stdout.println("kanga code:");
			stdout.println(kgOutput.toString());
			stdout.println();
		}
		// kanga => mips
		System.setOut(stdout);
		
		ByteArrayInputStream MipsInput = new ByteArrayInputStream(kgOutput.toByteArray());
		
		System.setIn(MipsInput);
		
		kanga.kanga2mips.Main.main(null);
		
		
		
	}
	
	
}