package serializers;


public class CorrectnessCheckHelper {


  public void runCorrectness(MediaContentTestGroup groups) throws Exception {
    MediaItemBenchmark benchmark = new MediaItemBenchmark() {
      @Override
      public void addTests(MediaContentTestGroup groups) {

      }
    };
    benchmark.checkForCorrectness(groups, "data/media.1.json");
    //TODO: This object breaks a lot of tests as it has nulls
    benchmark.checkForCorrectness(groups, "data/media.2.json");
    benchmark.checkForCorrectness(groups, "data/media.3.json");
    benchmark.checkForCorrectness(groups, "data/media.4.json");

  }

}
