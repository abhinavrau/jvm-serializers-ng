package serializers.cli;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import data.media.MediaContent;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMax;
import picocli.CommandLine;
import serializers.MediaContentTestGroup;
import serializers.TestGroup;
import serializers.core.metadata.SerializerProperties;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@CommandLine.Command(name = "info",
		sortOptions = false,
		headerHeading = "Usage:%n%n",
		synopsisHeading = "%n",
		descriptionHeading = "%nDescription:%n%n",
		parameterListHeading = "%nParameters:%n",
		optionListHeading = "%nOptions:%n",
		header = "Outputs information about supported serializers",
		mixinStandardHelpOptions = true,
		description = "Shows metadata associated with serializers supported by this tool")
public class InfoCmd implements Callable<Void> {

	private static final String TabeOptionHelp = " Hint: Combine with -t (--table) option to see extended metadata";
	@CommandLine.ParentCommand
	private Cli parent; // picocli injects reference to parent command

	@CommandLine.Option(names = {"-a", "--all"}, description = "List all the available serializers and relevant metadata." + TabeOptionHelp)
	private boolean listAll = false;

	@CommandLine.Option(names = {"-n", "--names"}, description = "List only the names of supported serializer names with no related metadata i.e. json, xml, protobuf etc.")
	private boolean names = false;

	@CommandLine.Option(names = {"-b", "--binary"}, description = "List binary names serializers and relevant metadata." + TabeOptionHelp)
	private boolean binary = false;

	@CommandLine.Option(names = {"-j", "--json"}, description = "List json names serializers and relevant metadata." + TabeOptionHelp)
	private boolean json = false;

	@CommandLine.Option(names = {"-x", "--xml"}, description = "List xml names serializers relevant metadata." + TabeOptionHelp)
	private boolean xml = false;

	@CommandLine.Option(names = {"-e", "--text"}, description = "List text names serializers and relevant metadata i.e. json & xml (same as calling with -jx)." + TabeOptionHelp)
	private boolean text = false;

	@CommandLine.Option(names = {"-t", "--table"}, description = "Outputs all metadata in a more readable ascii table")
	private boolean tableFormat = false;

	@CommandLine.Option(names = {"-f", "--find"}, arity = "1..*", description = "Finds serializers with the one or more <keywords> associated with it in it's metadata." + TabeOptionHelp)
	private String[] keywords = null;


	@Override
	public Void call() {

		MediaContentTestGroup groups = parent.registerAllSerializers();

		ImmutableTable<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>> table
				= ImmutableTable.copyOf(groups.media.entries);


		NavigableSet<String> output = new TreeSet<>();

		if (listAll) {

			//
			filterSerializers(output, table, stringSerializerPropertiesEntryCell -> true);

		} else {
			if (names) {
				filterSerializers(output, table, stringSerializerPropertiesEntryCell -> true);
			}

			if (json) {

				filterSerializers(output, table, stringSerializerPropertiesEntryCell ->
				{
					SerializerProperties input = stringSerializerPropertiesEntryCell.getColumnKey();
					return input.getFormat().equals(SerializerProperties.Format.JSON);

				});

			}
			if (xml) {
				Set<SerializerProperties> filter = Sets.filter(table.columnKeySet(),
						input -> input.getFormat().equals(SerializerProperties.Format.XML));

				filterSerializers(output, table, stringSerializerPropertiesEntryCell ->
				{
					SerializerProperties input = stringSerializerPropertiesEntryCell.getColumnKey();
					return input.getFormat().equals(SerializerProperties.Format.XML);

				});

			}
			if (text) {

				filterSerializers(output, table, stringSerializerPropertiesEntryCell ->
				{
					SerializerProperties input = stringSerializerPropertiesEntryCell.getColumnKey();
					return input.getFormat().equals(SerializerProperties.Format.XML)
							|| input.getFormat().equals(SerializerProperties.Format.JSON);

				});

			}
			if (binary) {

				filterSerializers(output, table, stringSerializerPropertiesEntryCell ->
				{
					SerializerProperties input = stringSerializerPropertiesEntryCell.getColumnKey();
					return input.getFormat().equals(SerializerProperties.Format.BINARY)
							|| input.getFormat().equals(SerializerProperties.Format.BINARY_JDK_COMPATIBLE);

				});


			}
			if (keywords != null) {
				final Set<String> keywordSet = Sets.newHashSet(keywords);

				filterSerializers(output, table, stringSerializerPropertiesEntryCell ->
						Iterables.all(keywordSet, input ->
								stringSerializerPropertiesEntryCell.getColumnKey().values().contains(input)));

			}


		}
		if (tableFormat) {
			printAsciiTable(output);
		} else {
			for (String s : output) {
				System.out.println(s);
			}
		}

		return null;
	}

	private void printAsciiTable(NavigableSet<String> output) {
		AsciiTable asciiTable = new AsciiTable();
		asciiTable.getRenderer().setCWC(new CWC_LongestWordMax(30));
		asciiTable.setPaddingLeftRight(1);
		asciiTable.addRule();
		AT_Row header = null;
		if (names) {
			header = asciiTable.addRow("#", "Name");
		} else {
			header = asciiTable.addRow("#", "Name", "Format", "ApiStyle", "Value Type", "Mode", "Features", "Optimization", "URL");
		}
		int index = 0;
		for (String s : output) {
			asciiTable.addRule();
			if (names) {
				asciiTable.addRow(++index, s);
			} else {

				Iterator<String> splitter = Splitter.on(",").split(s).iterator();
				asciiTable.addRow(++index, splitter.next(), splitter.next(), splitter.next(), splitter.next(), splitter.next(), splitter.next(), splitter.next(), splitter.next());
			}
		}
		asciiTable.addRule();
		System.out.println(asciiTable.render(80));
	}

	/**
	 * Writes to the output set the details of the serializer(s) that match the given Predicate   
	 *
	 * @param output
	 * @param table
	 * @param predicate
	 */
	private void filterSerializers(NavigableSet<String> output,
	                               ImmutableTable<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>> table,
	                               Predicate<Table.Cell<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>>> predicate) {


		for (Table.Cell<String, SerializerProperties, TestGroup.Entry<MediaContent, Object>> entry : table.cellSet()) {
			if (predicate.test(entry)) {
				SerializerProperties properties = entry.getColumnKey();
				if (names) {
					output.add(properties.getName());
				} else {
					if (tableFormat) {
						Joiner joiner = Joiner.on(",").useForNull("N/A");
						output.add(joiner.join(properties.getName(), properties.getFormat(),
								properties.getApiStyle(), properties.getValueType(),
								properties.getMode(), properties.getFormattedFeatures(),
								properties.getOptimizedDescription(), properties.getProjectURL()));
					} else {
						output.add(properties.getShortName());
					}
				}

			}
		}

	}

}
