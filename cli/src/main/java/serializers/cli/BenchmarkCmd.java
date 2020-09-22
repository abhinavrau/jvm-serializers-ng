package serializers.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "bench",
    sortOptions = false,
    headerHeading = "Usage:%n%n",
    synopsisHeading = "%n",
    descriptionHeading = "%nDescription:%n%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    header = "Runs micro benchmark using jmh",
    mixinStandardHelpOptions = true,
    description = "Runs micro benchmark using jmh on provided serializers")
public class BenchmarkCmd {

    @CommandLine.ParentCommand
    private Cli parent; // picocli injects reference to parent command
}
