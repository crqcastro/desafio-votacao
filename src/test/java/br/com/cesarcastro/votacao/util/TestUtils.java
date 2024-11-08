package br.com.cesarcastro.votacao.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {
    private static final Integer MIN_LIST_SIZE = 1;
    private static final Integer MAX_LIST_SIZE = 10;

    public static <T> T generateRandom(Class<T> clazz) {
        return generateRandom(clazz, null);
    }

    public static <T> T generateRandom(Class<T> clazz, Integer listSize) {
        Randomizer<Integer> idIntegerRandomizer = new IntegerRangeRandomizer(1, 100);
        Randomizer<Long> idLongRandomizer = new LongRangeRandomizer(1L, 100L);

        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.collectionSizeRange(Objects.isNull(listSize) ? MIN_LIST_SIZE : listSize, Objects.isNull(listSize) ? MAX_LIST_SIZE : listSize);
        parameters.randomize(
                field -> field.getName().toLowerCase().contains("id") && field.getType().equals(Integer.class),
                idIntegerRandomizer
        );
        parameters.randomize(
                field -> field.getName().toLowerCase().contains("id") && field.getType().equals(Long.class),
                idLongRandomizer
        );
        EasyRandom er = new EasyRandom(parameters);
        return er.nextObject(clazz);
    }
}
