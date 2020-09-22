package serializers.avro;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import serializers.MediaContentTestGroup;
import serializers.Serializer;
import serializers.Transformer;
import serializers.core.DataLoader;
import serializers.core.util.Pair;

import java.util.concurrent.TimeUnit;


public class AvroBenchmark<MediaContent> {



    @State(Scope.Thread)
    public static class SerializationState<T> {

        public Object objectToSerialize;
        public Serializer genericSerializer;
        public Serializer specificSerializer;

        @Setup(Level.Iteration)
        public void doSetup() throws Exception {
            DataLoader<data.media.MediaContent, data.media.MediaContent> val =
                    new DataLoader<>(data.media.MediaContent.class, data.media.MediaContent.class);

            Object temp = val.loadTestDataFromPath("data/media.1.json");
            MediaContentTestGroup groups = new MediaContentTestGroup();
            AvroGeneric.register(groups);

            Pair<Serializer, Transformer> genericPair = DataLoader.getSerializer(groups);
            genericSerializer = genericPair.getKey();
            Transformer transformer = genericPair.getValue();
            if ( transformer!= null) {
                objectToSerialize = transformer.forward(temp);
            }
            else {
                objectToSerialize = temp;
            }

        }
    }


    @Benchmark @BenchmarkMode(Mode.SampleTime) @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object avroGenericSerialization( SerializationState state) throws Exception {

        Object object = state.genericSerializer.serialize(state.objectToSerialize);
        return object;
    }

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(AvroBenchmark.class.getSimpleName())
                .threads(4)
                .forks(1)
                .addProfiler()
                .build();

        new Runner(opt).run();

    }

}
