package io.game.numbers.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GivenSet {

    final List<int[]> numIndexPerms;
    final List<int[]> opsIndexPerms;
    final int[] numSet;
    final int target;
    final static Operator[] ops = new Operator[]
            {Operator.ADD, Operator.SUBTRACT, Operator.MULTIPLY, Operator.DIVIDE};

    public GivenSet(int[] numSet, int numTaget) {
        this.numSet = numSet;
        target = numTaget;
        numIndexPerms = new ArrayList<>(); setNumIndexPerms();
        opsIndexPerms = new ArrayList<>(); setOpsIndexPerms();
    }

    public boolean isSetGood(){
        boolean res = false;
        for (int[] numIndex : numIndexPerms){
            for (int[] opsIndex : opsIndexPerms){
                if (isGiveNumAndOpsPermutationsGood(numIndex, opsIndex)){
                    printGoodPermutation(numIndex, opsIndex);
                    res = true;
                }
            }
        }
        return res;
    }

    private void printGoodPermutation(int[] numIndex, int[] opsIndex) {
        String toPrint = String.format("%d %s %d %s %d %s %d %s %d = %d",
                numSet[numIndex[0]], ops[opsIndex[0]],
                numSet[numIndex[1]], ops[opsIndex[1]],
                numSet[numIndex[2]], ops[opsIndex[2]],
                numSet[numIndex[3]], ops[opsIndex[3]],
                numSet[numIndex[4]],
                target);
        System.out.println(toPrint);
    }

    private void setOpsIndexPerms() {
        for (int a=0; a<4; a++){
            for (int b=0; b<4; b++){
                for (int c=0; c<4; c++){
                    for (int d=0; d<4; d++){
                        opsIndexPerms.add(new int[] {a, b, c, d});
                    }
                }
            }
        }
    }

    private boolean isAllNumbersUnique(HashSet<Integer> tempSet, int... nums) {
        tempSet.clear();
        for (int n : nums){
            if (!tempSet.add(n)){
                return false;
            }
        }
        return true;
    }

    private boolean isGiveNumAndOpsPermutationsGood(int[] indexSet, int[] opsIndex) {
        return (calculateResult(indexSet, opsIndex) == target);
    }

    private int calculateResult(int[] indexSet, int[] opsIndex) {
        try {
            return new ExpressionBuilder(numSet[indexSet[0]])
                    .composeOp(ops[opsIndex[0]], numSet[indexSet[1]])
                    .composeOp(ops[opsIndex[1]], numSet[indexSet[2]])
                    .composeOp(ops[opsIndex[2]], numSet[indexSet[3]])
                    .composeOp(ops[opsIndex[3]], numSet[indexSet[4]])
                    .build();
        } catch (ExpressionBuilder.DeadEndResultException e) {
            return -1;
        }
    }

    private void setNumIndexPerms() {
        HashSet<Integer> reusableHashSet;
        reusableHashSet = new HashSet<>();
        for (int a=0; a<5; a++){
            for (int b=0; b<5; b++){
                for (int c=0; c<5; c++){
                    for (int d=0; d<5; d++){
                        for (int e=0; e<5; e++){
                            if (a+b+c+d+e == 10){
                                if (isAllNumbersUnique(reusableHashSet, a, b, c, d, e)){
                                    numIndexPerms.add(new int[]{a, b, c, d, e});
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public String toString(){
        return String.format("%d ? %d ? %d ? %d ? %d <> %d", numSet[0],numSet[1],numSet[2],numSet[3],numSet[4], target);
    }

    private enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE;



        @Override
        public String toString(){
            switch (this){
                case ADD:
                    return "+";
                case SUBTRACT:
                    return "-";
                case MULTIPLY:
                    return "*";
                case DIVIDE:
                    return "/";
                default:
                    return this.name();
            }
        }

    }
    private class ExpressionBuilder {
        final int state;

        public ExpressionBuilder(int initNum){
            state = initNum;
        }

        public ExpressionBuilder composeOp(Operator type, int arg) throws DeadEndResultException {
            return new ExpressionBuilder(calculate(arg, type));
        }

        public int build(){
            return state;
        }

        private int calculate(int arg, Operator type) throws DeadEndResultException {
            double tempAns;
            switch (type){
                case ADD:
                    tempAns = state + arg;
                    break;
                case SUBTRACT:
                    tempAns = state - arg;
                    break;
                case MULTIPLY:
                    tempAns = state * arg;
                    break;
                case DIVIDE:
                    tempAns = (double)state / (double)arg;
                    break;
                default:
                    throw new IllegalArgumentException(
                            String.format("Operator '%s' is not supported", type.name()));
            }
            if (tempAns < 0 || !isNumAWhole(tempAns)){
                throw new DeadEndResultException();
            } else {
                return (int)tempAns;
            }
        }

        private boolean isNumAWhole(double tempAns) {
            return tempAns % 1 == 0.0;
        }

        private class DeadEndResultException extends Exception {

        }
    }
}
