package example.x;



import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

import static java.util.Arrays.asList;

public class Test1 {
    public static void main(String[] args) {
        List<String> rows = asList("x", "y", "x");

//        Map<String, List<String>> map = new HashMap<>();
//
//        rows.forEach(row -> {
//            if(map.containsKey(row)) {
//                map.get(row).add(row);
//            } else {
//                ArrayList<String> list = new ArrayList<>();
//                list.add(row);
//                map.put(row, list);
//            }
//        });
//
//        System.out.println(map);

        Function<String, List<String>> value = (val) -> {
            List<String> list = new ArrayList<>();
            list.add(val);
            return list;
        };
        BinaryOperator<List<String>> merge = (old, latest)->{
            old.addAll(latest);
            return old;
        };

        Map<String, List<String>> collect1 = rows.stream()
                .map(row -> row+1)
//                .collect(Collectors.toMap(Function.identity(), Arrays::asList)); // collision exception
                .collect(Collectors.toMap(Function.identity(), value, merge)); // {x1 -> [x1, x1], y1 -> [y1]}
        System.out.println(collect1);

        Function<Pair<String, String>, List<String>> valuePair = (val) -> {
            List<String> list = new ArrayList<>();
            list.add(val.snd);
            return list;
        };

        Map<String, List<String>> collect = rows.stream()
                .map(row -> new Pair<>(row, row + 1))
                .collect(toMap(
                        key -> key.fst,
                        valuePair,
                        merge
                ));

        System.out.println(collect);
    }
}
