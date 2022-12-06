package ru.urfu.training.genetic;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.urfu.network.Network;
import ru.urfu.network.properties.Dataset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class GeneticTraining {
    @NonNull
    private final Integer MEMBERS_SIZE;
    @NonNull
    private final Integer GENERATIONS;
    private List<Network> MEMBERS = new ArrayList<>();
    @NonNull
    private final Integer CHAMPIONS_SIZE;

    public void training() {
        for (int generation = 0; generation < GENERATIONS; generation++) {
            if (generation == 0) {
                for (int member = 0; member < MEMBERS_SIZE; member++) {
                    Network network = new Network(3, 1, Dataset.TRAIN);
                    network.setNumberNeuronsHiddenLayers(64);
                    network.traverseDataset();
                    MEMBERS.add(network);
                }
            }
            MEMBERS.sort(Comparator.comparingDouble(Network::getERROR));
            MEMBERS = new ArrayList<>(MEMBERS.subList(0, CHAMPIONS_SIZE));
            System.out.println("Generation: " + (generation + 1) + " Error: " + MEMBERS.get(0).getERROR());
            while (MEMBERS.size() < MEMBERS_SIZE) {
                double geneticMethod = Math.random();
                int firstParentIndex = (int) (Math.random() * (MEMBERS.size() - 1));
                List<List<List<Double>>> weights;
                // Crossing
                if (geneticMethod - 0.5 >= 0) {
                    int secondParentIndex = (int) (Math.random() * (MEMBERS.size() - 1));
                    while (firstParentIndex == secondParentIndex) {
                        secondParentIndex = (int) (Math.random() * (MEMBERS.size() - 1));
                    }
                    weights = Genetic.crossing(MEMBERS.get(firstParentIndex).getWEIGHTS(),
                            MEMBERS.get(secondParentIndex).getWEIGHTS());
                }

                // Mutation
                else {
                    weights = Genetic.mutation(MEMBERS.get(firstParentIndex).getWEIGHTS());
                }
                Network childMember = new Network(3, 1, Dataset.TRAIN);
                childMember.setNumberNeuronsHiddenLayers(64);
                childMember.setWEIGHTS(weights);
                childMember.traverseDataset();
                MEMBERS.add(childMember);
            }
        }
    }
}
