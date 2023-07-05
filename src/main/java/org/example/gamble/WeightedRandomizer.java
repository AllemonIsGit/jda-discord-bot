package org.example.gamble;

import io.vavr.collection.Stream;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class WeightedRandomizer {

    private final Random random;

    public WeightedRandomizer() {
        this(new Random());
    }

    public <T> T pick(Iterable<T> options, double... weights) {
        val optionList = Stream.ofAll(options).toJavaList();

        val weightedOptions = IntStream.range(0, optionList.size())
                .boxed()
                .collect(Collectors.toMap(optionList::get, i -> getWeightAtIndex(i, weights)));

        return pick(weightedOptions);
    }

    private double getWeightAtIndex(int index, double[] weights) {
        return index > weights.length - 1 ? 0d : weights[index];
    }

    public <T> T pick(Map<T, Double> weightedOptions) {
        return pick(weightedOptions.keySet(), weightedOptions::get);
    }

    public <T> T pick(Iterable<T> options, Function<T, Double> weightFunction) {
        val optionList = Stream.ofAll(options).toJavaList();

        if (optionList.isEmpty()) {
            throw new IllegalArgumentException("List of options cannot be empty");
        }

        val weightsSum = optionList
                .stream()
                .mapToDouble(option -> getAndValidateWeight(option, weightFunction))
                .sum();

        if (weightsSum == 0) {
            throw new IllegalArgumentException("Sum of option weights cannot be zero " + optionList);
        }

        var randomWeight = random.nextDouble() * weightsSum;

        for (val option : optionList) {
            randomWeight -= weightFunction.apply(option);
            if (randomWeight <= 0) {
                return option;
            }
        }

        return optionList.get(optionList.size() - 1);
    }

    private <T> double getAndValidateWeight(T option, Function<T, Double> weightFunction) {
        val weight = weightFunction.apply(option);

        if (weight < 0) {
            val errorMessage = String.format("Weight of an option cannot be negative %s -> %f", option, weight);
            throw new IllegalArgumentException(errorMessage);
        }

        return weight;
    }
}
