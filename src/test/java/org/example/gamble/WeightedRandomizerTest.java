package org.example.gamble;

import io.vavr.collection.LinkedHashMap;
import io.vavr.control.Try;
import lombok.val;
import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class WeightedRandomizerTest {

    private static final Supplier<Random> SEEDED_RANDOM_SUPPLIER = () -> new Random(0x0F0F0F);

    private static final int PICK_REPEAT_COUNT = 10_000;

    private static final Map<Integer, Double> EMPTY_MAP = Map.of();

    // Vavr's LinkedHashMap is used because tests need entry order to be preserved, Java's LinkedHashMap API is shitty.
    private static final Map<Integer, Double> ZERO_WEIGHTS_MAP = LinkedHashMap.of(1, 0d, 2, 0d, 3, 0d)
            .toJavaMap();
    private static final Map<Integer, Double> NEGATIVE_WEIGHT_MAP = LinkedHashMap.of(1, 0d, 2, 1d, 3, -1d)
            .toJavaMap();

    private static Stream<Arguments> invalidOptionsWithCorrespondingMessageArguments() {
        return Stream.of(
                Arguments.of(EMPTY_MAP, "List of options cannot be empty"),
                Arguments.of(ZERO_WEIGHTS_MAP, "Sum of option weights cannot be zero"),
                Arguments.of(NEGATIVE_WEIGHT_MAP, "Weight of an option cannot be negative")
        );
    }

    private static Stream<Map<Integer, Double>> allIntOptionWeightMaps() {
        return Stream.concat(intOptionWeightMaps(), invalidIntOptionWeightMaps());
    }

    private static Stream<Map<Integer, Double>> intOptionWeightMaps() {
        return Stream.of(
                LinkedHashMap.of(1, 0.5d, 2, 0.5d),
                LinkedHashMap.of(1, 0.1d, 2, 0.9d),
                LinkedHashMap.of(1, 0.3d, 2, 03.d, 3, 0.3d),
                LinkedHashMap.of(1, 1e-5, 2, 0d, 3, 0d),
                LinkedHashMap.of(1, 0d, 2, 0d, 3, 1e3),
                LinkedHashMap.of(1, 1e3, 2, 1d, 3, 1d),
                LinkedHashMap.of(1, 10d, 2, 5d, 3, 1d, 4, 0.5d, 5, 0.1d)
        ).map(LinkedHashMap::toJavaMap);
    }

    private static Stream<Map<Integer, Double>> invalidIntOptionWeightMaps() {
        return Stream.of(
                EMPTY_MAP,
                ZERO_WEIGHTS_MAP,
                NEGATIVE_WEIGHT_MAP
        );
    }

    @ParameterizedTest
    @MethodSource("invalidOptionsWithCorrespondingMessageArguments")
    public void shouldThrowWhenInvalidParametersProvided(Map<Integer, Double> invalidOptions, String message) {
        // given
        val randomizer = new WeightedRandomizer(SEEDED_RANDOM_SUPPLIER.get());

        // when
        // then
        assertThatThrownBy(() -> randomizer.pick(invalidOptions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(message);
    }

    @ParameterizedTest
    @MethodSource("intOptionWeightMaps")
    public void shouldPickWeightedOptionWithRelativeProbability(Map<Integer, Double> weightedIntOptions) {
        // given
        val randomizer = new WeightedRandomizer(SEEDED_RANDOM_SUPPLIER.get());
        val weightsSum = weightedIntOptions.values()
                .stream()
                .mapToDouble(d -> d)
                .sum();

        // when
        val pickResults = Stream.generate(() -> randomizer.pick(weightedIntOptions))
                .limit(PICK_REPEAT_COUNT)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // then
        for (val option : weightedIntOptions.entrySet()) {
            val actualPickProbability = pickResults.getOrDefault(option.getKey(), 0L) / (double) PICK_REPEAT_COUNT;
            val expectedPickProbability = option.getValue() / weightsSum;

            assertThat(actualPickProbability).isCloseTo(expectedPickProbability, Offset.offset(6e-3));
        }
    }

    @ParameterizedTest
    @MethodSource("allIntOptionWeightMaps")
    public void shouldPickIdenticalResultsWithAllOfMethods(Map<Integer, Double> weightedIntOptions) {
        // given
        val optionRecords = weightedIntOptions.entrySet()
                .stream()
                .map(entry -> new WeightedIntOption(entry.getKey(), entry.getValue()))
                .toList();

        val optionSet = weightedIntOptions.keySet();
        val weights = weightedIntOptions.values()
                .stream()
                .mapToDouble(d -> d)
                .toArray();

        // when
        val pickedWithMap = pickWith(
                () -> new WeightedRandomizer(SEEDED_RANDOM_SUPPLIER.get())
                        .pick(weightedIntOptions));

        val pickedWithFunction = pickWith(
                () -> new WeightedRandomizer(SEEDED_RANDOM_SUPPLIER.get())
                        .pick(optionRecords, WeightedIntOption::weight)
                        .value);

        val pickedWithWeights = pickWith(
                () -> new WeightedRandomizer(SEEDED_RANDOM_SUPPLIER.get())
                        .pick(optionSet, weights));

        // then
        if (pickedWithMap.isFailure()) {
            assertSameFailure(pickedWithMap, pickedWithFunction);
            assertSameFailure(pickedWithMap, pickedWithWeights);
        } else {
            assertThat(pickedWithMap.get())
                    .isEqualTo(pickedWithFunction.get())
                    .isEqualTo(pickedWithWeights.get());
        }
    }

    private void assertSameFailure(Try<Integer> expected, Try<Integer> actual) {
        assertThat(actual.isFailure()).isTrue();
        assertThat(actual.getCause())
                .isInstanceOf(expected.getCause().getClass())
                .hasMessage(expected.getCause().getMessage());
    }

    private Try<Integer> pickWith(Supplier<Integer> pickingMethod) {
        return Try.of(pickingMethod::get);
    }

    private record WeightedIntOption(int value, double weight) {
        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
}
