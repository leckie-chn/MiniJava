import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;





public class Main {

	public static void main(String[] args) {
		PrintStream stdout = System.out;
		PrintStream stderr = System.err;
		
		boolean debug = false;
		
		// minijava => spiglet
		ByteArrayOutputStream spgStream = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(spgStream));
		
		minijava.minijava2piglet.Main.main(null);
		
		if (debug == true){
			stderr.println("spiglet code:");
			stderr.println(spgStream.toString());
			stderr.println();
		}
		// spiglet => kanga
		ByteArrayInputStream kgInput = new ByteArrayInputStream(spgStream.toByteArray());
		
		System.setIn(kgInput);
		
		ByteArrayOutputStream kgOutput = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(kgOutput));
		
		spiglet.spiglet2kanga.Main.main(null);
		
		if (debug == true){
			stderr.println("kanga code:");
			stderr.println(kgOutput.toString());
			stderr.println();
		}
		// kanga => mips
		System.setOut(stdout);
		
		ByteArrayInputStream MipsInput = new ByteArrayInputStream(kgOutput.toByteArray());
		
		System.setIn(MipsInput);
		
		kanga.kanga2mips.Main.main(null);
		
		
		
	}
	
	
}