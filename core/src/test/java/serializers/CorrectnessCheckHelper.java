package serializers;



public class CorrectnessCheckHelper {



	public void runCorrectness(TestGroups groups) throws Exception
	{
		MediaItemBenchmark benchmark = new MediaItemBenchmark() {
			@Override
			public void addTests(TestGroups groups) {


			}
		};
		benchmark.checkForCorrectness(groups,"/Users/blownie/code/jvm-serializers-ng/core/src/main/resources/data/media.1.json");
		//TODO: This object breaks a lot of tests as it has nulls
		benchmark.checkForCorrectness(groups,"/Users/blownie/code/jvm-serializers-ng/core/src/main/resources/data/media.2.json");
		benchmark.checkForCorrectness(groups,"/Users/blownie/code/jvm-serializers-ng/core/src/main/resources/data/media.3.json");
		benchmark.checkForCorrectness(groups,"/Users/blownie/code/jvm-serializers-ng/core/src/main/resources/data/media.4.json");

	}

}
