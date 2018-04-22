package serializers.cli;

import picocli.CommandLine;

import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Compares jvm serializer performance",
		name = "sercompare", mixinStandardHelpOptions = true)
public class SerCompare implements Callable<Void> {

	@CommandLine.Parameters(index = "0", description = "The file whose checksum to calculate.")
	private File file;

	@CommandLine.Option(names = {"-a", "--algorithm"}, description = "MD5, SHA-1, SHA-256, ...")
	private String algorithm = "MD5";

	public static void main(String[] args) throws Exception {
		// CheckSum implements Callable, so parsing, error handling and handling user
		// requests for usage help or version help can be done with one line of code.
		CommandLine.call(new SerCompare(), System.err, args);
	}

	@Override
	public Void call() throws Exception {
		// your business logic goes here...
		byte[] fileContents = Files.readAllBytes(file.toPath());
		byte[] digest = MessageDigest.getInstance(algorithm).digest(fileContents);
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(digest));
		return null;
	}

}
